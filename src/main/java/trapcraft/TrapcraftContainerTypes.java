package trapcraft;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;
import trapcraft.inventory.IgniterContainer;

public class TrapcraftContainerTypes {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);

    public static final RegistryObject<MenuType<IgniterContainer>> IGNITER = CONTAINERS.register("igniter", () -> new MenuType<IgniterContainer>(IgniterContainer::new));
}
