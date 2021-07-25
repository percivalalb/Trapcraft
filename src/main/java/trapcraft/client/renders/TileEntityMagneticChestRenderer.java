package trapcraft.client.renders;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trapcraft.api.Constants;
import trapcraft.block.tileentity.MagneticChestTileEntity;

public class TileEntityMagneticChestRenderer extends ChestRenderer<MagneticChestTileEntity> {

    public static final Material CHEST_TRAPPED_MATERIAL = Util.make(() -> {
        return new Material(Sheets.CHEST_SHEET, Constants.RES_BLOCK_MAGNETIC_CHEST);
    });

    public TileEntityMagneticChestRenderer(BlockEntityRenderDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    protected Material getMaterial(MagneticChestTileEntity tileEntity, ChestType chestType) {
        return CHEST_TRAPPED_MATERIAL;
    }
}