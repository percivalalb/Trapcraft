package trapcraft.client.renders;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import trapcraft.api.Constants;
import trapcraft.entity.DummyEntity;
import trapcraft.handler.ModelHandler;

/**
 * @author ProPercivalalb
 */
public class DummyRenderer extends LivingEntityRenderer<DummyEntity, HumanoidModel<DummyEntity>> {

    public DummyRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, createModel(renderManagerIn.getModelSet(), ModelHandler.DUMMY), 0.5F); // TODO
    }

    private static HumanoidModel<DummyEntity> createModel(EntityModelSet p_174350_, ModelLayerLocation p_174351_) {
        HumanoidModel<DummyEntity> piglinmodel = new HumanoidModel<>(p_174350_.bakeLayer(p_174351_));

        return piglinmodel;
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
