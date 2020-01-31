package trapcraft;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
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

	public static final RegistryObject<Block> FAN = makeBlockWithItem("fan", () -> new BlockFan());
    private static final Pair<RegistryObject<Block>, RegistryObject<Item>> MAGNETIC_CHEST_PAIR = makeBlockWithItemCustom("magnetic_chest", () -> new BlockMagneticChest(), (blockObj) -> makeMagneticChestItem(blockObj));
    public static final RegistryObject<Block> MAGNETIC_CHEST = MAGNETIC_CHEST_PAIR.getLeft();
    public static final RegistryObject<Item> MAGNETIC_CHEST_ITEM = MAGNETIC_CHEST_PAIR.getRight();
    public static final RegistryObject<Block> GRASS_COVERING = makeBlockWithItem("grass_covering", () -> new BlockGrassCovering());
    public static final RegistryObject<Block> BEAR_TRAP = makeBlockWithItem("bear_trap", () -> new BlockBearTrap());
    public static final RegistryObject<Block> SPIKES = makeBlockWithItem("spikes", () -> new BlockSpikes());
    public static final RegistryObject<Block> IGNITER = makeBlockWithItem("igniter", () -> new BlockIgniter());

    private static RegistryObject<Block> makeBlockWithItem(final String name, final Supplier<Block> blockSupplier) {
        return makeBlockWithItemCustom(name, blockSupplier, (blockObj) -> makeBlockItem(blockObj)).getLeft();
    }

    private static Pair<RegistryObject<Block>, RegistryObject<Item>> makeBlockWithItemCustom(final String name, final Supplier<Block> blockSupplier, final Function<RegistryObject<Block>, Item> itemFunction) {
        RegistryObject<Block> blockObj = BLOCKS.register(name, blockSupplier);
        RegistryObject<Item> itemObj   = ITEMS.register(name, () -> itemFunction.apply(blockObj));
        return Pair.of(blockObj, itemObj);
    }

    private static BlockItem makeMagneticChestItem(Supplier<Block> blockSupplier) {
        return makeBlockItemWithISTER(blockSupplier, ItemGroup.REDSTONE, TileEntityItemStackMagneticChestRenderer::new);
    }

    private static BlockItem makeBlockItem(Supplier<Block> blockSupplier) {
        return makeBlockItem(blockSupplier, ItemGroup.REDSTONE);
    }

    private static BlockItem makeBlockItem(Supplier<Block> block, ItemGroup group) {
        return new BlockItem(block.get(), new Item.Properties().group(group));
    }

    private static BlockItem makeBlockItemWithISTER(Supplier<Block> block, ItemGroup group, Callable<ItemStackTileEntityRenderer> ister) {
        return new BlockItem(block.get(), new Item.Properties().group(group).setISTER(() -> ister));
    }
}