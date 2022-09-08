package trapcraft.data;

import static trapcraft.TrapcraftBlocks.*;

import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder.FaceRotation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;
import trapcraft.block.BearTrapBlock;
import trapcraft.block.FanBlock;

import javax.annotation.Nonnull;

public class TrapcraftBlockstateProvider extends BlockStateProvider {

    public TrapcraftBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Constants.MOD_ID, exFileHelper);
    }

    public ExistingFileHelper getExistingHelper() {
        return this.models().existingFileHelper;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Trapcraft Blockstates/Block Models";
    }

    @Override
    protected void registerStatesAndModels() {
        registerOrientable(FAN);
        registerOrientable(IGNITER);

        registerGrassCovering(GRASS_COVERING);
        registerParticleOnly(MAGNETIC_CHEST, () -> Blocks.OAK_PLANKS);
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
            return ConfiguredModel.builder().modelFile(state.getValue(BearTrapBlock.TRIGGERED) ? modelClosed : modelOpen).build();
        }, BearTrapBlock.WATERLOGGED);
    }

    protected void registerGrassCovering(Supplier<? extends Block> blockIn) {
        BlockModelBuilder model = this.models()
                .getBuilder(name(blockIn))
                .parent(this.models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/block")))
                .texture("top", extend(blockTexture(Blocks.GRASS_BLOCK), "_top"))
                .texture("side", extend(blockTexture(Blocks.GRASS_BLOCK), "_top"))
                .texture("overlay", extend(blockTexture(Blocks.GRASS_BLOCK), "_top"))
                .texture("particle", blockTexture(Blocks.DIRT))
                .texture("bottom", blockTexture(Blocks.DIRT));

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
        BlockModelBuilder model = this.models().withExistingParent(name(blockIn), ModelProvider.BLOCK_FOLDER + "/block");

        model.element().allFaces((dir, f) -> {
            if (dir.getAxis() == Axis.Z) {
                f = f.texture(dir == Direction.NORTH ? "#top" : "#bottom");
            } else {
                f = f.texture("#side");
                f = f.rotation(
                        dir == Direction.UP ? FaceRotation.ZERO :
                        dir == Direction.EAST  ? FaceRotation.CLOCKWISE_90 :
                        dir == Direction.DOWN  ? FaceRotation.UPSIDE_DOWN :
                                                 FaceRotation.COUNTERCLOCKWISE_90
                );
            }
            f = f.cullface(dir);
        });
        model.texture("side", extend(blockTexture(blockIn), "_side"));
        model.texture("top", extend(blockTexture(blockIn), "_top"));
        model.texture("bottom", extend(blockTexture(blockIn), "_bottom"));
        model.texture("particle", extend(blockTexture(blockIn), "_side"));

        ModelFile modelVertical = this.models().orientableWithBottom(name(blockIn) + "_vertical",
                extend(blockTexture(blockIn), "_side"),
                extend(blockTexture(blockIn), "_side"),
                extend(blockTexture(blockIn), "_bottom"),
                extend(blockTexture(blockIn), "_top"));

        this.getVariantBuilder(blockIn.get()).forAllStatesExcept(state -> {
            final Direction facing = state.getValue(FanBlock.FACING);
            int xRot = 0;
            int yRot = ((int) facing.toYRot()) + 180;
            final boolean vertical = facing.getAxis().isVertical();

            if (vertical) {
                xRot = facing.getAxisDirection() == AxisDirection.NEGATIVE ? 180 : 0;
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
        final ModelFile model = this.models().getBuilder(name(blockIn))
                .texture("particle", blockTexture(particle));
        this.simpleBlock(blockIn.get(), model);
    }

    private void registerCross(Supplier<? extends Block> block) {
        final ModelFile model = this.models().cross(name(block), blockTexture(block)).renderType("cutout");
        this.simpleBlock(block.get(), model);
    }

    private String name(Supplier<? extends Block> forgeEntry) {
        return ForgeRegistries.BLOCKS.getKey(forgeEntry.get()).getPath();
    }

    private ResourceLocation blockTexture(Supplier<? extends Block> block) {
        final ResourceLocation base = ForgeRegistries.BLOCKS.getKey(block.get());
        return this.prepend(base, ModelProvider.BLOCK_FOLDER + "/");
    }

    private ResourceLocation prepend(final ResourceLocation rl, final String prefix) {
        return new ResourceLocation(rl.getNamespace(), prefix + rl.getPath());
    }

    private String extend(Supplier<? extends Block> forgeEntry, String suffix) {
        return extend(ForgeRegistries.BLOCKS.getKey(forgeEntry.get()), suffix).getPath();
    }

    private ResourceLocation extend(final ResourceLocation rl, final String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }
}
