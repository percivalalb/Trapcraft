package trapcraft;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GrassColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import trapcraft.api.Constants;
import trapcraft.client.gui.IgniterScreen;
import trapcraft.config.ConfigHandler;
import trapcraft.data.TrapcraftBlockstateProvider;
import trapcraft.data.TrapcraftItemModelProvider;
import trapcraft.data.TrapcraftLootTableProvider;
import trapcraft.data.TrapcraftRecipeProvider;
import trapcraft.handler.ActionHandler;
import trapcraft.handler.ModelHandler;
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
        modEventBus.addListener(TrapcraftEntityTypes::addEntityAttributes);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(this::clientSetup);
            modEventBus.addListener(this::registerBlockColors);
            modEventBus.addListener(this::registerItemColors);
            modEventBus.addListener(this::addTexturesToAtlas);
            modEventBus.register(new ModelHandler());
        });

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        forgeEventBus.register(new ActionHandler());

        ConfigHandler.init(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        PacketHandler.register();
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(TrapcraftContainerTypes.IGNITER.get(), IgniterScreen::new);
    }

    @OnlyIn(Dist.CLIENT)
    private void registerBlockColors(final RegisterColorHandlersEvent.Block event) {
        final BlockColors blockColors = event.getBlockColors();
        blockColors.register((state, blockAccess, pos, tintIndex) -> {
            return blockAccess != null && pos != null ? BiomeColors.getAverageGrassColor(blockAccess, pos) : -1;
        }, TrapcraftBlocks.GRASS_COVERING.get());
    }

    @OnlyIn(Dist.CLIENT)
    private void registerItemColors(final RegisterColorHandlersEvent.Item event) {
        final ItemColors itemColors = event.getItemColors();
        itemColors.register((stack, tintIndex) -> GrassColor.get(0.5D, 1.0D), TrapcraftBlocks.GRASS_COVERING.get());
    }

    @OnlyIn(Dist.CLIENT)
    private void addTexturesToAtlas(final TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
            event.addSprite(Constants.RES_BLOCK_MAGNETIC_CHEST);
        }
    }

    private void interModProcess(final InterModProcessEvent event) {

    }

    private void gatherData(final GatherDataEvent event) {
        final DataGenerator gen = event.getGenerator();

        if (event.includeClient()) {
            final TrapcraftBlockstateProvider blockstates = new TrapcraftBlockstateProvider(gen, event.getExistingFileHelper());
            gen.addProvider(true, blockstates);
            gen.addProvider(true, new TrapcraftItemModelProvider(gen, blockstates.getExistingHelper()));
        }

        if (event.includeServer()) {
            gen.addProvider(true, new TrapcraftRecipeProvider(gen));
            gen.addProvider(true, new TrapcraftLootTableProvider(gen));
        }
    }
}
