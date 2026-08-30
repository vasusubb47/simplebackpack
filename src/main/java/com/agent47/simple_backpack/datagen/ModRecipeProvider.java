package com.agent47.simple_backpack.datagen;

import com.agent47.simple_backpack.Simplebackpack;
import com.agent47.simple_backpack.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    public static class Runner extends RecipeProvider.Runner{
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "Simple Backpack recipies";
        }
    }

    @Override
    protected void buildRecipes() {
        shapeless(RecipeCategory.MISC, ModItems.WRAPPER.get(), 1)
                .requires(Items.LEATHER)
                .requires(Items.HONEYCOMB)
                .requires(ItemTags.WOOL)
                .requires(Items.STRING)
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .group("wrapper")
                .save(output);
    }
}
