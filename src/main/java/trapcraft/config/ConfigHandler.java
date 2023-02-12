package trapcraft.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHandler {

    public static ServerConfig SERVER;
    private static ForgeConfigSpec CONFIG_SERVER_SPEC;

    public static void init(final IEventBus eventBus) {
        final Pair<ServerConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        CONFIG_SERVER_SPEC = commonPair.getRight();
        SERVER = commonPair.getLeft();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CONFIG_SERVER_SPEC);
    }

    public static class ServerConfig {

        public ForgeConfigSpec.IntValue FAN_RANGE;
        public ForgeConfigSpec.DoubleValue FAN_ACCELERATION;
        public ForgeConfigSpec.DoubleValue FAN_MAX_SPEED;

        public ServerConfig(final ForgeConfigSpec.Builder builder) {

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
