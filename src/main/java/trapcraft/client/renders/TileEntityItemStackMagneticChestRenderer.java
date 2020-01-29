package trapcraft.client.renders;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import trapcraft.TrapcraftBlocks;
import trapcraft.tileentity.TileEntityMagneticChest;

public class TileEntityItemStackMagneticChestRenderer extends ItemStackTileEntityRenderer {

	private final TileEntityMagneticChest chestBasic = new TileEntityMagneticChest();

	@Override
	public void renderByItem(ItemStack itemStackIn) {
		Item item = itemStackIn.getItem();

		if (item == TrapcraftBlocks.MAGNETIC_CHEST_ITEM.get()) {
			 TileEntityRendererDispatcher.instance.renderAsItem(this.chestBasic);
		}
	}
}
