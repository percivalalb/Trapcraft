package trapcraft.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import trapcraft.handler.ActionHandler;
import trapcraft.network.PacketHandler;

public class CommonProxy {

	public CommonProxy() {
        // ConfigHandler.init();
        // Add listeners for common events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
    }

	protected void preInit(FMLCommonSetupEvent event) {
        PacketHandler.register();
        MinecraftForge.EVENT_BUS.register(new ActionHandler());
    }

    protected void init(InterModEnqueueEvent event) {

    }

    protected void postInit(InterModProcessEvent event) {

    }

    public PlayerEntity getPlayerEntity() {
		return null;
	}
}
