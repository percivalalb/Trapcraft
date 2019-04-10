package trapcraft;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trapcraft.api.Properties;
import trapcraft.client.model.ModelHelper;
import trapcraft.item.ItemIgniterRange;
import trapcraft.lib.Reference;

@EventBusSubscriber
public class ModItems {

	public static Item IGNITER_RANGE;
	
	@SubscribeEvent
	public static void onRegister(RegistryEvent.Register<Item> event) {
		IGNITER_RANGE = new ItemIgniterRange().setUnlocalizedName("trapcraft.igniter_range").setRegistryName(Reference.MOD_ID, "igniter_range");

		event.getRegistry().register(IGNITER_RANGE);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void setItemModels(ModelRegistryEvent event) {
		ModelHelper.setModel(IGNITER_RANGE, 0, "trapcraft:igniter_range");
	}
}
