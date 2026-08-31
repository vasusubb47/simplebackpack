package com.agent47.simple_backpack.UI;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class AbstractStorageMenu extends AbstractContainerMenu {

    protected final int rows;
    protected final int cols;
    protected final int storageSlotCount;
    protected final ItemStacksResourceHandler storage;

    @Nullable
    private Runnable onClose;

    protected AbstractStorageMenu(MenuType<?> menuType, int containerId, Inventory playerInventory,
                                  ItemStacksResourceHandler storage, int rows, int cols) {
        super(menuType, containerId);
        this.rows = rows;
        this.cols = cols;
        this.storageSlotCount = rows * cols;
        this.storage = storage;

        addStorageSlots();
        int inventoryTop = storageOriginY() + rows * 18 + 13;
        this.addStandardInventorySlots(playerInventory, storageOriginX(), inventoryTop);
    }

    /** Called by the owning item to run cleanup (e.g. writing changes back to the stack) when the menu closes. */
    public void setOnClose(@Nullable Runnable onClose) {
        this.onClose = onClose;
    }

    protected int storageOriginX() { return 8; }
    protected int storageOriginY() { return 18; }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    protected void addStorageSlots() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.addSlot(new ResourceHandlerSlot(
                        storage, storage::set,
                        row * cols + col,
                        storageOriginX() + col * 18,
                        storageOriginY() + row * 18
                ));
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void removed(@NonNull Player closer) {
        super.removed(closer);
        if (onClose != null) {
            onClose.run();
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < storageSlotCount) {
                if (!this.moveItemStackTo(stack, storageSlotCount, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if (!this.moveItemStackTo(stack, 0, storageSlotCount, false)) return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();
            slot.onTake(player, stack);
        }
        return result;
    }
}