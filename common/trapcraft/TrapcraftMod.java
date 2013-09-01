package trapcraft;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import trapcraft.api.OreDictionaryHelper;
import trapcraft.api.Properties;
import trapcraft.common.CommonProxy;
import trapcraft.common.Version;
import trapcraft.common.blocks.BlockBearTrap;
import trapcraft.common.blocks.BlockFan;
import trapcraft.common.blocks.BlockGrassCovering;
import trapcraft.common.blocks.BlockIgniter;
import trapcraft.common.blocks.BlockMagneticChest;
import trapcraft.common.blocks.BlockSpikes;
import trapcraft.common.entity.EntityDummy;
import trapcraft.common.handler.ActionHandler;
import trapcraft.common.items.ItemIgniterRange;
import trapcraft.common.packet.PacketHandler;
import trapcraft.common.tileentitys.TileEntityBearTrap;
import trapcraft.common.tileentitys.TileEntityFan;
import trapcraft.common.tileentitys.TileEntityIgniter;
import trapcraft.common.tileentitys.TileEntityMagneticChest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
@Mod(name= Properties.MOD_NAME, version = Properties.MOD_VERSION, modid = Properties.MOD_ID)
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class TrapcraftMod {
	
     @SidedProxy(clientSide = Properties.SP_CLIENT, serverSide = Properties.SP_SERVER)
     public static CommonProxy proxy;
     
     public static TrapcraftMod instance;
     
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
	 public void preLoad(FMLPreInitializationEvent var1) throws Exception {
		 this.loadConfig(new Configuration(var1.getSuggestedConfigurationFile()));
		 proxy.onModPre();
		 //Event Buses
	     MinecraftForge.EVENT_BUS.register(new ActionHandler());
	     NetworkRegistry.instance().registerGuiHandler(instance, proxy);
	     Properties.packetHandler = new PacketHandler();
	     for(String packetName : Properties.getPackets()) {
	    	 NetworkRegistry.instance().registerChannel(Properties.packetHandler, packetName);
	     }
	 }

	 @EventHandler
	 public void load(FMLInitializationEvent var1) {
	     proxy.onModLoad();
	     fan = new BlockFan(Properties.fanID).setHardness(0.8F).setUnlocalizedName("tc.Fan");
	     magneticChest = new BlockMagneticChest(Properties.magneticChestID).setHardness(2.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("tc.magneticChest");
	     grassCovering = new BlockGrassCovering(Properties.grassCoveringID).setHardness(0.2F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("tc.foilage");
	     igniter = new BlockIgniter(Properties.igniterID).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("tc.Igniter");
	     spikes = new BlockSpikes(Properties.spikesID).setHardness(2.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("tc.spikes");
	     bearTrap = new BlockBearTrap(Properties.bearTrapID).setHardness(2.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("tc.bearTrap");
	     GameRegistry.registerBlock(magneticChest, "tc.magneticChest");
	     GameRegistry.registerTileEntity(TileEntityMagneticChest.class, "tc.magneticChest");
	     GameRegistry.registerBlock(fan, "tc.fan");
		 GameRegistry.registerTileEntity(TileEntityFan.class, "tc.Fan");
		 GameRegistry.registerBlock(grassCovering, "tc.grassCovering");
		 GameRegistry.registerBlock(igniter, "tc.igniter");
		 GameRegistry.registerTileEntity(TileEntityIgniter.class, "tc.Igniter");
		 GameRegistry.registerBlock(spikes, "tc.spikes");
		 GameRegistry.registerBlock(bearTrap, "tc.bearTrap");
		 GameRegistry.registerTileEntity(TileEntityBearTrap.class, "tc.bearTrap");
		 this.registerCreature(EntityDummy.class, "Tc Dummy", EntityRegistry.findGlobalUniqueEntityId());
		 igniter_Range = new ItemIgniterRange(Properties.igniter_RangeID).setUnlocalizedName("tc.igniter_Range");
	 }
	 
	 @EventHandler
	 public void post(FMLPostInitializationEvent var1) {
		 proxy.onModPost();
		 addName(magneticChest, "Magnetic Chest");
		 addNameStr("container.magneticChest", "Magnetic Chest");
		 addName(fan, "Fan");
		 addName(grassCovering, "Grass Covering");
		 addName(igniter, "Igniter");
		 addNameStr("container.igniter", "Upgrades!");
		 addName(spikes, "Spikes");
		 addName(bearTrap, "Bear Trap");
		 addName(igniter_Range, "Igniter Range Module");
		 Version.checkVersion(false);
		 GameRegistry.addShapelessRecipe(new ItemStack(Item.skull, 1, 3), new Object[] {new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Block.cloth, 1, 12)});
	     GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(magneticChest, 1), false, new Object[] {"XXX", "XZX", "XYX", 'Y', Item.redstone, 'X', OreDictionaryHelper.PLANKS, 'Z', Item.ingotIron}));
	     GameRegistry.addRecipe(new ItemStack(fan, 1), new Object[] {"XXX", "XYX", "XXX", 'Y', Item.ingotIron, 'X', Block.cobblestone});
	     GameRegistry.addRecipe(new ItemStack(grassCovering, 3), new Object[] {"XXX", "YYY", 'X', new ItemStack(Block.tallGrass, 1, 1), 'Y', Item.stick});
	     GameRegistry.addRecipe(new ItemStack(bearTrap, 1), new Object[] {"XYX", "XXX", 'X', Item.ingotIron, 'Y', Block.pressurePlateStone});
	     GameRegistry.addRecipe(new ItemStack(igniter, 1), new Object[] {"NNN", "CRC", "CCC", 'N', Block.netherrack, 'R', Item.redstone, 'C', Block.cobblestone});
	     GameRegistry.addRecipe(new ItemStack(spikes, 1), new Object[] {" I ", " I ", "III", 'I', Item.ingotIron});
	     GameRegistry.addRecipe(new ItemStack(igniter_Range, 1), new Object[] {"ALA", "LRL", "ALA", 'R', Item.redstone, 'L', Item.leather, 'A', Item.arrow, 'L', new ItemStack(Item.dyePowder, 1, 4)});
	     
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
		 EntityRegistry.registerGlobalEntityID(var1, var2, var3);
		 EntityRegistry.registerModEntity(var1, var2, var3, instance, 128, 1, true);
	 }
	 
	 public void loadConfig(Configuration var1) {
		 var1.load();
		 //Blocks
		 Properties.fanID = Integer.valueOf(var1.getBlock("fan", 3450).getString());
		 Properties.igniterID = Integer.valueOf(var1.getBlock("igniter", 3451).getString());
		 Properties.magneticChestID = Integer.valueOf(var1.getBlock("magneticChest", 3452).getString());
	     Properties.bearTrapID = Integer.valueOf(var1.getBlock("bearTrap", 3453).getString());
	     Properties.grassCoveringID = Integer.valueOf(var1.getBlock("grassCovering", 3454).getString());
	     Properties.dummyHeadID = Integer.valueOf(var1.getBlock("dummyHead", 3455).getString());
	     Properties.spikesID = Integer.valueOf(var1.getBlock("spikes", 3456).getString());
	     //Items
	     Properties.igniter_RangeID = Integer.valueOf(var1.getItem("igniter_Range", 5450).getString());
		 var1.save();
	 }
}
