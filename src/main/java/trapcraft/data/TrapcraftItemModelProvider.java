package trapcraft.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import static trapcraft.TrapcraftBlocks.*;
import static trapcraft.TrapcraftItems.IGNITER_RANGE;

public class TrapcraftItemModelProvider extends ItemModelProvider {

    public TrapcraftItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }
    @Nonnull
    @Override
    public String getName() {
        return "Trapcraft Item Models";
    }

    @Override
    protected void registerModels() {
        this.generated(IGNITER_RANGE, "_upgrade");

        this.blockSprite(SPIKES);

        this.blockItem(FAN);
        this.blockItem(IGNITER);
        this.blockItem(BEAR_TRAP, "_open");
        this.blockItem(GRASS_COVERING);
        this.chest(MAGNETIC_CHEST, () -> Blocks.OAK_PLANKS);
    }

    private ResourceLocation itemTexture(Supplier<? extends ItemLike> item) {
        return modLoc(ModelProvider.ITEM_FOLDER + "/" + name(item));
    }

    private ResourceLocation blockTexture(Supplier<? extends Block> block) {
        ResourceLocation base = ForgeRegistries.BLOCKS.getKey(block.get());
        return new ResourceLocation(base.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + base.getPath());
    }

    private String name(Supplier<? extends ItemLike> item) {
        return ForgeRegistries.ITEMS.getKey(item.get().asItem()).getPath();
    }

    private ItemModelBuilder blockSprite(Supplier<? extends Block> block) {
        return generated(block, blockTexture(block));
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block) {
        return blockItem(block, "");
    }

    private ItemModelBuilder generated(Supplier<? extends ItemLike> item) {
        return generated(item, itemTexture(item));
    }

    private ItemModelBuilder generated(Supplier<? extends ItemLike> item, String suffix) {
        return generated(item, extend(itemTexture(item), suffix));
    }

    private ItemModelBuilder generated(Supplier<? extends ItemLike> item, ResourceLocation texture) {
        return getBuilder(name(item)).parent(new UncheckedModelFile(ModelProvider.ITEM_FOLDER + "/generated")).texture("layer0", texture);
    }

    private ItemModelBuilder chest(Supplier<? extends ItemLike> item, Supplier<? extends Block> particle) {
        return chest(item, blockTexture(particle));
    }

    private ItemModelBuilder chest(Supplier<? extends ItemLike> item, ResourceLocation texture) {
        return getBuilder(name(item)).parent(new UncheckedModelFile(ModelProvider.ITEM_FOLDER + "/chest")).texture("particle", texture);
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block, String suffix) {
        return withExistingParent(name(block), modLoc(ModelProvider.BLOCK_FOLDER + "/" + name(block) + suffix));
    }

    private ResourceLocation extend(final ResourceLocation rl, final String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }
}
