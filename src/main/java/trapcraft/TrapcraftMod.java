package trapcraft;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import trapcraft.api.OreDictionaryHelper;
import trapcraft.api.Properties;
import trapcraft.common.CommonProxy;
import trapcraft.common.VersionHelper;
import trapcraft.common.VersionHelper.UpdateType;
import trapcraft.common.blocks.BlockBearTrap;
import trapcraft.common.blocks.BlockFan;
import trapcraft.common.blocks.BlockGrassCovering;
import trapcraft.common.blocks.BlockIgniter;
import trapcraft.common.blocks.BlockMagneticChest;
import trapcraft.common.blocks.BlockSpikes;
import trapcraft.common.entity.EntityDummy;
import trapcraft.common.handler.ActionHandler;
import trapcraft.common.items.ItemIgniterRange;
import trapcraft.common.tileentitys.TileEntityBearTrap;
import trapcraft.common.tileentitys.TileEntityFan;
import trapcraft.common.tileentitys.TileEntityIgniter;
import trapcraft.common.tileentitys.TileEntityMagneticChest;
import trapcraft.network.NetworkManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * @author ProPercivalalb
 **/
@Mod(name = Properties.MOD_NAME, version = Properties.MOD_VERSION, modid = Properties.MOD_ID)
public class TrapcraftMod {
	
     @SidedProxy(clientSide = Properties.SP_CLIENT, serverSide = Properties.SP_SERVER)
     public static CommonProxy proxy;
     
     @Instance(Properties.MOD_ID)
     public static TrapcraftMod instance;
     
     public static NetworkManager NETWORK_MANAGER;
     
     public static Block fan;
     public static Block magneticChest;
     public static Block grassCovering;
     public static Block igniter;
     public static Block spikes;
     public static Block bearTrap;
     
     public static Item igniter_Range;
     
     public TrapcraftMod() {
    	 instance = this;
     }
     
     @EventHandler
	 public void preLoad(FMLPreInitializationEvent event) throws Exception {
		 this.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));
		 VersionHelper.checkVersion(UpdateType.BLANK);
		 
		 proxy.onModPre();
	     fan = new BlockFan().setHardness(0.8F).setBlockName("trapcraft.fan");
	     magneticChest = new BlockMagneticChest().setHardness(2.5F).setStepSound(Block.soundTypeWood).setBlockName("trapcraft.magneticchest");
	     grassCovering = new BlockGrassCovering().setHardness(0.2F).setStepSound(Block.soundTypeGrass).setBlockName("trapcraft.grasscovering");
	     igniter = new BlockIgniter().setHardness(3.5F).setStepSound(Block.soundTypeStone).setBlockName("trapcraft.igniter");
	     spikes = new BlockSpikes().setHardness(2.0F).setStepSound(Block.soundTypeMetal).setBlockName("trapcraft.spikes");
	     bearTrap = new BlockBearTrap().setHardness(2.0F).setStepSound(Block.soundTypeMetal).setBlockName("trapcraft.beartrap");
	     GameRegistry.registerBlock(magneticChest, "magneticchest");
	     GameRegistry.registerTileEntity(TileEntityMagneticChest.class, "trapcraft:magneticchest");
	     GameRegistry.registerBlock(fan, "tc.fan");
		 GameRegistry.registerTileEntity(TileEntityFan.class, "trapcraft:fan");
		 GameRegistry.registerBlock(grassCovering, "grasscovering");
		 GameRegistry.registerBlock(igniter, "igniter");
		 GameRegistry.registerTileEntity(TileEntityIgniter.class, "trapcraft:igniter");
		 GameRegistry.registerBlock(spikes, "spikes");
		 GameRegistry.registerBlock(bearTrap, "bearTrap");
		 GameRegistry.registerTileEntity(TileEntityBearTrap.class, "trapcraft:beartrap");
		 this.registerCreature(EntityDummy.class, "trapcraft:dummy", 0);
		 
		 
		 igniter_Range = new ItemIgniterRange().setUnlocalizedName("trapcraft.igniterrange");
		 GameRegistry.registerItem(igniter_Range, "trapcraft:igniterrange");
	 }

	 @EventHandler
	 public void load(FMLInitializationEvent var1) {
	     NETWORK_MANAGER = new NetworkManager();
	     proxy.onModLoad();
		 //Event Buses
	     MinecraftForge.EVENT_BUS.register(new ActionHandler());
	     NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
	 }
	 
	 @EventHandler
	 public void post(FMLPostInitializationEvent var1) {
		 proxy.onModPost();
		 GameRegistry.addShapelessRecipe(new ItemStack(Items.skull, 1, 3), new Object[] {new ItemStack(Items.dye, 1, 4), new ItemStack(Blocks.wool, 1, 12)});
	     GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(magneticChest, 1), false, new Object[] {"XXX", "XZX", "XYX", 'Y', Items.redstone, 'X', OreDictionaryHelper.PLANKS, 'Z', Items.iron_ingot}));
	     GameRegistry.addRecipe(new ItemStack(fan, 1), new Object[] {"XXX", "XYX", "XXX", 'Y', Items.iron_ingot, 'X', Blocks.cobblestone});
	     GameRegistry.addRecipe(new ItemStack(grassCovering, 3), new Object[] {"XXX", "YYY", 'X', new ItemStack(Blocks.tallgrass, 1, 1), 'Y', Items.stick});
	     GameRegistry.addRecipe(new ItemStack(bearTrap, 1), new Object[] {"XYX", "XXX", 'X', Items.iron_ingot, 'Y', Blocks.stone_pressure_plate});
	     GameRegistry.addRecipe(new ItemStack(igniter, 1), new Object[] {"NNN", "CRC", "CCC", 'N', Blocks.netherrack, 'R', Items.redstone, 'C', Blocks.cobblestone});
	     GameRegistry.addRecipe(new ItemStack(spikes, 1), new Object[] {" I ", " I ", "III", 'I', Items.iron_ingot});
	     GameRegistry.addRecipe(new ItemStack(igniter_Range, 1), new Object[] {"ALA", "LRL", "ALA", 'R', Items.redstone, 'L', Items.leather, 'A', Items.arrow, 'L', new ItemStack(Items.dye, 1, 4)});
	     
	 }
	 
	 public void addName(Object objectToName, String name) {
		 LanguageRegistry.instance().addNameForObject(objectToName, "en_US", name);
		 LanguageRegistry.instance().addNameForObject(objectToName, "en_GB", name);
	 }
	 
	 public void addNameStr(String objectToName, String name) {
		 LanguageRegistry.instance().addStringLocalization(objectToName, "en_US", name);
		 LanguageRegistry.instance().addStringLocalization(objectToName, "en_GB", name);
	 }
	 
	 public void registerCreature(Class var1, String var2, int var3) {
		 EntityRegistry.registerModEntity(var1, var2, var3, instance, 128, 1, true);
	 }
	 
	 public void loadConfig(Configuration var1) {
		 var1.load();
		 var1.save();
	 }
}
