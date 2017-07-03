package trapcraft.client.renders;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import trapcraft.api.Properties;
import trapcraft.entity.EntityDummy;

/**
 * @author ProPercivalalb
 */
public class RenderDummy extends RenderLiving<EntityDummy> {
	
	private float scale = 1F;
	private ResourceLocation resource;

    public RenderDummy(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelBiped(0.0F), 0.5F);
        this.resource = Properties.RES_MOB_DUMMY;
    }
   
    @Override
    public void doRender(EntityDummy dummy, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(dummy, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(EntityDummy dummy, float partialTickTime) {
    	GL11.glScalef(this.scale, this.scale, this.scale);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityDummy dummy) {
		return this.resource;
	}
}
