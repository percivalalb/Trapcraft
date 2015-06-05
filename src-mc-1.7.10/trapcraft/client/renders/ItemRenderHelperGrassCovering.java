package trapcraft.client.renders;

import java.util.logging.Level;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import trapcraft.TrapcraftMod;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class ItemRenderHelperGrassCovering implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) { 
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
	    renderer.setRenderBounds(0.0D, 0.99D, 0.0D, 1.0D, 1.0D, 1.0D);
	    renderer.renderStandardBlock(block, x, y, z);
	    renderer.setOverrideBlockTexture(renderer.getBlockIconFromSideAndMetadata(Blocks.planks, 1, 0));
	    if(world.getBlock(x - 1, y, z) == TrapcraftMod.grassCovering) {
	    	renderer.setRenderBounds(-0.5D, 0.89D, 0.2D, 0.5D, 0.99D, 0.4D);
	    	renderer.renderStandardBlock(block, x, y, z);
	    	renderer.setRenderBounds(-0.5D, 0.89D, 0.6D, 0.5D, 0.99D, 0.8D);
	    	renderer.renderStandardBlock(block, x, y, z);
	    }
	    if(world.getBlock(x + 1, y, z) == TrapcraftMod.grassCovering) {
	    	renderer.setRenderBounds(0.5D, 0.89D, 0.2D, 1.5D, 0.99D, 0.4D);
	    	renderer.renderStandardBlock(block, x, y, z);
	    	renderer.setRenderBounds(0.5D, 0.89D, 0.6D, 1.5D, 0.99D, 0.8D);
	    	renderer.renderStandardBlock(block, x, y, z);
	    }
	    
	    if(world.getBlock(x, y, z - 1) == TrapcraftMod.grassCovering) {
	    	renderer.setRenderBounds(0.2D, 0.891D, -0.5D, 0.4D, 0.99D, 0.5D);
	    	renderer.renderStandardBlock(block, x, y, z);
	    	renderer.setRenderBounds(0.6D, 0.891D, -0.5D, 0.8D, 0.99D, 0.5D);
	    	renderer.renderStandardBlock(block, x, y, z);
	    }
	    if(world.getBlock(x, y, z + 1) == TrapcraftMod.grassCovering) {
	    	renderer.setRenderBounds(0.2D, 0.891D, 0.5D, 0.4D, 0.99D, 1.5D);
	    	renderer.renderStandardBlock(block, x, y, z);
	    	renderer.setRenderBounds(0.6D, 0.891D, 0.5D, 0.8D, 0.99D, 1.5D);
	    	renderer.renderStandardBlock(block, x, y, z);
	    }
	    
	    
        renderer.clearOverrideBlockTexture();
        return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return 0;
	}
}
