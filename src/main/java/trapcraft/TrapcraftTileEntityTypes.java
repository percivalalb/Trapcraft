package trapcraft;

import com.google.common.collect.Sets;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;
import trapcraft.block.tileentity.BearTrapTileEntity;
import trapcraft.block.tileentity.FanTileEntity;
import trapcraft.block.tileentity.IgniterTileEntity;
import trapcraft.block.tileentity.MagneticChestTileEntity;

public class TrapcraftTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Constants.MOD_ID);

	public static final RegistryObject<TileEntityType<? extends MagneticChestTileEntity>> MAGNETIC_CHEST = TILE_ENTITIES.register("magnetic_chest", () -> new TileEntityType<>(MagneticChestTileEntity::new, Sets.newHashSet(TrapcraftBlocks.MAGNETIC_CHEST.get()), null));
	public static final RegistryObject<TileEntityType<?>> FAN = TILE_ENTITIES.register("fan", () -> new TileEntityType<>(FanTileEntity::new, Sets.newHashSet(TrapcraftBlocks.FAN.get()), null));
	public static final RegistryObject<TileEntityType<?>> BEAR_TRAP = TILE_ENTITIES.register("bear_trap", () -> new TileEntityType<>(BearTrapTileEntity::new, Sets.newHashSet(TrapcraftBlocks.BEAR_TRAP.get()), null));
	public static final RegistryObject<TileEntityType<?>> IGNITER = TILE_ENTITIES.register("igniter", () -> new TileEntityType<>(IgniterTileEntity::new, Sets.newHashSet(TrapcraftBlocks.IGNITER.get()), null));
}