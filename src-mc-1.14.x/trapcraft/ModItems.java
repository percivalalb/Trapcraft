package trapcraft;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import trapcraft.item.ItemIgniterRange;
import trapcraft.lib.ItemNames;
import trapcraft.lib.Reference;

public class ModItems {

	@ObjectHolder(ItemNames.IGNITER_RANGE)
	public static Item IGNITER_RANGE;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
		
	    @SubscribeEvent
	    public static void onItemRegister(final RegistryEvent.Register<Item> event) {
	    	IForgeRegistry<Item> itemRegistry = event.getRegistry();
	    	itemRegistry.register(new ItemIgniterRange().setRegistryName(ItemNames.IGNITER_RANGE));
	    }
    }
}