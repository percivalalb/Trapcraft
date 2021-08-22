package trapcraft.handler;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import trapcraft.TrapcraftEntityTypes;
import trapcraft.TrapcraftTileEntityTypes;
import trapcraft.api.Constants;
import trapcraft.client.renders.DummyRenderer;
import trapcraft.client.renders.TileEntityMagneticChestRenderer;

public class ModelHandler {

    public static final ModelLayerLocation DUMMY = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "dummy"), "main");
    public static final ModelLayerLocation MAGNETIC_CHEST = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "magnetic_chest"), "main");


    @SubscribeEvent
    public void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DUMMY, () -> LayerDefinition.create(PlayerModel.createMesh(CubeDeformation.NONE, false), 64, 32));
        event.registerLayerDefinition(MAGNETIC_CHEST, TileEntityMagneticChestRenderer::createSingleBodyLayer);
    }

    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TrapcraftEntityTypes.DUMMY.get(), DummyRenderer::new);
        event.registerBlockEntityRenderer(TrapcraftTileEntityTypes.MAGNETIC_CHEST.get(), TileEntityMagneticChestRenderer::new);
    }

    @SubscribeEvent
    public void registerTileEntityRenders(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre event) {
        if (!event.getMap().location().equals(Sheets.CHEST_SHEET)) {
            return;
        }

        event.addSprite(Constants.RES_BLOCK_MAGNETIC_CHEST);
    }
}
