package trapcraft;

import com.google.common.collect.Sets;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;
import trapcraft.block.tileentity.BearTrapTileEntity;
import trapcraft.block.tileentity.FanTileEntity;
import trapcraft.block.tileentity.IgniterTileEntity;
import trapcraft.block.tileentity.MagneticChestTileEntity;

public class TrapcraftTileEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Constants.MOD_ID);

    public static final RegistryObject<BlockEntityType<MagneticChestTileEntity>> MAGNETIC_CHEST = TILE_ENTITIES.register("magnetic_chest", () -> new BlockEntityType<>(MagneticChestTileEntity::new, Sets.newHashSet(TrapcraftBlocks.MAGNETIC_CHEST.get()), null));
    public static final RegistryObject<BlockEntityType<?>> FAN = TILE_ENTITIES.register("fan", () -> new BlockEntityType<>(FanTileEntity::new, Sets.newHashSet(TrapcraftBlocks.FAN.get()), null));
    public static final RegistryObject<BlockEntityType<?>> BEAR_TRAP = TILE_ENTITIES.register("bear_trap", () -> new BlockEntityType<>(BearTrapTileEntity::new, Sets.newHashSet(TrapcraftBlocks.BEAR_TRAP.get()), null));
    public static final RegistryObject<BlockEntityType<?>> IGNITER = TILE_ENTITIES.register("igniter", () -> new BlockEntityType<>(IgniterTileEntity::new, Sets.newHashSet(TrapcraftBlocks.IGNITER.get()), null));
}
