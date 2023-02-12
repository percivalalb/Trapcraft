package trapcraft.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import trapcraft.TrapcraftBlocks;
import trapcraft.TrapcraftEntityTypes;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrapcraftLootTableProvider extends LootTableProvider {

    public TrapcraftLootTableProvider(PackOutput packOutput) {
        super(packOutput, BuiltInLootTables.all(), List.of(new LootTableProvider.SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK), new LootTableProvider.SubProviderEntry(Entities::new, LootContextParamSets.ENTITY)));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {}

    private static class Blocks extends BlockLootSubProvider {

        protected Blocks() {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
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

    private static class Entities extends EntityLootSubProvider {

        protected Entities() {
            super(FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        public void generate() {
            this.add(TrapcraftEntityTypes.DUMMY.get(), LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.SHULKER_SHELL)).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.0625F))));
        }


        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return TrapcraftEntityTypes.ENTITIES.getEntries().stream().map(Supplier::get);
        }
    }
}
