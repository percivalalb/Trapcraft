package trapcraft.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
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
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(
                Pair.of(Blocks::new, LootContextParamSets.BLOCK),
                Pair.of(Entities::new, LootContextParamSets.ENTITY)
               );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {}

    private static class Blocks extends BlockLoot {

        @Override
        protected void addTables() {
            dropsSelf(TrapcraftBlocks.FAN);
            dropsSelf(TrapcraftBlocks.MAGNETIC_CHEST);
            dropsSelf(TrapcraftBlocks.BEAR_TRAP);
            dropsSelf(TrapcraftBlocks.SPIKES);
            dropsSelf(TrapcraftBlocks.IGNITER);
            droppingWithSilkTouchOrItemInRange(TrapcraftBlocks.GRASS_COVERING, () -> Items.STICK, UniformGenerator.between(1.0F, 3.0F));
        }

        private void dropsSelf(Supplier<? extends Block> block) {
            dropSelf(block.get());
        }

        private void droppingWithSilkTouchOrItemInRange(Supplier<? extends Block> block, Supplier<? extends ItemLike> dropItem, NumberProvider range) {
            add(block.get(), (b) -> {
                return createSingleItemTableWithSilkTouch(b, dropItem.get(), range);
            });
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return TrapcraftBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }

    private static class Entities extends EntityLoot {

        @Override
        protected void addTables() {
            this.registerLootTable(TrapcraftEntityTypes.DUMMY, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.SHULKER_SHELL)).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.0625F))));
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
