package trapcraft;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import trapcraft.lib.BlockNames;
import trapcraft.lib.Reference;
import trapcraft.tileentity.TileEntityBearTrap;
import trapcraft.tileentity.TileEntityFan;
import trapcraft.tileentity.TileEntityIgniter;
import trapcraft.tileentity.TileEntityMagneticChest;

@ObjectHolder(Reference.MOD_ID)
public class ModTileEntities {
	
	public static final TileEntityType<TileEntityMagneticChest> MAGNETIC_CHEST = null;
	public static final TileEntityType<TileEntityFan> FAN = null;
	public static final TileEntityType<TileEntityBearTrap> BEAR_TRAP = null;
	public static final TileEntityType<TileEntityIgniter> IGNITER = null;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
	    
	    @SubscribeEvent
	    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event){
	    	IForgeRegistry<TileEntityType<?>> tileRegistry = event.getRegistry();
	    	
	        tileRegistry.register(TileEntityType.Builder.create(TileEntityMagneticChest::new, ModBlocks.MAGNETIC_CHEST).build(null).setRegistryName(BlockNames.MAGNETIC_CHEST));
	        tileRegistry.register(TileEntityType.Builder.create(TileEntityFan::new, ModBlocks.FAN).build(null).setRegistryName(BlockNames.FAN));
	        tileRegistry.register(TileEntityType.Builder.create(TileEntityBearTrap::new, ModBlocks.BEAR_TRAP).build(null).setRegistryName(BlockNames.BEAR_TRAP));
	        tileRegistry.register(TileEntityType.Builder.create(TileEntityIgniter::new, ModBlocks.IGNITER).build(null).setRegistryName(BlockNames.IGNITER));
	    }
    }
}