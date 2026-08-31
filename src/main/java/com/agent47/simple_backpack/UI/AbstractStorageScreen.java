package com.agent47.simple_backpack.UI;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jspecify.annotations.NonNull;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractStorageScreen<T extends AbstractStorageMenu> extends AbstractContainerScreen<T> {

    private static final Identifier BACKGROUND =
            Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");

    protected AbstractStorageScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 114 + menu.getRows() * 18);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int topHeight = this.menu.getRows() * 18 + 17;
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, topHeight, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos + topHeight, 0.0F, 126.0F, this.imageWidth, 96, 256, 256);
    }
}
