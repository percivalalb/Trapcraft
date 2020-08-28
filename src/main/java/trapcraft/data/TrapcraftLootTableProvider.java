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
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.IRandomRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTable.Builder;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.ValidationTracker;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
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
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables() {
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
            droppingWithSilkTouchOrItemInRange(TrapcraftBlocks.GRASS_COVERING, Items.STICK.delegate, RandomValueRange.of(1.0F, 3.0F));
        }

        private void dropsSelf(Supplier<? extends Block> block) {
            registerDropSelfLootTable(block.get());
        }

        private void droppingWithSilkTouchOrItemInRange(Supplier<? extends Block> block, Supplier<? extends IItemProvider> dropItem, IRandomRange range) {
            registerLootTable(block.get(), (b) -> {
                return droppingWithSilkTouchOrRandomly(b, dropItem.get(), range);
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
            this.registerLootTable(TrapcraftEntityTypes.DUMMY, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.SHULKER_SHELL)).acceptCondition(RandomChanceWithLooting.builder(0.5F, 0.0625F))));
        }

        protected void registerLootTable(Supplier<? extends EntityType<?>> type, final LootTable.Builder table) {
           this.registerLootTable(type.get().getLootTable(), table);
        }

		@Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return TrapcraftEntityTypes.ENTITIES.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }
}