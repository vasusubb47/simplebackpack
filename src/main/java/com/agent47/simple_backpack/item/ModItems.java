package com.agent47.simple_backpack.item;

import com.agent47.simple_backpack.Simplebackpack;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Simplebackpack.MOD_ID);

    public static final DeferredItem<Item> WRAPPER =ITEMS.registerSimpleItem("wrapper");

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
