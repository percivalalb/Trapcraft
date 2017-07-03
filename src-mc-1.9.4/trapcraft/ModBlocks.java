package trapcraft;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
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
import trapcraft.helper.ModelHelper;
import trapcraft.tileentity.TileEntityBearTrap;
import trapcraft.tileentity.TileEntityFan;
import trapcraft.tileentity.TileEntityIgniter;
import trapcraft.tileentity.TileEntityMagneticChest;

public class ModBlocks {

    public static Block FAN;
    public static Block MAGNETIC_CHEST;
    public static Block GRASS_COVERING;
    public static Block BEAR_TRAP;
    public static Block SPIKES;
    public static Block IGNITER;

	public static void onRegisterBlock() {	
		FAN = new BlockFan().setUnlocalizedName("trapcraft.fan").setRegistryName(Properties.MOD_ID, "fan");
	  	MAGNETIC_CHEST = new BlockMagneticChest().setUnlocalizedName("trapcraft.magnetic_chest").setRegistryName(Properties.MOD_ID, "magnetic_chest");
	  	GRASS_COVERING = new BlockGrassCovering().setUnlocalizedName("trapcraft.grass_covering").setRegistryName(Properties.MOD_ID, "grass_covering");
	    BEAR_TRAP = new BlockBearTrap().setHardness(2.0F).setUnlocalizedName("trapcraft.bear_trap").setRegistryName(Properties.MOD_ID, "bear_trap");
	    SPIKES = new BlockSpikes().setUnlocalizedName("trapcraft.spikes").setRegistryName(Properties.MOD_ID, "spikes");
	    IGNITER = new BlockIgniter().setUnlocalizedName("trapcraft.igniter").setRegistryName(Properties.MOD_ID, "igniter");
	    
	    GameRegistry.registerTileEntity(TileEntityMagneticChest.class, "trapcraft:magnetic_chest");
	    GameRegistry.registerTileEntity(TileEntityFan.class, "trapcraft.fan");
		GameRegistry.registerTileEntity(TileEntityBearTrap.class, "trapcraft:bear_trap");
	    GameRegistry.registerTileEntity(TileEntityIgniter.class, "trapcraft.igniter");
		
		MAGNETIC_CHEST.setHarvestLevel("axe", 0);
		FAN.setHarvestLevel("pickaxe", 0);
		BEAR_TRAP.setHarvestLevel("pickaxe", 0);
		SPIKES.setHarvestLevel("pickaxe", 0);
		IGNITER.setHarvestLevel("pickaxe", 0);
		
		GameRegistry.register(FAN);
		GameRegistry.register(MAGNETIC_CHEST);
		GameRegistry.register(GRASS_COVERING);
		GameRegistry.register(BEAR_TRAP);
		GameRegistry.register(SPIKES);
		GameRegistry.register(IGNITER);
	}
	
	public static void onRegisterItem() {
		GameRegistry.register(makeItemBlock(MAGNETIC_CHEST));
		GameRegistry.register(makeItemBlock(GRASS_COVERING));
		GameRegistry.register(makeItemBlock(FAN));
		GameRegistry.register(makeItemBlock(BEAR_TRAP));
		GameRegistry.register(makeItemBlock(SPIKES));
		GameRegistry.register(makeItemBlock(IGNITER));
	}
	
	@SideOnly(Side.CLIENT)
	public static void setItemModels() {
		ModelHelper.registerBlock(FAN, "trapcraft:fan");
		ModelHelper.registerBlock(MAGNETIC_CHEST, "trapcraft:magnetic_chest");
		ModelHelper.registerBlock(BEAR_TRAP, "trapcraft:bear_trap");
		ModelHelper.registerBlock(GRASS_COVERING, "trapcraft:grass_covering");
		ModelHelper.registerBlock(SPIKES, "trapcraft:spikes");
		ModelHelper.registerBlock(IGNITER, "trapcraft:igniter");
	}
	
	private static ItemBlock makeItemBlock(Block block) {
        return (ItemBlock)new ItemBlock(block).setRegistryName(block.getRegistryName());
    }
}
