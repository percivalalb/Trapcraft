package trapcraft;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;
import trapcraft.entity.DummyEntity;

public class TrapcraftEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MOD_ID);

    public static final RegistryObject<EntityType<DummyEntity>> DUMMY = ENTITIES.register("dummy", () -> EntityType.Builder.<DummyEntity>of(DummyEntity::new, EntityClassification.AMBIENT).setCustomClientFactory(DummyEntity::new).build("dummy"));

    public static void addEntityAttributes() {
        GlobalEntityTypeAttributes.put(DUMMY.get(), DummyEntity.createAttributeMap().build());
    }
}
