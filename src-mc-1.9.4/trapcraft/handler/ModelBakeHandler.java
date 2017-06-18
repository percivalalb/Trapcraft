package trapcraft.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModelBakeHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
	    TextureAtlasSprite base = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/slime");

	    for(String thing : new String[] {"inventory", "facing=north", "facing=south", "facing=east", "facing=west"}) {
	    	IBakedModel model = (IBakedModel)event.getModelRegistry().getObject(new ModelResourceLocation("trapcraft:magnetic_chest", thing));
	    	event.getModelRegistry().putObject(new ModelResourceLocation("trapcraft:magnetic_chest", thing), new BuiltInModel());
	    }
	    
	}
}
