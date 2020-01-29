package trapcraft.client.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import trapcraft.api.Constants;
import trapcraft.api.Properties;
import trapcraft.entity.EntityDummy;

/**
 * @author ProPercivalalb
 */
public class RenderDummy extends LivingRenderer<EntityDummy, BipedModel<EntityDummy>> {

	private float scale = 1F;

    public RenderDummy(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BipedModel<EntityDummy>(0.0F), 0.5F);
    }

    @Override
    public void doRender(EntityDummy dummy, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(dummy, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(EntityDummy dummy, float partialTickTime) {
    	//GlStateManager.scalef(this.scale, this.scale, this.scale);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityDummy dummy) {
		switch(dummy.getVariant()) {
		case 0:
			return Constants.RES_MOB_DUMMY_OAK;
		case 1:
			return Constants.RES_MOB_DUMMY_SPRUCE;
		case 2:
			return Constants.RES_MOB_DUMMY_BIRCH;
		case 3:
			return Constants.RES_MOB_DUMMY_JUNGLE;
		case 4:
			return Constants.RES_MOB_DUMMY_ACACIA;
		case 5:
			return Constants.RES_MOB_DUMMY_DARK_OAK;
		default:
			return Constants.RES_MOB_DUMMY_OAK;
		}
	}

	@Override
	protected boolean canRenderName(EntityDummy entity) {
		return false;
	}
}
