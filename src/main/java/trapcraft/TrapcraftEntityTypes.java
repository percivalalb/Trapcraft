package trapcraft;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.entity.EntityDummy;
import trapcraft.lib.Reference;

public class TrapcraftEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<EntityType<EntityDummy>> DUMMY = ENTITIES.register("dummy", () -> EntityType.Builder.<EntityDummy>create(EntityDummy::new, EntityClassification.AMBIENT).setCustomClientFactory(EntityDummy::new).build("dummy"));
}
