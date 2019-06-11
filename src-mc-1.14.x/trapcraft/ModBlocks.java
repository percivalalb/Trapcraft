package trapcraft;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
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
import trapcraft.tileentity.TileEntityBearTrap;
import trapcraft.tileentity.TileEntityFan;
import trapcraft.tileentity.TileEntityIgniter;
import trapcraft.tileentity.TileEntityMagneticChest;

public class ModBlocks {
	
	@ObjectHolder(BlockNames.FAN)
	public static Block FAN;
	@ObjectHolder(BlockNames.MAGNETIC_CHEST)
    public static Block MAGNETIC_CHEST;
	@ObjectHolder(BlockNames.GRASS_COVERING)
    public static Block GRASS_COVERING;
	@ObjectHolder(BlockNames.BEAR_TRAP)
    public static Block BEAR_TRAP;
	@ObjectHolder(BlockNames.SPIKES)
    public static Block SPIKES;
	@ObjectHolder(BlockNames.IGNITER)
    public static Block IGNITER;
	
	@ObjectHolder(BlockNames.MAGNETIC_CHEST)
	public static TileEntityType<TileEntityMagneticChest> TILE_MAGNETIC_CHEST;
	@ObjectHolder(BlockNames.FAN)
	public static TileEntityType<TileEntityFan> TILE_FAN;
	@ObjectHolder(BlockNames.BEAR_TRAP)
	public static TileEntityType<TileEntityBearTrap> TILE_BEAR_TRAP;
	@ObjectHolder(BlockNames.IGNITER)
	public static TileEntityType<TileEntityIgniter> TILE_IGNITER;
	
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
	    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event){
	    	IForgeRegistry<TileEntityType<?>> tileRegistry = event.getRegistry();
	    	
	        tileRegistry.register(register(BlockNames.MAGNETIC_CHEST, TileEntityType.Builder.create(TileEntityMagneticChest::new)).setRegistryName(BlockNames.MAGNETIC_CHEST));
	        tileRegistry.register(register(BlockNames.FAN, TileEntityType.Builder.create(TileEntityFan::new)).setRegistryName(BlockNames.FAN));
	        tileRegistry.register(register(BlockNames.BEAR_TRAP, TileEntityType.Builder.create(TileEntityBearTrap::new)).setRegistryName(BlockNames.BEAR_TRAP));
	        tileRegistry.register(register(BlockNames.IGNITER, TileEntityType.Builder.create(TileEntityIgniter::new)).setRegistryName(BlockNames.IGNITER));
	    }
	    
	    public static <T extends TileEntity> TileEntityType<T> register(String id, TileEntityType.Builder<T> builder) {
	        Type<?> type = null;

	        try {
	           type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(1631)).getChoiceType(TypeReferences.BLOCK_ENTITY, id);
	        } 
	        catch(IllegalArgumentException illegalstateexception) {
	           if(SharedConstants.developmentMode) {
	              throw illegalstateexception;
	           }

	           TrapcraftMod.LOGGER.warn("No data fixer registered for block entity {}", (Object)id);
	        }

	        TileEntityType<T> tileentitytype = builder.build(type);
	        return tileentitytype;
		}
		
	    @SubscribeEvent
	    public static void onItemRegister(final RegistryEvent.Register<Item> event) {
	       	IForgeRegistry<Item> itemRegistry = event.getRegistry();
	       	
	       	itemRegistry.register(new ItemBlock(MAGNETIC_CHEST, new Item.Properties().group(ItemGroup.REDSTONE).setTEISR(() -> TileEntityItemStackMagneticChestRenderer::new)).setRegistryName(MAGNETIC_CHEST.getRegistryName()));
	       	itemRegistry.register(makeItemBlock(GRASS_COVERING));
	       	itemRegistry.register(makeItemBlock(FAN));
	       	itemRegistry.register(makeItemBlock(BEAR_TRAP));
	       	itemRegistry.register(makeItemBlock(SPIKES));
	       	itemRegistry.register(makeItemBlock(IGNITER));
	    }
	    
	    private static ItemBlock makeItemBlock(Block block) {
	    	return makeItemBlock(block, ItemGroup.REDSTONE);
	    }
	    
	    private static ItemBlock makeItemBlock(Block block, ItemGroup group) {
	        return (ItemBlock)new ItemBlock(block, new Item.Properties().group(group)).setRegistryName(block.getRegistryName());
	    }
    }
}