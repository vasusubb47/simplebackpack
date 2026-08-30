package com.agent47.simple_backpack;

import com.agent47.simple_backpack.item.pouch.PouchMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Simplebackpack.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<PouchMenu>> POUCH_MENU =
            MENUS.register("pouch_menu", () -> new MenuType<>(PouchMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
