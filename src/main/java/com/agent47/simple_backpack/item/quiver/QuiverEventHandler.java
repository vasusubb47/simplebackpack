package com.agent47.simple_backpack.item.quiver;

import com.agent47.simple_backpack.ModDataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

@EventBusSubscriber
public final class QuiverEventHandler {
    private QuiverEventHandler() {}

    @SubscribeEvent
    public static void onItemPickupPre(ItemEntityPickupEvent.Pre event) {
        ItemStack groundStack = event.getItemEntity().getItem();
        if (!QuiverUtils.isArrow(groundStack)) return;

        Player player = event.getPlayer();
        ItemStack quiverStack = QuiverUtils.findQuiverStack(player);
        if (quiverStack.isEmpty()) return;

        DataComponentType<ItemContainerContents> component = ModDataComponents.QUIVER_CONTENTS.get();
        var handler = new ItemAccessItemHandler(ItemAccess.forStack(quiverStack), component, QuiverItem.SLOTS);

        ItemResource arrowResource = ItemResource.of(groundStack);
        try (Transaction tx = Transaction.openRoot()) {
            int inserted = handler.insert(arrowResource, groundStack.getCount(), tx);
            if (inserted > 0) {
                tx.commit();
                groundStack.shrink(inserted);
            }
        }

        if (groundStack.isEmpty()) {
            event.setCanPickup(TriState.FALSE);
        }
    }

    /**
     * PEEK ONLY. Reports what the quiver could offer, but never mutates it.
     * This can safely fire any number of times per shot without side effects.
     */
    @SubscribeEvent
    public static void onGetProjectile(LivingGetProjectileEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        // Don't clobber a non-arrow selection (e.g. a firework loaded into a crossbow).
        ItemStack current = event.getProjectileItemStack();
        if (!current.isEmpty() && !(current.getItem() instanceof ArrowItem)) return;

        ItemStack quiverStack = QuiverUtils.findQuiverStack(player);
        if (quiverStack.isEmpty()) return;

        ItemStack peeked = peekFirstArrow(quiverStack);
        if (!peeked.isEmpty()) {
            event.setProjectileItemStack(peeked);
        }
    }

    /**
     * REAL removal point. Fires exactly once per actual shot attempt for both bow and crossbow.
     * Must be guarded to server-side only, since bow's releaseUsing fires this on both sides.
     */
    @SubscribeEvent
    public static void onArrowLoose(ArrowLooseEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.isCanceled()) return;
        if (!event.hasAmmo()) return;

        Player player = event.getEntity();
        ItemStack quiverStack = QuiverUtils.findQuiverStack(player);
        if (quiverStack.isEmpty()) return;

        DataComponentType<ItemContainerContents> component = ModDataComponents.QUIVER_CONTENTS.get();
        var handler = new ItemAccessItemHandler(ItemAccess.forStack(quiverStack), component, QuiverItem.SLOTS);

        try (Transaction tx = Transaction.openRoot()) {
            for (int i = 0; i < handler.size(); i++) {
                ItemResource resource = handler.getResource(i);
                if (resource.isEmpty()) continue;

                int extracted = handler.extract(i, resource, 1, tx);
                if (extracted == 1) {
                    tx.commit();
                    return;
                }
            }
        }
    }

    private static ItemStack peekFirstArrow(ItemStack quiverStack) {
        DataComponentType<ItemContainerContents> component = ModDataComponents.QUIVER_CONTENTS.get();
        var handler = new ItemAccessItemHandler(ItemAccess.forStack(quiverStack), component, QuiverItem.SLOTS);

        for (int i = 0; i < handler.size(); i++) {
            ItemResource resource = handler.getResource(i);
            if (!resource.isEmpty()) {
                return resource.toStack(1);
            }
        }
        return ItemStack.EMPTY;
    }
}