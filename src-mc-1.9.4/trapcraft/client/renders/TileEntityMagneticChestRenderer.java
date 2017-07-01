package trapcraft.client.renders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;
import trapcraft.tileentity.TileEntityMagneticChest;

@SideOnly(Side.CLIENT)
public class TileEntityMagneticChestRenderer extends TileEntitySpecialRenderer<TileEntityMagneticChest> {

    private ModelChest modelChest = new ModelChest();

    @Override
    public void renderTileEntityAt(TileEntityMagneticChest magneticChest, double x, double y, double z, float partialTicks, int destroyStage) {
    	if(true)
    		return;
    	if(magneticChest != null) {
    		//FMLClientHandler.instance().getClient().getTextureManager().bindTexture(Properties.RES_BLOCK_MAGNETIC_CHEST);
    		 int i = 0;

    	        if (magneticChest.hasWorldObj())
    	        {
    	            i = magneticChest.getBlockMetadata();
    	        }

    	        if (destroyStage >= 0)
    	        {
    	            this.bindTexture(DESTROY_STAGES[destroyStage]);
    	            GlStateManager.matrixMode(5890);
    	            GlStateManager.pushMatrix();
    	            GlStateManager.scale(4.0F, 4.0F, 1.0F);
    	            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
    	            GlStateManager.matrixMode(5888);
    	        }
    	        else
    	        {
    	            this.bindTexture(Properties.RES_BLOCK_MAGNETIC_CHEST);
    	        }

    	        GlStateManager.pushMatrix();
    	        GlStateManager.enableRescaleNormal();
    	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    	        GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
    	        GlStateManager.scale(1.0F, -1.0F, -1.0F);
    	        GlStateManager.translate(0.5F, 0.5F, 0.5F);
    	        int j = 0;

    	        if (i == 2)
    	        {
    	            j = 180;
    	        }

    	        if (i == 3)
    	        {
    	            j = 0;
    	        }

    	        if (i == 4)
    	        {
    	            j = 90;
    	        }

    	        if (i == 5)
    	        {
    	            j = -90;
    	        }

    	        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
    	        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
    	        float f = magneticChest.prevLidAngle + (magneticChest.lidAngle - magneticChest.prevLidAngle) * partialTicks;
    	        f = 1.0F - f;
    	        f = 1.0F - f * f * f;
    	        this.modelChest.chestLid.rotateAngleX = -(f * ((float)Math.PI / 2F));
    	        this.modelChest.renderAll();
    	        GlStateManager.disableRescaleNormal();
    	        GlStateManager.popMatrix();
    	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

    	        if (destroyStage >= 0)
    	        {
    	            GlStateManager.matrixMode(5890);
    	            GlStateManager.popMatrix();
    	            GlStateManager.matrixMode(5888);
    	        }
    	}
    	else {
	    	FMLClientHandler.instance().getClient().getTextureManager().bindTexture(Properties.RES_BLOCK_MAGNETIC_CHEST);
	        GL11.glPushMatrix();
	        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
	        GL11.glScalef(1.0F, -1.0F, -1.0F);
	        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	        short angle = 0;
	
	       // IBlockState state = magneticChest.getBlockMetadata().getWorld().getBlockState(magneticChest.getPos());
	        
	        int meta = magneticChest.getBlockMetadata();
	
	            if (meta == 3) {
	                angle = 180;
	            }
	            else if (meta == 2) {
	                angle = 0;
	            }
	            else if (meta == 5) {
	                angle = 90;
	            }
	            else if (meta == 4) {
	                angle = -90;
	            }
	
	
	        GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
	        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	        float adjustedLidAngle = magneticChest.prevLidAngle + (magneticChest.lidAngle - magneticChest.prevLidAngle) * partialTicks;
	        adjustedLidAngle = 1.0F - adjustedLidAngle;
	        adjustedLidAngle = 1.0F - adjustedLidAngle * adjustedLidAngle * adjustedLidAngle;
	        this.modelChest.chestLid.rotateAngleX = -(adjustedLidAngle * (float) Math.PI / 2.0F);
	        this.modelChest.renderAll();
	        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        GL11.glPopMatrix();
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	}
    }

}