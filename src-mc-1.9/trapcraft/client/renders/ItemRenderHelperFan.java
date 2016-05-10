package trapcraft.client.renders;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class ItemRenderHelperFan implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,RenderBlocks renderer) {
	    renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	    this.renderInvBlock(block, metadata, modelID, renderer);
        renderer.clearOverrideBlockTexture();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
	    renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	    renderer.renderStandardBlock(block, x, y, z);
        renderer.clearOverrideBlockTexture();
        return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return 0;
	}
	
	public void renderInvBlock(Block block, int metadata, int modelID, net.minecraft.client.renderer.RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, -1.0F, 0.0F);
	    renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, 1.0F);
	    renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, 0));
	    tessellator.draw();
	    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}   
}
