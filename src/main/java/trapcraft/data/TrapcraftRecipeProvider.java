package trapcraft.data;

import java.nio.file.Path;
import java.util.function.Consumer;

import com.google.gson.JsonObject;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import trapcraft.TrapcraftBlocks;
import trapcraft.TrapcraftItems;
import trapcraft.api.Constants;

public class TrapcraftRecipeProvider extends RecipeProvider {

    public TrapcraftRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject advancementJson, Path pathIn) {

    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        ShapedRecipeBuilder.shapedRecipe(TrapcraftBlocks.BEAR_TRAP.get(), 1)
            .patternLine("XYX")
            .patternLine("YYY")
            .key('X', Items.IRON_INGOT)
            .key('Y', Blocks.STONE_PRESSURE_PLATE)
            .addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT))
            .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TrapcraftBlocks.FAN.get(), 1)
            .patternLine("XXX")
            .patternLine("XYX")
            .patternLine("XXX")
            .key('X', Blocks.COBBLESTONE)
            .key('Y', Items.IRON_INGOT)
            .addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT))
            .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TrapcraftBlocks.GRASS_COVERING.get(), 1)
            .patternLine("XXX")
            .patternLine("YYY")
            .key('X', Blocks.TALL_GRASS)
            .key('Y', Items.STICK)
            .addCriterion("has_tall_grass", this.hasItem(Blocks.TALL_GRASS))
            .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TrapcraftItems.IGNITER_RANGE.get(), 1)
            .patternLine("ALA")
            .patternLine("DRD")
            .patternLine("DRD")
            .key('A', Items.ARROW)
            .key('R', Items.REDSTONE)
            .key('L', Items.LEATHER)
            .key('D', Items.LAPIS_LAZULI)
            .addCriterion("has_netherrack", this.hasItem(Blocks.NETHERRACK))
            .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TrapcraftBlocks.IGNITER.get(), 1)
            .patternLine("NNN")
            .patternLine("CRC")
            .patternLine("CCC")
            .key('N', Blocks.NETHERRACK)
            .key('R', Items.REDSTONE)
            .key('C', Blocks.COBBLESTONE)
            .addCriterion("has_netherrack", this.hasItem(Blocks.NETHERRACK))
            .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TrapcraftBlocks.MAGNETIC_CHEST.get(), 1)
            .patternLine("PPP")
            .patternLine("PRP")
            .patternLine("PIP")
            .key('P', ItemTags.PLANKS)
            .key('R', Items.REDSTONE)
            .key('I', Items.IRON_INGOT)
            .addCriterion("has_redstone", this.hasItem(Items.REDSTONE))
            .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(Items.PLAYER_HEAD)
            .addIngredient(Items.BLUE_DYE, 1)
            .addIngredient(Blocks.BROWN_WOOL, 1)
            .addCriterion("has_brown_wool", this.hasItem(Blocks.BROWN_WOOL))
            .build(consumer, new ResourceLocation(Constants.MOD_ID, "player_head"));

        ShapedRecipeBuilder.shapedRecipe(TrapcraftBlocks.SPIKES.get(), 1)
            .patternLine(" I ")
            .patternLine(" I ")
            .patternLine("III")
            .key('I', Items.IRON_INGOT)
            .addCriterion("has_iron_ingot", this.hasItem(Items.IRON_INGOT))
            .build(consumer);
    }
}
