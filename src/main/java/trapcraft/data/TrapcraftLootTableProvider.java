package trapcraft.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import trapcraft.TrapcraftBlocks;
import trapcraft.TrapcraftEntityTypes;

import javax.annotation.Nonnull;

public class TrapcraftLootTableProvider extends LootTableProvider {

    public TrapcraftLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Trapcraft LootTables";
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(Blocks::new, LootParameterSets.BLOCK),
                Pair.of(Entities::new, LootParameterSets.ENTITY)
               );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationTracker) {}

    private static class Blocks extends BlockLootTables {

        @Override
        protected void addTables() {
            dropsSelf(TrapcraftBlocks.FAN);
            dropsSelf(TrapcraftBlocks.MAGNETIC_CHEST);
            dropsSelf(TrapcraftBlocks.BEAR_TRAP);
            dropsSelf(TrapcraftBlocks.SPIKES);
            dropsSelf(TrapcraftBlocks.IGNITER);
            droppingWithSilkTouchOrItemInRange(TrapcraftBlocks.GRASS_COVERING, Items.STICK.delegate, RandomValueRange.between(1.0F, 3.0F));
        }

        private void dropsSelf(Supplier<? extends Block> block) {
            dropSelf(block.get());
        }

        private void droppingWithSilkTouchOrItemInRange(Supplier<? extends Block> block, Supplier<? extends IItemProvider> dropItem, IRandomRange range) {
            add(block.get(), (b) -> {
                return createSingleItemTableWithSilkTouch(b, dropItem.get(), range);
            });
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return TrapcraftBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }

    private static class Entities extends EntityLootTables {

        @Override
        protected void addTables() {
            this.registerLootTable(TrapcraftEntityTypes.DUMMY, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(Items.SHULKER_SHELL)).when(RandomChanceWithLooting.randomChanceAndLootingBoost(0.5F, 0.0625F))));
        }

        protected void registerLootTable(Supplier<? extends EntityType<?>> type, final LootTable.Builder table) {
           this.add(type.get().getDefaultLootTable(), table);
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return TrapcraftEntityTypes.ENTITIES.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }
}
