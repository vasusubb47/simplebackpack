package com.agent47.simple_backpack.item.quiver;

import com.agent47.simple_backpack.ModDataComponents;
import com.agent47.simple_backpack.item.AbstractStorageItem;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class QuiverItem extends AbstractStorageItem<QuiverMenu> {
    public static final int SLOTS = 9;

    public QuiverItem(Properties properties) {
        super(properties, SLOTS, ModDataComponents.QUIVER_CONTENTS, QuiverMenu::new);
    }

    @Override
    protected ItemStacksResourceHandler createEmptyHandler(int slots) {
        return new ArrowOnlyItemStacksResourceHandler(slots);
    }
}
