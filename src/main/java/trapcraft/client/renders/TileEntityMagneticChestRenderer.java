package trapcraft.client.renders;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trapcraft.api.Properties;
import trapcraft.tileentity.TileEntityMagneticChest;

@SideOnly(Side.CLIENT)
public class TileEntityMagneticChestRenderer extends TileEntitySpecialRenderer<TileEntityMagneticChest> {

    private ModelChest modelChest = new ModelChest();

    @Override
    public void render(TileEntityMagneticChest magneticChest, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    	if(magneticChest != null) {
    		int i = 0;

    		if(magneticChest.hasWorld())
    	    {
    			i = magneticChest.getBlockMetadata();
    	    }

    		if(destroyStage >= 0) {
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
    	        j = 0;
    	    }
   	        if (i == 3)
   	        {
   	            j = 180;
   	        }
   	        if (i == 4)
   	        {
   	            j = -90;
   	        }
   	        if (i == 5)
   	        {
   	            j = 90;
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
    		int i = 4;

    		if(destroyStage >= 0) {
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
    	        j = 0;
    	    }
   	        if (i == 3)
   	        {
   	            j = 180;
   	        }
   	        if (i == 4)
   	        {
   	            j = -90;
   	        }
   	        if (i == 5)
   	        {
   	            j = 90;
   	        }

   	        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
   	        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
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
    }

}
