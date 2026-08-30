package com.agent47.simple_backpack.item.pouch;

import com.agent47.simple_backpack.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;

public class PouchItem extends Item {
    public static final int SLOTS = 9;

    public PouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, @NonNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, playerInv, p) -> {
                        // The persistent, stack-backed handler — same one used for the capability.
                        var source = new ItemAccessItemHandler(
                                ItemAccess.forStack(stack), ModDataComponents.POUCH_CONTENTS.get(), SLOTS);

                        // A transient handler for the menu to work against during this session.
                        var menuHandler = new ItemStacksResourceHandler(SLOTS);
                        copyAllSlots(source, menuHandler);

                        return new PouchMenu(containerId, playerInv, menuHandler) {
                            @Override
                            public void removed(@NonNull Player closer) {
                                super.removed(closer);
                                // Write whatever's now in the menu back into the persistent stack handler.
                                copyAllSlots(menuHandler, source);
                            }
                        };
                    },
                    Component.translatable("container.simplebackpack.pouch")
            ));
        }
        return InteractionResult.SUCCESS;
    }

    private static void copyAllSlots(ResourceHandler<ItemResource> from, ResourceHandler<ItemResource> to) {
        try (Transaction tx = Transaction.openRoot()) {
            int slots = Math.min(from.size(), to.size());
            for (int i = 0; i < slots; i++) {
                // Clear whatever's currently in the destination slot first.
                ItemResource existing = to.getResource(i);
                int existingAmount = to.getAmountAsInt(i);
                if (existingAmount > 0 && !existing.isEmpty()) {
                    to.extract(i, existing, existingAmount, tx);
                }
                // Copy the source slot's contents in.
                ItemResource resource = from.getResource(i);
                int amount = from.getAmountAsInt(i);
                if (amount > 0 && !resource.isEmpty()) {
                    to.insert(i, resource, amount, tx);
                }
            }
            tx.commit();
        }
    }
}