package trapcraft.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import trapcraft.TrapcraftBlocks;
import trapcraft.TrapcraftItems;
import trapcraft.api.Constants;

import java.util.function.Consumer;

public class TrapcraftRecipeProvider extends RecipeProvider {

    public TrapcraftRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TrapcraftBlocks.BEAR_TRAP.get(), 1)
            .pattern("XYX")
            .pattern("YYY")
            .define('X', Items.IRON_INGOT)
            .define('Y', Blocks.STONE_PRESSURE_PLATE)
            .unlockedBy("has_iron_ingot", this.has(Items.IRON_INGOT))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TrapcraftBlocks.FAN.get(), 1)
            .pattern("XXX")
            .pattern("XYX")
            .pattern("XXX")
            .define('X', Blocks.COBBLESTONE)
            .define('Y', Items.IRON_INGOT)
            .unlockedBy("has_iron_ingot", this.has(Items.IRON_INGOT))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TrapcraftBlocks.GRASS_COVERING.get(), 1)
            .pattern("XXX")
            .pattern("YYY")
            .define('X', Blocks.TALL_GRASS)
            .define('Y', Items.STICK)
            .unlockedBy("has_tall_grass", this.has(Blocks.TALL_GRASS))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TrapcraftItems.IGNITER_RANGE.get(), 1)
            .pattern("ALA")
            .pattern("DRD")
            .pattern("DRD")
            .define('A', Items.ARROW)
            .define('R', Items.REDSTONE)
            .define('L', Items.LEATHER)
            .define('D', Items.LAPIS_LAZULI)
            .unlockedBy("has_netherrack", this.has(Blocks.NETHERRACK))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TrapcraftBlocks.IGNITER.get(), 1)
            .pattern("NNN")
            .pattern("CRC")
            .pattern("CCC")
            .define('N', Blocks.NETHERRACK)
            .define('R', Items.REDSTONE)
            .define('C', Blocks.COBBLESTONE)
            .unlockedBy("has_netherrack", this.has(Blocks.NETHERRACK))
            .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TrapcraftBlocks.MAGNETIC_CHEST.get(), 1)
            .pattern("PPP")
            .pattern("PRP")
            .pattern("PIP")
            .define('P', ItemTags.PLANKS)
            .define('R', Items.REDSTONE)
            .define('I', Items.IRON_INGOT)
            .unlockedBy("has_redstone", this.has(Items.REDSTONE))
            .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, Items.PLAYER_HEAD)
            .requires(Items.BLUE_DYE, 1)
            .requires(Blocks.BROWN_WOOL, 1)
            .unlockedBy("has_brown_wool", this.has(Blocks.BROWN_WOOL))
            .save(consumer, new ResourceLocation(Constants.MOD_ID, "player_head"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TrapcraftBlocks.SPIKES.get(), 1)
            .pattern(" I ")
            .pattern(" I ")
            .pattern("III")
            .define('I', Items.IRON_INGOT)
            .unlockedBy("has_iron_ingot", this.has(Items.IRON_INGOT))
            .save(consumer);
    }
}
