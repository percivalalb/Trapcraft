package trapcraft.client.renders;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import trapcraft.api.Constants;
import trapcraft.entity.DummyEntity;

/**
 * @author ProPercivalalb
 */
public class DummyRenderer extends LivingEntityRenderer<DummyEntity, HumanoidModel<DummyEntity>> {

    public DummyRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new HumanoidModel<DummyEntity>(0.0F), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DummyEntity dummy) {
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
    protected boolean shouldShowName(DummyEntity entity) {
        return false;
    }
}
