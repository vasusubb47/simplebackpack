package com.agent47.simple_backpack.item.quiver;

import com.agent47.simple_backpack.ModDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

@EventBusSubscriber
public final class QuiverSweepHandler {
    private QuiverSweepHandler() {}

    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        ItemStack quiverStack = QuiverUtils.findQuiverStack(player);
        if (quiverStack.isEmpty()) return;

        DataComponentType<ItemContainerContents> component = ModDataComponents.QUIVER_CONTENTS.get();
        var handler = new ItemAccessItemHandler(ItemAccess.forStack(quiverStack), component, QuiverItem.SLOTS);

        NonNullList<ItemStack> inventory = player.getInventory().getNonEquipmentItems();
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.get(slot);
            if (stack.isEmpty() || !QuiverUtils.isArrow(stack)) continue;

            ItemResource arrowResource = ItemResource.of(stack);
            try (Transaction tx = Transaction.openRoot()) {
                int inserted = handler.insert(arrowResource, stack.getCount(), tx);
                if (inserted > 0) {
                    tx.commit();
                    stack.shrink(inserted);
                    if (stack.isEmpty()) {
                        inventory.set(slot, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}