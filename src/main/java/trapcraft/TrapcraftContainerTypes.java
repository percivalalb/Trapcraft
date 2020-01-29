package trapcraft;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;
import trapcraft.inventory.ContainerIgniter;

public class TrapcraftContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Constants.MOD_ID);

	public static final RegistryObject<ContainerType<ContainerIgniter>> IGNITER = CONTAINERS.register("igniter", () -> new ContainerType<ContainerIgniter>(ContainerIgniter::new));
}