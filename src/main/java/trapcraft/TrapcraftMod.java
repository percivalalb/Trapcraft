package trapcraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import trapcraft.client.gui.GuiIgniter;
import trapcraft.client.renders.RenderDummy;
import trapcraft.client.renders.TileEntityMagneticChestRenderer;
import trapcraft.entity.EntityDummy;
import trapcraft.handler.ActionHandler;
import trapcraft.lib.Reference;
import trapcraft.network.PacketHandler;
import trapcraft.tileentity.TileEntityMagneticChest;

/**
 * @author ProPercivalalb
 **/
@Mod(value = Reference.MOD_ID)
public class TrapcraftMod {

	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);
	private static final String PROTOCOL_VERSION = Integer.toString(2);
	public static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Reference.MOD_ID, "channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

	public static TrapcraftMod INSTANCE;

	public TrapcraftMod() {
		INSTANCE = this;

	    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

	    modEventBus.addListener(this::gatherData);
	    modEventBus.addListener(this::commonSetup);
	    modEventBus.addListener(this::clientSetup);
	    modEventBus.addListener(this::interModProcess);

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {

		});

		IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        modEventBus.addListener(this::registerBlockColors);
        modEventBus.addListener(this::registerItemColors);
		forgeEventBus.register(new ActionHandler());

	    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {

	    });
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
	    PacketHandler.register();
    }

	private void clientSetup(final FMLClientSetupEvent event) {
	    RenderingRegistry.registerEntityRenderingHandler(EntityDummy.class, RenderDummy::new);
        ScreenManager.registerFactory(ModContainerTypes.IGNITER, GuiIgniter::new);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMagneticChest.class, new TileEntityMagneticChestRenderer());
    }

	private void registerBlockColors(final ColorHandlerEvent.Block event) {
	    BlockColors blockColors = event.getBlockColors();
	    blockColors.register((state, blockAccess, pos, tintIndex) -> {
            return blockAccess != null && pos != null ? BiomeColors.getGrassColor(blockAccess, pos) : -1;
        }, ModBlocks.GRASS_COVERING);
	}

	private void registerItemColors(final ColorHandlerEvent.Item event) {
	    ItemColors itemColors = event.getItemColors();
        itemColors.register((stack, tintIndex) -> GrassColors.get(0.5D, 1.0D), ModBlocks.GRASS_COVERING);
    }

    private void interModProcess(final InterModProcessEvent event) {

    }

	private void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        if (event.includeClient()) {

        }

        if (event.includeServer()) {

        }
    }
}
