package trapcraft;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
     public static CommonProxy PROXY;
     
     @Instance(Properties.MOD_ID)
     public static TrapcraftMod INSTANCE;
     
     @EventHandler
	 public void preLoad(FMLPreInitializationEvent event) throws Exception {
    	 this.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));
    	 
    	 ModBlocks.onRegisterBlock();
    	 ModBlocks.onRegisterItem();

    	 ModItems.onRegister();
    	 
		 this.registerCreature(EntityDummy.class, "dummy", 0);

		 PROXY.onModPre();
	 }

	 @EventHandler
	 public void load(FMLInitializationEvent var1) {
	     PROXY.onModLoad();
		 //Event Buses
	     MinecraftForge.EVENT_BUS.register(new ActionHandler());
	     NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, PROXY);
		 PacketDispatcher.registerPackets();
	 }
	 
	 @EventHandler
	 public void post(FMLPostInitializationEvent var1) {
		 PROXY.onModPost();
		 GameRegistry.addShapelessRecipe(new ItemStack(Items.SKULL, 1, 3), new Object[] {new ItemStack(Items.DYE, 1, 4), new ItemStack(Blocks.WOOL, 1, 12)});
	     GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.MAGNETIC_CHEST, 1), false, new Object[] {"XXX", "XZX", "XYX", 'Y', Items.REDSTONE, 'X', OreDictionaryHelper.PLANKS, 'Z', Items.IRON_INGOT}));
	     GameRegistry.addRecipe(new ItemStack(ModBlocks.FAN, 1), new Object[] {"XXX", "XYX", "XXX", 'Y', Items.IRON_INGOT, 'X', Blocks.COBBLESTONE});
	     GameRegistry.addRecipe(new ItemStack(ModBlocks.GRASS_COVERING, 3), new Object[] {"XXX", "YYY", 'X', new ItemStack(Blocks.TALLGRASS, 1, 1), 'Y', Items.STICK});
	     GameRegistry.addRecipe(new ItemStack(ModBlocks.BEAR_TRAP, 1), new Object[] {"XYX", "XXX", 'X', Items.IRON_INGOT, 'Y', Blocks.STONE_PRESSURE_PLATE});
	     GameRegistry.addRecipe(new ItemStack(ModBlocks.IGNITER, 1), new Object[] {"NNN", "CRC", "CCC", 'N', Blocks.NETHERRACK, 'R', Items.REDSTONE, 'C', Blocks.COBBLESTONE});
	     GameRegistry.addRecipe(new ItemStack(ModBlocks.SPIKES, 1), new Object[] {" I ", " I ", "III", 'I', Items.IRON_INGOT});
	     GameRegistry.addRecipe(new ItemStack(ModItems.IGNITER_RANGE, 1), new Object[] {"ALA", "LRL", "ALA", 'R', Items.REDSTONE, 'L', Items.LEATHER, 'A', Items.ARROW, 'L', new ItemStack(Items.DYE, 1, 4)});
	     
	 }

	 public void registerCreature(Class var1, String var2, int var3) {
		 EntityRegistry.registerModEntity(var1, var2, var3, INSTANCE, 128, 1, true);
	 }
	 
	 public void loadConfig(Configuration var1) {
		 var1.load();
		 var1.save();
	 }
}
