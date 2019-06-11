package trapcraft;

import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import trapcraft.entity.EntityDummy;
import trapcraft.lib.EntityNames;
import trapcraft.lib.Reference;

public class ModEntities {

	@ObjectHolder(EntityNames.DUMMY)
	public static EntityType<EntityDummy> DUMMY;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
		
		 @SubscribeEvent
		 public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
			 IForgeRegistry<EntityType<?>> entityRegistry = event.getRegistry();
			 
			 entityRegistry.register(EntityType.Builder.create(EntityDummy.class, EntityDummy::new).build(EntityNames.DUMMY).setRegistryName(EntityNames.DUMMY));
		 }
    }
}
