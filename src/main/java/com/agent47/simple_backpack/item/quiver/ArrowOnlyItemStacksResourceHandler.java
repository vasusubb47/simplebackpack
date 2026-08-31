package com.agent47.simple_backpack.item.quiver;

import net.minecraft.world.item.ArrowItem;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class ArrowOnlyItemStacksResourceHandler extends ItemStacksResourceHandler {
    public ArrowOnlyItemStacksResourceHandler(int size) {
        super(size);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return resource.test(stack -> stack.getItem() instanceof ArrowItem) && super.isValid(index, resource);
    }
}
