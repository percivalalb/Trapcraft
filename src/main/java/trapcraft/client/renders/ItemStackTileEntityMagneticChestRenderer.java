package trapcraft.client.renders;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import trapcraft.TrapcraftBlocks;
import trapcraft.block.tileentity.MagneticChestTileEntity;

public class ItemStackTileEntityMagneticChestRenderer extends ItemStackTileEntityRenderer {

	private static MagneticChestTileEntity chestBasic;

	@Override
	public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Item item = itemStackIn.getItem();

		if (item == TrapcraftBlocks.MAGNETIC_CHEST_ITEM.get()) {
		    TileEntityRendererDispatcher.instance.renderItem(chestBasic, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		}
	}

	public static void setDummyTE() {
	    ItemStackTileEntityMagneticChestRenderer.chestBasic = new MagneticChestTileEntity();
	}
}
