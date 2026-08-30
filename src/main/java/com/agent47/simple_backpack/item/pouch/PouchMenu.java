package com.agent47.simple_backpack.item.pouch;

import com.agent47.simple_backpack.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class PouchMenu extends AbstractContainerMenu {
    public static final int ROWS = 1;
    public static final int COLS = 9;
    public static final int POUCH_SLOTS = ROWS * COLS;

    private final ItemStacksResourceHandler pouchInventory;

    public PouchMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new ItemStacksResourceHandler(POUCH_SLOTS));
    }

    public PouchMenu(int containerId, Inventory playerInventory, ItemStacksResourceHandler pouchInventory) {
        super(ModMenus.POUCH_MENU.get(), containerId);
        this.pouchInventory = pouchInventory;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                this.addSlot(new ResourceHandlerSlot(
                        pouchInventory, pouchInventory::set,
                        row * COLS + col,
                        8 + col * 18, 18 + row * 18
                ));
            }
        }

        int inventoryTop = 18 + ROWS * 18 + 13;
        this.addStandardInventorySlots(playerInventory, 8, inventoryTop);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < POUCH_SLOTS) {
                if (!this.moveItemStackTo(stack, POUCH_SLOTS, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if (!this.moveItemStackTo(stack, 0, POUCH_SLOTS, false)) return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();
            slot.onTake(player, stack);
        }
        return result;
    }
}