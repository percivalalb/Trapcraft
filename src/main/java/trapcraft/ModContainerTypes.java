package trapcraft;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import trapcraft.inventory.ContainerIgniter;
import trapcraft.lib.Reference;

@ObjectHolder(Reference.MOD_ID)
public class ModContainerTypes {
	
	public static final ContainerType<ContainerIgniter> IGNITER = null;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
	    
	    @SubscribeEvent
	    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
	    	IForgeRegistry<ContainerType<?>> containerRegistry = event.getRegistry();
	    	
	        containerRegistry.register(new ContainerType<ContainerIgniter>(ContainerIgniter::new).setRegistryName("trapcraft:igniter"));
	    }
    }
}