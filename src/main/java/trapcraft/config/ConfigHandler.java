package trapcraft.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import trapcraft.api.ConfigValues;

public class ConfigHandler {

    private static ClientConfig CLIENT;
    private static ServerConfig SERVER;
    private static ForgeConfigSpec CONFIG_SERVER_SPEC;
    private static ForgeConfigSpec CONFIG_CLIENT_SPEC;

    public static void init(IEventBus eventBus) {
        Pair<ServerConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        CONFIG_SERVER_SPEC = commonPair.getRight();
        SERVER = commonPair.getLeft();
        Pair<ClientConfig, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CONFIG_CLIENT_SPEC = clientPair.getRight();
        CLIENT = clientPair.getLeft();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CONFIG_SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CONFIG_CLIENT_SPEC);

        eventBus.addListener(ConfigHandler::loadConfig);
        eventBus.addListener(ConfigHandler::reloadConfig);
    }

    public static void loadConfig(final ModConfig.Loading event) {
        ModConfig config = event.getConfig();
        if(config.getSpec() == ConfigHandler.CONFIG_CLIENT_SPEC) {
            ConfigHandler.refreshClient();
        } else if(config.getSpec() == ConfigHandler.CONFIG_SERVER_SPEC) {
            ConfigHandler.refreshServer();
        }
    }

    public static void reloadConfig(final ModConfig.Reloading event) {
        ModConfig config = event.getConfig();
        if(config.getSpec() == ConfigHandler.CONFIG_CLIENT_SPEC) {
            ConfigHandler.refreshClient();
        } else if(config.getSpec() == ConfigHandler.CONFIG_SERVER_SPEC) {
            ConfigHandler.refreshServer();
        }
    }

    public static void refreshServer() {
        ConfigValues.FAN_RANGE = SERVER.FAN_RANGE.get();
        ConfigValues.FAN_ACCELERATION = SERVER.FAN_ACCELERATION.get();
        ConfigValues.FAN_MAX_SPEED = SERVER.FAN_MAX_SPEED.get();
    }

    public static void refreshClient() {

    }

    static class ClientConfig {

        public ClientConfig(ForgeConfigSpec.Builder builder) {

        }
    }

    static class ServerConfig {

        public ForgeConfigSpec.IntValue FAN_RANGE;
        public ForgeConfigSpec.DoubleValue FAN_ACCELERATION;
        public ForgeConfigSpec.DoubleValue FAN_MAX_SPEED;

        public ServerConfig(ForgeConfigSpec.Builder builder) {

            FAN_RANGE = builder
                    .comment("The distance at which entities are pushed by the fan.")
                    .translation("trapcraft.config.fan_range")
                    .defineInRange("fan_range", 5, 0, 64);

            FAN_ACCELERATION = builder
                    .comment("The rate at which velocity is added to entities.")
                    .translation("trapcraft.config.fan_acceleration")
                    .defineInRange("fan_acceleration", 0.05D, 0.01D, 1.0D);

            FAN_MAX_SPEED = builder
                    .comment("The max velocity entities can be given by a fan.")
                    .translation("trapcraft.config.fan_max_speed")
                    .defineInRange("fan_max_speed", 0.3D, 0.01D, 1.0D);
        }
    }
}
