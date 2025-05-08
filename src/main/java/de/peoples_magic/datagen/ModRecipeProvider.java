package de.peoples_magic.datagen;

import de.peoples_magic.PeoplesMagicMod;
import de.peoples_magic.item.ModItems;
import de.peoples_magic.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {


    protected ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
            super(output, provider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }


        @Override
        public String getName() {
            return "Peoples Magic Recipes";
        }
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, ModBlocks.MANA_POOL.get())
                .pattern("   ")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', Items.HEART_OF_THE_SEA)
                .define('B', Items.POLISHED_DEEPSLATE_SLAB)
                .unlockedBy("has_book_of_magic", has(ModItems.BOOK_OF_MAGIC)).save(this.output);

        shapeless(RecipeCategory.MISC, ModItems.SUSPICIOUS_TOME.get())
                .requires(Items.LEATHER, 1)
                .requires(Items.PAPER, 1)
                .requires(Items.LAPIS_LAZULI, 1)
                .unlockedBy("has_book_of_magic", has(ModItems.BOOK_OF_MAGIC)).save(this.output);

        shapeless(RecipeCategory.MISC, ModItems.BOOK_OF_MAGIC.get())
                .requires(Items.LEATHER, 1)
                .requires(Items.PAPER, 1)
                .requires(Items.RED_MUSHROOM, 1)
                .unlockedBy("has_book_of_magic", has(ModItems.BOOK_OF_MAGIC)).save(this.output);

//        shaped(RecipeCategory.MISC, ModItems.SUSPICIOUS_TOME.get())
//                .pattern(" BB")
//                .pattern(" AB")
//                .pattern(" BB")
//                .define('A', Items.HEART_OF_THE_SEA)
//                .define('B', Items.POLISHED_DEEPSLATE_SLAB)
//                .unlockedBy("has_book_of_magic", has(ModItems.BOOK_OF_MAGIC)).save(this.output);
    }


}
