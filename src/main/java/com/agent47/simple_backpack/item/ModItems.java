package com.agent47.simple_backpack.item;

import com.agent47.simple_backpack.ModDataComponents;
import com.agent47.simple_backpack.Simplebackpack;
import com.agent47.simple_backpack.item.pouch.PouchItem;
import com.agent47.simple_backpack.item.quiver.QuiverItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Simplebackpack.MOD_ID);

    public static final DeferredItem<Item> WRAPPER =ITEMS.registerSimpleItem("wrapper");

//    public static final DeferredItem<Item> POUCH = ITEMS.register("pouch", () ->
//            new PouchItem(new Item.Properties()
//                    .stacksTo(1)
//                    .component(ModDataComponents.POUCH_CONTENTS.get(), ItemContainerContents.EMPTY)));

    private static Identifier ResourceLocation;
    public static final DeferredItem<Item> POUCH = ITEMS.register("pouch", () ->
            new PouchItem(new Item.Properties()
                    .stacksTo(1)
                    .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Simplebackpack.MOD_ID, "pouch")))));

    public static final DeferredItem<Item> QUIVER = ITEMS.register("quiver", () ->
            new QuiverItem(new Item.Properties()
                    .stacksTo(1)
                    .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Simplebackpack.MOD_ID, "quiver")))));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
