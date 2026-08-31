package com.agent47.simple_backpack.item.quiver;

import com.agent47.simple_backpack.UI.AbstractStorageScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuiverScreen extends AbstractStorageScreen<QuiverMenu> {
    public QuiverScreen(QuiverMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
}
