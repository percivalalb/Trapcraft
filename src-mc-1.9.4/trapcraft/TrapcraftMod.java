package trapcraft;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import trapcraft.api.OreDictionaryHelper;
import trapcraft.api.Properties;
import trapcraft.block.BlockBearTrap;
import trapcraft.block.BlockFan;
import trapcraft.block.BlockGrassCovering;
import trapcraft.block.BlockIgniter;
import trapcraft.block.BlockMagneticChest;
import trapcraft.block.BlockSpikes;
import trapcraft.entity.EntityDummy;
import trapcraft.handler.ActionHandler;
import trapcraft.item.ItemIgniterRange;
import trapcraft.network.PacketDispatcher;
import trapcraft.proxy.CommonProxy;
import trapcraft.tileentity.TileEntityBearTrap;
import trapcraft.tileentity.TileEntityFan;
import trapcraft.tileentity.TileEntityIgniter;
import trapcraft.tileentity.TileEntityMagneticChest;

/**
 * @author ProPercivalalb
 **/
@Mod(name = Properties.MOD_NAME, version = Properties.MOD_VERSION, modid = Properties.MOD_ID)
public class TrapcraftMod {
	
     @SidedProxy(clientSide = Properties.SP_CLIENT, serverSide = Properties.SP_SERVER)
     public static CommonProxy proxy;
     
     @Instance(Properties.MOD_ID)
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
	 public void preLoad(FMLPreInitializationEvent event) throws Exception {
		 this.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));
		 

	    // fan = new BlockFan().setHardness(0.8F).setUnlocalizedName("trapcraft.fan");
	     magneticChest = new BlockMagneticChest().setHardness(2.5F).setUnlocalizedName("trapcraft.magnetic_chest");
	    // grassCovering = new BlockGrassCovering().setHardness(0.2F).setUnlocalizedName("trapcraft.grass_covering");
	     //igniter = new BlockIgniter().setHardness(3.5F).setUnlocalizedName("trapcraft.igniter");
	     //spikes = new BlockSpikes().setHardness(2.0F).setUnlocalizedName("trapcraft.spikes");
	     //bearTrap = new BlockBearTrap().setHardness(2.0F).setUnlocalizedName("trapcraft.bear_trap");
	     GameRegistry.registerBlock(magneticChest, "magneticchest");
	     GameRegistry.registerTileEntity(TileEntityMagneticChest.class, "trapcraft:magneticchest");
	     //GameRegistry.registerBlock(fan, "tc.fan");
		 //GameRegistry.registerTileEntity(TileEntityFan.class, "trapcraft.fan");
		 //GameRegistry.registerBlock(grassCovering, "grass_covering");
		 //GameRegistry.registerBlock(igniter, "igniter");
		 //GameRegistry.registerTileEntity(TileEntityIgniter.class, "trapcraft.igniter");
		 //GameRegistry.registerBlock(spikes, "spikes");
		 //GameRegistry.registerBlock(bearTrap, "bearTrap");
		 //GameRegistry.registerTileEntity(TileEntityBearTrap.class, "trapcraft.beartrap");
		 this.registerCreature(EntityDummy.class, "dummy", 0);
		 
		 
		 igniter_Range = new ItemIgniterRange().setUnlocalizedName("trapcraft.igniterrange");
		 GameRegistry.registerItem(igniter_Range, "trapcraft:igniterrange");
		 proxy.onModPre();
	 }

	 @EventHandler
	 public void load(FMLInitializationEvent var1) {
	     proxy.onModLoad();
		 //Event Buses
	     MinecraftForge.EVENT_BUS.register(new ActionHandler());
	     NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		 PacketDispatcher.registerPackets();
	 }
	 
	 @EventHandler
	 public void post(FMLPostInitializationEvent var1) {
		 proxy.onModPost();
		 GameRegistry.addShapelessRecipe(new ItemStack(Items.SKULL, 1, 3), new Object[] {new ItemStack(Items.DYE, 1, 4), new ItemStack(Blocks.WOOL, 1, 12)});
	     GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(magneticChest, 1), false, new Object[] {"XXX", "XZX", "XYX", 'Y', Items.REDSTONE, 'X', OreDictionaryHelper.PLANKS, 'Z', Items.IRON_INGOT}));
	     GameRegistry.addRecipe(new ItemStack(fan, 1), new Object[] {"XXX", "XYX", "XXX", 'Y', Items.IRON_INGOT, 'X', Blocks.COBBLESTONE});
	     GameRegistry.addRecipe(new ItemStack(grassCovering, 3), new Object[] {"XXX", "YYY", 'X', new ItemStack(Blocks.TALLGRASS, 1, 1), 'Y', Items.STICK});
	     GameRegistry.addRecipe(new ItemStack(bearTrap, 1), new Object[] {"XYX", "XXX", 'X', Items.IRON_INGOT, 'Y', Blocks.STONE_PRESSURE_PLATE});
	     GameRegistry.addRecipe(new ItemStack(igniter, 1), new Object[] {"NNN", "CRC", "CCC", 'N', Blocks.NETHERRACK, 'R', Items.REDSTONE, 'C', Blocks.COBBLESTONE});
	     GameRegistry.addRecipe(new ItemStack(spikes, 1), new Object[] {" I ", " I ", "III", 'I', Items.IRON_INGOT});
	     GameRegistry.addRecipe(new ItemStack(igniter_Range, 1), new Object[] {"ALA", "LRL", "ALA", 'R', Items.REDSTONE, 'L', Items.LEATHER, 'A', Items.ARROW, 'L', new ItemStack(Items.DYE, 1, 4)});
	     
	 }

	 public void registerCreature(Class var1, String var2, int var3) {
		 EntityRegistry.registerModEntity(var1, var2, var3, instance, 128, 1, true);
	 }
	 
	 public void loadConfig(Configuration var1) {
		 var1.load();
		 var1.save();
	 }
}
