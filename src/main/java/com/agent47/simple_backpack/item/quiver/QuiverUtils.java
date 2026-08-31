package com.agent47.simple_backpack.item.quiver;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;

public final class QuiverUtils {
    private QuiverUtils() {}

    public static boolean isArrow(ItemStack stack) {
        return stack.getItem() instanceof ArrowItem;
    }

    /** Finds the first quiver stack in the player's main inventory. */
    public static ItemStack findQuiverStack(Player player) {
        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (stack.getItem() instanceof QuiverItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
