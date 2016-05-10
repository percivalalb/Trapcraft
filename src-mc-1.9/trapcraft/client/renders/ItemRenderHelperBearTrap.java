package trapcraft.client.renders;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public class ItemRenderHelperBearTrap implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) { 
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int metadata = world.getBlockMetadata(x, y, z);
		if(metadata == 0) {
	    	return renderBearTrap(block, x, y, z, renderer);
	    }
		else {
			return renderBearTrapSprung(block, x, y, z, renderer);
		}
	}
	

	public boolean renderBearTrap(Block block, int i, int j, int k, RenderBlocks renderblocks) {
        renderblocks.setRenderBounds(0.85D, 0.0D, 0.0D, 0.9D, 0.05D, 1.0D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.1D, 0.0D, 0.0D, 0.15D, 0.05D, 1.0D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.15D, 0.0D, 0.0D, 0.85D, 0.05D, 0.05D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.15D, 0.0D, 0.95D, 0.85D, 0.05D, 1.0D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.25D, 0.05D, 0.0D, 0.3D, 0.15D, 0.05D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.4D, 0.05D, 0.0D, 0.45D, 0.15D, 0.05D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.55D, 0.05D, 0.0D, 0.6D, 0.15D, 0.05D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.7D, 0.05D, 0.0D, 0.75D, 0.15D, 0.05D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.25D, 0.05D, 0.95D, 0.3D, 0.15D, 1.0D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.4D, 0.05D, 0.95D, 0.45D, 0.15D, 1.0D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.55D, 0.05D, 0.95D, 0.6D, 0.15D, 1.0D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.7D, 0.05D, 0.95D, 0.75D, 0.15D, 1.0D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.15D, 0.0D, 0.4D, 0.85D, 0.03D, 0.6D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.1D, 0.0D, 0.0D, 0.9D, 0.15D, 1.0D);
        renderblocks.clearOverrideBlockTexture();
        return true;
    }

    public boolean renderBearTrapSprung(Block block, int i, int j, int k, RenderBlocks renderblocks) {
        renderblocks.setRenderBounds(0.1D, 0.0D, 0.35D, 0.15D, 0.45D, 0.4D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.1D, 0.0D, 0.6D, 0.15D, 0.45D, 0.65D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.85D, 0.0D, 0.35D, 0.9D, 0.45D, 0.4D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.85D, 0.0D, 0.6D, 0.9D, 0.45D, 0.65D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.85D, 0.0D, 0.4D, 0.9D, 0.05D, 0.6D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.1D, 0.0D, 0.4D, 0.15D, 0.05D, 0.6D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.15D, 0.0D, 0.4D, 0.85D, 0.03D, 0.6D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.15D, 0.4D, 0.35D, 0.85D, 0.45D, 0.4D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.15D, 0.4D, 0.6D, 0.85D, 0.45D, 0.65D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.25D, 0.4D, 0.4D, 0.3D, 0.45D, 0.6D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.4D, 0.4D, 0.4D, 0.45D, 0.45D, 0.6D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.55D, 0.4D, 0.4D, 0.6D, 0.45D, 0.6D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.7D, 0.4D, 0.4D, 0.75D, 0.45D, 0.6D);
        renderblocks.renderStandardBlock(block, i, j, k);
        renderblocks.setRenderBounds(0.1D, 0.0D, 0.35D, 0.9D, 0.45D, 0.65D);
        renderblocks.clearOverrideBlockTexture();
        return true;
    }

	@Override
	public int getRenderId() {
		return 0;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}
}
