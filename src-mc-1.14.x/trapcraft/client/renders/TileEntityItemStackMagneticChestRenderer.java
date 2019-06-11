package trapcraft.client.renders;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import trapcraft.ModBlocks;
import trapcraft.tileentity.TileEntityMagneticChest;

public class TileEntityItemStackMagneticChestRenderer extends ItemStackTileEntityRenderer {

	private final TileEntityMagneticChest chestBasic = new TileEntityMagneticChest();
	
	@Override
	public void renderByItem(ItemStack itemStackIn) {
		Item item = itemStackIn.getItem();

		if (item == ModBlocks.MAGNETIC_CHEST.asItem()) {
			 TileEntityRendererDispatcher.instance.renderAsItem(this.chestBasic);
		}
	}
}
