package trapcraft.data;

import static trapcraft.TrapcraftBlocks.*;

import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;
import trapcraft.api.Constants;
import trapcraft.block.BearTrapBlock;
import trapcraft.block.FanBlock;

public class TrapcraftBlockstateProvider extends BlockStateProvider {

    public TrapcraftBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Constants.MOD_ID, exFileHelper);
    }

    public ExistingFileHelper getExistingHelper() {
        return this.models().existingFileHelper;
    }

    @Override
    public String getName() {
        return "Trapcraft Blockstates/Block Models";
    }

    @Override
    protected void registerStatesAndModels() {
        registerOrientable(FAN);
        registerOrientable(IGNITER);

        registerGrassCovering(GRASS_COVERING);
        registerParticleOnly(MAGNETIC_CHEST, Blocks.OAK_PLANKS.delegate);
        registerCross(SPIKES);
        registerBearTrap(BEAR_TRAP);
    }

    protected void registerBearTrap(Supplier<? extends Block> blockIn) {
        BlockModelBuilder modelClosed = this.models()
                .getBuilder(extend(blockIn, "_closed"))
                .parent(this.models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/block")))
                .texture("particle", blockTexture(blockIn))
                .texture("base", blockTexture(blockIn));

        modelClosed.element().from(1.6F, 0F, 5.6F).to(2.4F, 7.2F, 6.4F).cube("#base");
        modelClosed.element().from(1.6F, 0F, 9.6F).to(2.4F, 7.2F, 10.4F).cube("#base");
        modelClosed.element().from(13.6F, 0F, 5.6F).to(14.4F, 7.2F, 6.4F).cube("#base");
        modelClosed.element().from(13.6F, 0F, 9.6F).to(14.4F, 7.2F, 10.4F).cube("#base");
        modelClosed.element().from(13.6F, 0F, 6.4F).to(14.4F, 0.8F, 9.6F).cube("#base");
        modelClosed.element().from(1.6F, 0F, 6.4F).to(2.4F, 0.8F, 9.6F).cube("#base");
        modelClosed.element().from(2.4F, 0F, 6.4F).to(13.6F, 0.48F, 9.6F).cube("#base");
        modelClosed.element().from(2.4F, 6.4F, 5.6F).to(13.6F, 7.2F, 6.4F).cube("#base");
        modelClosed.element().from(2.4F, 6.4F, 9.6F).to(13.6F, 7.2F, 10.4F).cube("#base");
        modelClosed.element().from(4F, 6.4F, 6.4F).to(4.8F, 7.2F, 9.6F).cube("#base");
        modelClosed.element().from(6.4F, 6.4F, 6.4F).to(7.2F, 7.2F, 9.6F).cube("#base");
        modelClosed.element().from(8.8F, 6.4F, 6.4F).to(9.6F, 7.2F, 9.6F).cube("#base");
        modelClosed.element().from(11.2F, 6.4F, 6.4F).to(12F, 7.2F, 9.6F).cube("#base");

        BlockModelBuilder modelOpen = this.models()
                .getBuilder(extend(blockIn, "_open"))
                .parent(this.models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/block")))
                .texture("particle", blockTexture(blockIn))
                .texture("base", blockTexture(blockIn));

        modelOpen.element().from(13.6F, 0F, 0F).to(14.4F, 0.8F, 16F).cube("#base");
        modelOpen.element().from(1.6F, 0F, 0F).to(2.4F, 0.8F, 16F).cube("#base");
        modelOpen.element().from(2.4F, 0F, 0F).to(13.6F, 0.8F, 0.8F).cube("#base");
        modelOpen.element().from(2.4F, 0F, 0F).to(13.6F, 0.8F, 0.8F).cube("#base");
        modelOpen.element().from(2.4F, 0F, 15.2F).to(13.6F, 0.8F, 16.0F).cube("#base");
        modelOpen.element().from(4F, 0.8F, 0F).to(4.8F, 2.4F, 0.8F).cube("#base");
        modelOpen.element().from(6.4F, 0.8F, 0F).to(7.2F, 2.4F, 0.8F).cube("#base");
        modelOpen.element().from(8.8F, 0.8F, 0F).to(9.6F, 2.4F, 0.8F).cube("#base");
        modelOpen.element().from(11.2F, 0.8F, 0F).to(12F, 2.4F, 0.8F).cube("#base");
        modelOpen.element().from(4F, 0.8F, 15.2F).to(4.8F, 2.4F, 16F).cube("#base");
        modelOpen.element().from(6.4F, 0.8F, 15.2F).to(7.2F, 2.4F, 16F).cube("#base");
        modelOpen.element().from(8.8F, 0.8F, 15.2F).to(9.6F, 2.4F, 16F).cube("#base");
        modelOpen.element().from(11.2F, 0.8F, 15.2F).to(12F, 2.4F, 16F).cube("#base");
        modelOpen.element().from(2.4F, 0F, 6.4F).to(13.6F, 0.48F, 9.6F).cube("#base");

        this.getVariantBuilder(blockIn.get()).forAllStatesExcept(state -> {
            return ConfiguredModel.builder().modelFile(state.get(BearTrapBlock.TRIGGERED) ? modelClosed : modelOpen).build();
        }, BearTrapBlock.WATERLOGGED);
    }

    protected void registerGrassCovering(Supplier<? extends Block> blockIn) {
        BlockModelBuilder model = this.models()
                .getBuilder(name(blockIn))
                .parent(this.models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/block")))
                .texture("top", extend(blockTexture(Blocks.GRASS_BLOCK.delegate), "_top"))
                .texture("side", extend(blockTexture(Blocks.GRASS_BLOCK.delegate), "_top"))
                .texture("overlay", extend(blockTexture(Blocks.GRASS_BLOCK.delegate), "_top"))
                .texture("particle", blockTexture(Blocks.DIRT.delegate))
                .texture("bottom", blockTexture(Blocks.DIRT.delegate));

        model.element()
          .from(0, 15, 0)
          .to(16, 16, 16)
          .allFaces((d, b) -> b
                  .texture(d.getAxis().isHorizontal() ? "#side" : d == Direction.UP ? "#top" : "#bottom")
                  .cullface(d)
                  .tintindex(d == Direction.UP ? 0 : -1));

        BlockModelBuilder.ElementBuilder overlayBuilder = model.element();
        Arrays.stream(Direction.values())
          .filter(d -> d.getAxis().isHorizontal())
          .forEach(overlayBuilder::face);

        overlayBuilder
          .from(0, 15, 0)
          .to(16, 16, 16)
          .faces((d, b) -> b
                  .texture("#overlay")
                  .cullface(d)
                  .tintindex(0));

        this.simpleBlock(blockIn.get(), model);
    }

    protected void registerOrientable(Supplier<? extends Block> blockIn) {
        ModelFile model = this.models().orientable(name(blockIn),
                extend(blockTexture(blockIn), "_side"),
                extend(blockTexture(blockIn), "_top"),
                extend(blockTexture(blockIn), "_side"));

        ModelFile modelVertical = this.models().orientableVertical(name(blockIn) + "_vertical",
                extend(blockTexture(blockIn), "_side"),
                extend(blockTexture(blockIn), "_top"));

        this.getVariantBuilder(blockIn.get()).forAllStatesExcept(state -> {
            int xRot = 0;
            int yRot = ((int) state.get(FanBlock.FACING).getHorizontalAngle()) + 180;
            boolean vertical = state.get(FanBlock.FACING).getAxis().isVertical();

            if (vertical) {
                xRot = state.get(FanBlock.FACING).getAxisDirection() == AxisDirection.NEGATIVE ? 180 : 0;
                yRot = 0;
            }
            yRot %= 360;
            return ConfiguredModel.builder().modelFile(vertical ? modelVertical : model)
                    .rotationX(xRot)
                    .rotationY(yRot)
                    .build();
        }, FanBlock.POWERED);
    }

    private void registerParticleOnly(Supplier<? extends Block> blockIn, Supplier<? extends Block> particle) {
        ModelFile model = this.models().getBuilder(name(blockIn))
                .texture("particle", blockTexture(particle));
        this.simpleBlock(blockIn.get(), model);
    }

    private void registerCross(Supplier<? extends Block> block) {
        ModelFile model = this.models().cross(name(block), blockTexture(block));
        this.simpleBlock(block.get(), model);
    }

    private String name(Supplier<? extends IForgeRegistryEntry<?>> forgeEntry) {
        return forgeEntry.get().getRegistryName().getPath();
    }

    private ResourceLocation blockTexture(Supplier<? extends Block> block) {
        ResourceLocation base = block.get().getRegistryName();
        return this.prepend(base, ModelProvider.BLOCK_FOLDER + "/");
    }

    private ResourceLocation prepend(ResourceLocation rl, String prefix) {
        return new ResourceLocation(rl.getNamespace(), prefix + rl.getPath());
    }

    private String extend(Supplier<? extends IForgeRegistryEntry<?>> forgeEntry, String suffix) {
        return extend(forgeEntry.get().getRegistryName(), suffix).getPath();
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }
}