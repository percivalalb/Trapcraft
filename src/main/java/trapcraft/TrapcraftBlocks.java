package trapcraft;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;
import trapcraft.block.BlockBearTrap;
import trapcraft.block.BlockFan;
import trapcraft.block.BlockGrassCovering;
import trapcraft.block.BlockIgniter;
import trapcraft.block.BlockMagneticChest;
import trapcraft.block.BlockSpikes;
import trapcraft.client.renders.TileEntityItemStackMagneticChestRenderer;

public class TrapcraftBlocks {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = TrapcraftItems.ITEMS;

	public static final RegistryObject<Block> FAN = BLOCKS.register("fan", () -> new BlockFan());
    public static final RegistryObject<Block> MAGNETIC_CHEST = BLOCKS.register("magnetic_chest", () -> new BlockMagneticChest());
    public static final RegistryObject<Block> GRASS_COVERING = BLOCKS.register("grass_covering", () -> new BlockGrassCovering());
    public static final RegistryObject<Block> BEAR_TRAP = BLOCKS.register("bear_trap", () -> new BlockBearTrap());
    public static final RegistryObject<Block> SPIKES = BLOCKS.register("spikes", () -> new BlockSpikes());
    public static final RegistryObject<Block> IGNITER = BLOCKS.register("igniter", () -> new BlockIgniter());

    private static final RegistryObject<Item> FAN_ITEM = ITEMS.register("fan", () -> makeBlockItem(FAN));
    public static final RegistryObject<Item> MAGNETIC_CHEST_ITEM = ITEMS.register("magnetic_chest", () -> new BlockItem(MAGNETIC_CHEST.get(), new Item.Properties().group(ItemGroup.REDSTONE).setTEISR(() -> TileEntityItemStackMagneticChestRenderer::new)));
    private static final RegistryObject<Item> GRASS_COVERING_ITEM = ITEMS.register("grass_covering", () -> makeBlockItem(GRASS_COVERING));
    private static final RegistryObject<Item> BEAR_TRAP_ITEM = ITEMS.register("bear_trap", () -> makeBlockItem(BEAR_TRAP));
    private static final RegistryObject<Item> SPIKES_ITEM = ITEMS.register("spikes", () -> makeBlockItem(SPIKES));
    private static final RegistryObject<Item> IGNITER_ITEM = ITEMS.register("igniter", () -> makeBlockItem(IGNITER));

    private static BlockItem makeBlockItem(Supplier<Block> fan) {
        return makeBlockItem(fan, ItemGroup.REDSTONE);
    }

    private static BlockItem makeBlockItem(Supplier<Block> block, ItemGroup group) {
        return new BlockItem(block.get(), new Item.Properties().group(group));
    }
}