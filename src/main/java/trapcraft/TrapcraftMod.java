package trapcraft;

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
import trapcraft.entity.EntityDummy;
import trapcraft.handler.ActionHandler;
import trapcraft.lib.Reference;
import trapcraft.network.PacketDispatcher;
import trapcraft.proxy.CommonProxy;

/**
 * @author ProPercivalalb
 **/
@Mod(name = Reference.MOD_NAME, version = Reference.MOD_VERSION, modid = Reference.MOD_ID, updateJSON = Reference.UPDATE_URL)
public class TrapcraftMod {

     @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
     public static CommonProxy PROXY;

     @Instance(Reference.MOD_ID)
     public static TrapcraftMod INSTANCE;

     @EventHandler
     public void preLoad(FMLPreInitializationEvent event) throws Exception {
         this.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));

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
     }

     public void registerCreature(Class var1, String var2, int var3) {
         EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, var2), var1, var2, var3, INSTANCE, 128, 1, true);
     }

     public void loadConfig(Configuration var1) {
         var1.load();
         var1.save();
     }
}
