package trapcraft.client.renders;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import trapcraft.entity.EntityDummy;

/**
 * @author ProPercivalalb
 */
public class RenderDummy extends RenderLiving<EntityDummy> {
	
	private float scale = 1F;
	private ResourceLocation resource;

    public RenderDummy(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn, ResourceLocation resource) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.resource = resource;
    }
   
    @Override
    public void doRender(EntityDummy dummy, double par2, double par4, double par6, float par8, float par9) {
        super.doRender(dummy, par2, par4, par6, par8, par9);
    }

    @Override
    protected void preRenderCallback(EntityDummy entityliving, float partialTickTime) {
    	GL11.glScalef(this.scale, this.scale, this.scale);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityDummy var1) {
		return this.resource;
	}
}
