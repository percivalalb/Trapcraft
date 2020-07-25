package trapcraft;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;
import trapcraft.inventory.IgniterContainer;

public class TrapcraftContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);

	public static final RegistryObject<ContainerType<IgniterContainer>> IGNITER = CONTAINERS.register("igniter", () -> new ContainerType<IgniterContainer>(IgniterContainer::new));
}