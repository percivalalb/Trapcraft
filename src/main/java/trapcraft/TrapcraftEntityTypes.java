package trapcraft;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;
import trapcraft.entity.DummyEntity;

public class TrapcraftEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MOD_ID);

    public static final RegistryObject<EntityType<DummyEntity>> DUMMY = ENTITIES.register("dummy", () -> EntityType.Builder.<DummyEntity>of(DummyEntity::new, MobCategory.AMBIENT).setCustomClientFactory(DummyEntity::new).build("dummy"));

    public static void addEntityAttributes(EntityAttributeCreationEvent e) {
        e.put(DUMMY.get(), DummyEntity.createAttributeMap().build());
    }
}
