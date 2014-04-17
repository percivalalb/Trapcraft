package trapcraft.client.renders;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class RenderDummy extends RenderLiving {
	
	private float scale = 1F;
	private ResourceLocation resource;
	
    public RenderDummy(ModelBase par1ModelBase, float shadowSize, float scale, ResourceLocation resource) {
        super(par1ModelBase, shadowSize);
        this.scale = scale;
        this.resource = resource;
    }

    public RenderDummy(ModelBase par1ModelBase, float shadowSize, ResourceLocation resource) {
        super(par1ModelBase, shadowSize);
        this.resource = resource;
    }

    public void renderMob(EntityLivingBase par1EntityHadrosaur, double par2, double par4, double par6, float par8, float par9) {
        super.doRender(par1EntityHadrosaur, par2, par4, par6, par8, par9);
    }

    @Override
    public void doRender(EntityLivingBase par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
        this.renderMob(par1EntityLiving, par2, par4, par6, par8, par9);
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderMob((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float partialTickTime) {
    	GL11.glScalef(scale, scale, scale);
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return this.resource;
	}
}
