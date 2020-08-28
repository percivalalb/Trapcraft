package trapcraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
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
import trapcraft.api.Constants;
import trapcraft.client.gui.IgniterScreen;
import trapcraft.client.renders.DummyRenderer;
import trapcraft.client.renders.ItemStackTileEntityMagneticChestRenderer;
import trapcraft.client.renders.TileEntityMagneticChestRenderer;
import trapcraft.config.ConfigHandler;
import trapcraft.data.TrapcraftBlockstateProvider;
import trapcraft.data.TrapcraftItemModelProvider;
import trapcraft.data.TrapcraftLootTableProvider;
import trapcraft.data.TrapcraftRecipeProvider;
import trapcraft.handler.ActionHandler;
import trapcraft.network.PacketHandler;

@Mod(Constants.MOD_ID)
public final class TrapcraftMod {

	public static final Logger LOGGER = LogManager.getLogger(Constants.MOD_NAME);
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	public static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Constants.MOD_ID, "channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

	public static TrapcraftMod INSTANCE;

	public TrapcraftMod() {
		INSTANCE = this;

	    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

	    TrapcraftBlocks.BLOCKS.register(modEventBus);
	    TrapcraftTileEntityTypes.TILE_ENTITIES.register(modEventBus);
	    TrapcraftItems.ITEMS.register(modEventBus);
	    TrapcraftEntityTypes.ENTITIES.register(modEventBus);
	    TrapcraftContainerTypes.CONTAINERS.register(modEventBus);

	    modEventBus.addListener(this::gatherData);
	    modEventBus.addListener(this::commonSetup);
	    modEventBus.addListener(this::interModProcess);

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
		    modEventBus.addListener(this::clientSetup);
	        modEventBus.addListener(this::registerBlockColors);
	        modEventBus.addListener(this::registerItemColors);
	        modEventBus.addListener(this::addTexturesToAtlas);
		});

		IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

		forgeEventBus.register(new ActionHandler());

	    ConfigHandler.init(modEventBus);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
	    PacketHandler.register();
	    TrapcraftEntityTypes.addEntityAttributes();
    }

	@OnlyIn(Dist.CLIENT)
	private void clientSetup(final FMLClientSetupEvent event) {
	    RenderingRegistry.registerEntityRenderingHandler(TrapcraftEntityTypes.DUMMY.get(), DummyRenderer::new);
        ScreenManager.registerFactory(TrapcraftContainerTypes.IGNITER.get(), IgniterScreen::new);
        ClientRegistry.bindTileEntityRenderer(TrapcraftTileEntityTypes.MAGNETIC_CHEST.get(), TileEntityMagneticChestRenderer::new);

        RenderTypeLookup.setRenderLayer(TrapcraftBlocks.SPIKES.get(), RenderType.getCutout());
        // Must be set here to avoid registry object missing error
        ItemStackTileEntityMagneticChestRenderer.setDummyTE();
    }

	@OnlyIn(Dist.CLIENT)
	private void registerBlockColors(final ColorHandlerEvent.Block event) {
	    final BlockColors blockColors = event.getBlockColors();
	    blockColors.register((state, blockAccess, pos, tintIndex) -> {
            return blockAccess != null && pos != null ? BiomeColors.getGrassColor(blockAccess, pos) : -1;
        }, TrapcraftBlocks.GRASS_COVERING.get());
	}

	@OnlyIn(Dist.CLIENT)
	private void registerItemColors(final ColorHandlerEvent.Item event) {
	    final ItemColors itemColors = event.getItemColors();
        itemColors.register((stack, tintIndex) -> GrassColors.get(0.5D, 1.0D), TrapcraftBlocks.GRASS_COVERING.get());
    }

	@OnlyIn(Dist.CLIENT)
	private void addTexturesToAtlas(final TextureStitchEvent.Pre event) {
	    if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
    	    event.addSprite(Constants.RES_BLOCK_MAGNETIC_CHEST);
	    }
	}

    private void interModProcess(final InterModProcessEvent event) {

    }

	private void gatherData(final GatherDataEvent event) {
        final DataGenerator gen = event.getGenerator();

        if (event.includeClient()) {
            final TrapcraftBlockstateProvider blockstates = new TrapcraftBlockstateProvider(gen, event.getExistingFileHelper());
            gen.addProvider(blockstates);
            gen.addProvider(new TrapcraftItemModelProvider(gen, blockstates.getExistingHelper()));
        }

        if (event.includeServer()) {
            gen.addProvider(new TrapcraftRecipeProvider(gen));
            gen.addProvider(new TrapcraftLootTableProvider(gen));
        }
    }
}
