package com.agent47.simple_backpack.item;

import com.agent47.simple_backpack.UI.AbstractStorageMenu;
import com.agent47.simple_backpack.util.ResourceHandlerUtils;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public abstract class AbstractStorageItem<M extends AbstractStorageMenu> extends Item {

    protected final int slots;
    private final Supplier<DataComponentType<ItemContainerContents>> dataComponent;
    private final StorageMenuFactory<M> menuFactory;

    protected AbstractStorageItem(Properties properties, int slots,
                                  Supplier<DataComponentType<ItemContainerContents>> dataComponent,
                                  StorageMenuFactory<M> menuFactory) {
        super(properties);
        this.slots = slots;
        this.dataComponent = dataComponent;
        this.menuFactory = menuFactory;
    }

    protected ItemStacksResourceHandler createEmptyHandler(int slots) {
        return new ItemStacksResourceHandler(slots);
    }

    @Override
    public @NonNull InteractionResult use(Level level, Player player, @NonNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, playerInv, p) -> {
                        var source = new ItemAccessItemHandler(
                                ItemAccess.forStack(stack), dataComponent.get(), slots);

                        var menuHandler = createEmptyHandler(slots);
                        ResourceHandlerUtils.copyAllSlots(source, menuHandler);

                        M menu = menuFactory.create(containerId, playerInv, menuHandler);
                        menu.setOnClose(() -> ResourceHandlerUtils.copyAllSlots(menuHandler, source));
                        return menu;
                    },
                    stack.getHoverName()
            ));
        }
        return InteractionResult.SUCCESS;
    }
}