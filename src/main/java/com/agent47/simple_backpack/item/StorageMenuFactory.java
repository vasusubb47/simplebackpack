package com.agent47.simple_backpack.item;

import com.agent47.simple_backpack.UI.AbstractStorageMenu;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

@FunctionalInterface
public interface StorageMenuFactory<M extends AbstractStorageMenu> {
    M create(int containerId, Inventory playerInventory, ItemStacksResourceHandler menuHandler);
}
