package trapcraft;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import trapcraft.block.BlockBearTrap;
import trapcraft.block.BlockFan;
import trapcraft.block.BlockGrassCovering;
import trapcraft.block.BlockIgniter;
import trapcraft.block.BlockMagneticChest;
import trapcraft.block.BlockSpikes;
import trapcraft.client.renders.TileEntityItemStackMagneticChestRenderer;
import trapcraft.lib.BlockNames;
import trapcraft.lib.Reference;

@ObjectHolder(Reference.MOD_ID)
public class ModBlocks {
	
	public static final Block FAN = null;
    public static final Block MAGNETIC_CHEST = null;
    public static final Block GRASS_COVERING = null;
    public static final Block BEAR_TRAP = null;
    public static final Block SPIKES = null;
    public static final Block IGNITER = null;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

	    @SubscribeEvent
	    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
	    	IForgeRegistry<Block> blockRegistry = event.getRegistry();
	    	
	    	blockRegistry.register(new BlockFan().setRegistryName(BlockNames.FAN));
	    	blockRegistry.register(new BlockMagneticChest().setRegistryName(BlockNames.MAGNETIC_CHEST));
	    	blockRegistry.register(new BlockGrassCovering().setRegistryName(BlockNames.GRASS_COVERING));
	    	blockRegistry.register(new BlockBearTrap().setRegistryName(BlockNames.BEAR_TRAP));
	    	blockRegistry.register(new BlockSpikes().setRegistryName(BlockNames.SPIKES));
	    	blockRegistry.register(new BlockIgniter().setRegistryName(BlockNames.IGNITER));
	    }
	    
	    @SubscribeEvent
	    public static void onItemRegister(final RegistryEvent.Register<Item> event) {
	       	IForgeRegistry<Item> itemRegistry = event.getRegistry();
	       	
	       	itemRegistry.register(new BlockItem(MAGNETIC_CHEST, new Item.Properties().group(ItemGroup.REDSTONE).setTEISR(() -> TileEntityItemStackMagneticChestRenderer::new)).setRegistryName(MAGNETIC_CHEST.getRegistryName()));
	       	itemRegistry.register(makeBlockItem(GRASS_COVERING));
	       	itemRegistry.register(makeBlockItem(FAN));
	       	itemRegistry.register(makeBlockItem(BEAR_TRAP));
	       	itemRegistry.register(makeBlockItem(SPIKES));
	       	itemRegistry.register(makeBlockItem(IGNITER));
	    }
	    
	    private static BlockItem makeBlockItem(Block block) {
	    	return makeBlockItem(block, ItemGroup.REDSTONE);
	    }
	    
	    private static BlockItem makeBlockItem(Block block, ItemGroup group) {
	        return (BlockItem)new BlockItem(block, new Item.Properties().group(group)).setRegistryName(block.getRegistryName());
	    }
    }
}