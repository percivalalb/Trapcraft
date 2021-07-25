package trapcraft.data;

import java.nio.file.Path;
import java.util.function.Consumer;

import com.google.gson.JsonObject;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import trapcraft.TrapcraftBlocks;
import trapcraft.TrapcraftItems;
import trapcraft.api.Constants;

import javax.annotation.Nonnull;

public class TrapcraftRecipeProvider extends RecipeProvider {

    public TrapcraftRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Trapcraft Recipes";
    }

    @Override
    protected void saveAdvancement(HashCache cache, JsonObject advancementJson, Path pathIn) {

    }

    @Override
    protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(TrapcraftBlocks.BEAR_TRAP.get(), 1)
            .pattern("XYX")
            .pattern("YYY")
            .define('X', Items.IRON_INGOT)
            .define('Y', Blocks.STONE_PRESSURE_PLATE)
            .unlockedBy("has_iron_ingot", this.has(Items.IRON_INGOT))
            .save(consumer);

        ShapedRecipeBuilder.shaped(TrapcraftBlocks.FAN.get(), 1)
            .pattern("XXX")
            .pattern("XYX")
            .pattern("XXX")
            .define('X', Blocks.COBBLESTONE)
            .define('Y', Items.IRON_INGOT)
            .unlockedBy("has_iron_ingot", this.has(Items.IRON_INGOT))
            .save(consumer);

        ShapedRecipeBuilder.shaped(TrapcraftBlocks.GRASS_COVERING.get(), 1)
            .pattern("XXX")
            .pattern("YYY")
            .define('X', Blocks.TALL_GRASS)
            .define('Y', Items.STICK)
            .unlockedBy("has_tall_grass", this.has(Blocks.TALL_GRASS))
            .save(consumer);

        ShapedRecipeBuilder.shaped(TrapcraftItems.IGNITER_RANGE.get(), 1)
            .pattern("ALA")
            .pattern("DRD")
            .pattern("DRD")
            .define('A', Items.ARROW)
            .define('R', Items.REDSTONE)
            .define('L', Items.LEATHER)
            .define('D', Items.LAPIS_LAZULI)
            .unlockedBy("has_netherrack", this.has(Blocks.NETHERRACK))
            .save(consumer);

        ShapedRecipeBuilder.shaped(TrapcraftBlocks.IGNITER.get(), 1)
            .pattern("NNN")
            .pattern("CRC")
            .pattern("CCC")
            .define('N', Blocks.NETHERRACK)
            .define('R', Items.REDSTONE)
            .define('C', Blocks.COBBLESTONE)
            .unlockedBy("has_netherrack", this.has(Blocks.NETHERRACK))
            .save(consumer);

        ShapedRecipeBuilder.shaped(TrapcraftBlocks.MAGNETIC_CHEST.get(), 1)
            .pattern("PPP")
            .pattern("PRP")
            .pattern("PIP")
            .define('P', ItemTags.PLANKS)
            .define('R', Items.REDSTONE)
            .define('I', Items.IRON_INGOT)
            .unlockedBy("has_redstone", this.has(Items.REDSTONE))
            .save(consumer);

        ShapelessRecipeBuilder.shapeless(Items.PLAYER_HEAD)
            .requires(Items.BLUE_DYE, 1)
            .requires(Blocks.BROWN_WOOL, 1)
            .unlockedBy("has_brown_wool", this.has(Blocks.BROWN_WOOL))
            .save(consumer, new ResourceLocation(Constants.MOD_ID, "player_head"));

        ShapedRecipeBuilder.shaped(TrapcraftBlocks.SPIKES.get(), 1)
            .pattern(" I ")
            .pattern(" I ")
            .pattern("III")
            .define('I', Items.IRON_INGOT)
            .unlockedBy("has_iron_ingot", this.has(Items.IRON_INGOT))
            .save(consumer);
    }
}
