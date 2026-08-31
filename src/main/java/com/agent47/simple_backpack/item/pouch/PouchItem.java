package com.agent47.simple_backpack.item.pouch;

import com.agent47.simple_backpack.ModDataComponents;
import com.agent47.simple_backpack.item.AbstractStorageItem;

public class PouchItem extends AbstractStorageItem<PouchMenu> {
    public static final int SLOTS = 9;

    public PouchItem(Properties properties) {
        super(properties, SLOTS, ModDataComponents.POUCH_CONTENTS, PouchMenu::new);
    }
}
