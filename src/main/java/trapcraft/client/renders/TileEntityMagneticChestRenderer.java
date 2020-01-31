package trapcraft.client.renders;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trapcraft.api.Constants;
import trapcraft.tileentity.TileEntityMagneticChest;

@OnlyIn(Dist.CLIENT)
public class TileEntityMagneticChestRenderer extends ChestTileEntityRenderer<TileEntityMagneticChest> {

    public static final Material CHEST_TRAPPED_MATERIAL = Util.make(() -> {
        return new Material(Atlases.CHEST_ATLAS, Constants.RES_BLOCK_MAGNETIC_CHEST);
    });

    public TileEntityMagneticChestRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    protected Material getMaterial(TileEntityMagneticChest tileEntity, ChestType chestType) {
        return CHEST_TRAPPED_MATERIAL;
    }
}