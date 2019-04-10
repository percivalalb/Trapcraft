package trapcraft;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trapcraft.api.Properties;
import trapcraft.block.BlockBearTrap;
import trapcraft.block.BlockFan;
import trapcraft.block.BlockGrassCovering;
import trapcraft.block.BlockIgniter;
import trapcraft.block.BlockMagneticChest;
import trapcraft.block.BlockSpikes;
import trapcraft.client.model.ModelHelper;
import trapcraft.lib.Reference;
import trapcraft.tileentity.TileEntityBearTrap;
import trapcraft.tileentity.TileEntityFan;
import trapcraft.tileentity.TileEntityIgniter;
import trapcraft.tileentity.TileEntityMagneticChest;

@EventBusSubscriber
public class ModBlocks {

    public static Block FAN;
    public static Block MAGNETIC_CHEST;
    public static Block GRASS_COVERING;
    public static Block BEAR_TRAP;
    public static Block SPIKES;
    public static Block IGNITER;

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
		FAN = new BlockFan().setUnlocalizedName("trapcraft.fan").setRegistryName(Reference.MOD_ID, "fan");
	  	MAGNETIC_CHEST = new BlockMagneticChest().setUnlocalizedName("trapcraft.magnetic_chest").setRegistryName(Reference.MOD_ID, "magnetic_chest");
	  	GRASS_COVERING = new BlockGrassCovering().setUnlocalizedName("trapcraft.grass_covering").setRegistryName(Reference.MOD_ID, "grass_covering");
	    BEAR_TRAP = new BlockBearTrap().setHardness(2.0F).setUnlocalizedName("trapcraft.bear_trap").setRegistryName(Reference.MOD_ID, "bear_trap");
	    SPIKES = new BlockSpikes().setUnlocalizedName("trapcraft.spikes").setRegistryName(Reference.MOD_ID, "spikes");
	    IGNITER = new BlockIgniter().setUnlocalizedName("trapcraft.igniter").setRegistryName(Reference.MOD_ID, "igniter");
	    
	    GameRegistry.registerTileEntity(TileEntityMagneticChest.class, "trapcraft:magnetic_chest");
	    GameRegistry.registerTileEntity(TileEntityFan.class, "trapcraft:fan");
		GameRegistry.registerTileEntity(TileEntityBearTrap.class, "trapcraft:bear_trap");
	    GameRegistry.registerTileEntity(TileEntityIgniter.class, "trapcraft:igniter");
		
		MAGNETIC_CHEST.setHarvestLevel("axe", 0);
		FAN.setHarvestLevel("pickaxe", 0);
		BEAR_TRAP.setHarvestLevel("pickaxe", 0);
		SPIKES.setHarvestLevel("pickaxe", 0);
		IGNITER.setHarvestLevel("pickaxe", 0);
		
		event.getRegistry().register(FAN);
		event.getRegistry().register(MAGNETIC_CHEST);
		event.getRegistry().register(GRASS_COVERING);
		event.getRegistry().register(BEAR_TRAP);
		event.getRegistry().register(SPIKES);
		event.getRegistry().register(IGNITER);
	}
	
	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(makeItemBlock(MAGNETIC_CHEST));
		event.getRegistry().register(makeItemBlock(GRASS_COVERING));
		event.getRegistry().register(makeItemBlock(FAN));
		event.getRegistry().register(makeItemBlock(BEAR_TRAP));
		event.getRegistry().register(makeItemBlock(SPIKES));
		event.getRegistry().register(makeItemBlock(IGNITER));
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void setItemModels(ModelRegistryEvent event) {
		ModelHelper.setModel(FAN, 0, "trapcraft:fan");
		ModelHelper.setModel(MAGNETIC_CHEST, 0, "trapcraft:magnetic_chest");
		ModelHelper.setModel(BEAR_TRAP, 0, "trapcraft:bear_trap");
		ModelHelper.setModel(GRASS_COVERING, 0, "trapcraft:grass_covering");
		ModelHelper.setModel(SPIKES, 0, "trapcraft:spikes");
		ModelHelper.setModel(IGNITER, 0, "trapcraft:igniter");
	}
	
	private static ItemBlock makeItemBlock(Block block) {
        return (ItemBlock)new ItemBlock(block).setRegistryName(block.getRegistryName());
    }
}
