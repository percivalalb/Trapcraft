package trapcraft;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;
import trapcraft.api.Constants;
import trapcraft.block.*;
import trapcraft.item.MagneticChestItem;

import java.util.function.Function;
import java.util.function.Supplier;

public class TrapcraftBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = TrapcraftItems.ITEMS;

    public static final RegistryObject<Block> FAN = makeBlockWithItem("fan", FanBlock::new);
    private static final Pair<RegistryObject<Block>, RegistryObject<Item>> MAGNETIC_CHEST_PAIR = makeBlockWithItemCustom("magnetic_chest", () -> new MagneticChestBlock(), (blockObj) -> makeMagneticChestItem(blockObj));
    public static final RegistryObject<Block> MAGNETIC_CHEST = MAGNETIC_CHEST_PAIR.getLeft();
    public static final RegistryObject<Item> MAGNETIC_CHEST_ITEM = MAGNETIC_CHEST_PAIR.getRight();
    public static final RegistryObject<Block> GRASS_COVERING = makeBlockWithItem("grass_covering", GrassCoveringBlock::new);
    public static final RegistryObject<Block> BEAR_TRAP = makeBlockWithItem("bear_trap", BearTrapBlock::new);
    public static final RegistryObject<Block> SPIKES = makeBlockWithItem("spikes", SpikesBlock::new);
    public static final RegistryObject<Block> IGNITER = makeBlockWithItem("igniter", IgniterBlock::new);

    private static RegistryObject<Block> makeBlockWithItem(final String name, final Supplier<Block> blockSupplier) {
        return makeBlockWithItemCustom(name, blockSupplier, (blockObj) -> makeBlockItem(blockObj)).getLeft();
    }

    private static Pair<RegistryObject<Block>, RegistryObject<Item>> makeBlockWithItemCustom(final String name, final Supplier<Block> blockSupplier, final Function<RegistryObject<Block>, Item> itemFunction) {
        RegistryObject<Block> blockObj = BLOCKS.register(name, blockSupplier);
        RegistryObject<Item> itemObj   = ITEMS.register(name, () -> itemFunction.apply(blockObj));
        return Pair.of(blockObj, itemObj);
    }

    private static BlockItem makeMagneticChestItem(Supplier<Block> blockSupplier) {
        return new MagneticChestItem(blockSupplier.get(), new Item.Properties());// TODO .setISTER(() -> ItemStackTileEntityMagneticChestRenderer::new));
    }

    private static BlockItem makeBlockItem(Supplier<Block> block) {
        return new BlockItem(block.get(), new Item.Properties());
    }
}
