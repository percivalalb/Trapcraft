package trapcraft;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trapcraft.api.Properties;
import trapcraft.helper.ModelHelper;
import trapcraft.item.ItemIgniterRange;
import trapcraft.lib.Reference;

public class ModItems {

	public static Item IGNITER_RANGE;
	
	public static void onRegister() {
		IGNITER_RANGE = new ItemIgniterRange().setUnlocalizedName("trapcraft.igniter_range").setRegistryName(Reference.MOD_ID, "igniter_range");

		GameRegistry.register(IGNITER_RANGE);
	}
	
	@SideOnly(Side.CLIENT)
	public static void setItemModels() {
		ModelHelper.registerItem(IGNITER_RANGE, "trapcraft:igniter_range");
	}
}
