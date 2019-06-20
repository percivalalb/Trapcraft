package trapcraft.client.renders;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import trapcraft.api.Properties;
import trapcraft.entity.EntityDummy;

/**
 * @author ProPercivalalb
 */
public class RenderDummy extends RenderLiving<EntityDummy> {
	
	private float scale = 1F;

    public RenderDummy(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelBiped(0.0F), 0.5F);
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
		switch(dummy.getVariant()) {
		case 0:
			return Properties.RES_MOB_DUMMY_OAK;
		case 1:
			return Properties.RES_MOB_DUMMY_SPRUCE;
		case 2:
			return Properties.RES_MOB_DUMMY_BIRCH;
		case 3:
			return Properties.RES_MOB_DUMMY_JUNGLE;
		case 4:
			return Properties.RES_MOB_DUMMY_ACACIA;
		case 5:
			return Properties.RES_MOB_DUMMY_DARK_OAK;
		default:
			return Properties.RES_MOB_DUMMY_OAK;	
		}
	}
}
