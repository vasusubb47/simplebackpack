package com.agent47.simple_backpack.item.quiver;

import com.agent47.simple_backpack.ModMenus;
import com.agent47.simple_backpack.UI.AbstractStorageMenu;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class QuiverMenu extends AbstractStorageMenu {
    public static final int ROWS = 1;
    public static final int COLS = 9;

    public QuiverMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new ItemStacksResourceHandler(ROWS * COLS));
    }

    public QuiverMenu(int containerId, Inventory playerInventory, ItemStacksResourceHandler quiverInventory) {
        super(ModMenus.QUIVER_MENU.get(), containerId, playerInventory, quiverInventory, ROWS, COLS);
    }
}
