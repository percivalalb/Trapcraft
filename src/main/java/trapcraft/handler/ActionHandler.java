package trapcraft.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import trapcraft.entity.DummyEntity;

/**
 * @author ProPercivalalb
 **/
public class ActionHandler {

    public final static List<Block> PLANKS = Collections.<Block>unmodifiableList(Arrays.asList(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS));

    @SubscribeEvent
    public void action(final EntityPlaceEvent event) {
        final Level world = (Level) event.getBlockSnapshot().getLevel();
        final BlockState state = event.getPlacedBlock();
        final BlockPos tPos = event.getBlockSnapshot().getPos();

        if (state.getBlock() == Blocks.PLAYER_HEAD) {
            final Block top = world.getBlockState(tPos.below()).getBlock();
            final Block bottom = world.getBlockState(tPos.below(2)).getBlock();
            if (top == bottom && PLANKS.contains(top)) {
                world.setBlockAndUpdate(tPos, Blocks.AIR.defaultBlockState());
                world.setBlockAndUpdate(tPos.below(), Blocks.AIR.defaultBlockState());
                world.setBlockAndUpdate(tPos.below(2), Blocks.AIR.defaultBlockState());

                if (!world.isClientSide) {
                    final float rotation = event.getEntity().getYRot() + 180F;
                    this.spawnDummy(world, tPos, rotation, (byte)PLANKS.indexOf(top));
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(final EntityJoinWorldEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof Mob) {
            final Mob mob = (Mob)entity;
            mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, DummyEntity.class, 10, true, false, (dummy) -> {
              return Math.abs(dummy.getY() - mob.getY()) <= 6.0D;
           }));
        }
    }

    public void spawnDummy(final Level world, final BlockPos tPos, final float rotation, final byte variant) {
        final DummyEntity entitydummy = new DummyEntity(world);
        entitydummy.setVariant(variant);
        entitydummy.moveTo(tPos.getX() + 0.5D, tPos.getY() - 1.95D, tPos.getZ() + 0.5D, Mth.wrapDegrees(rotation), 0.0F);
        world.addFreshEntity(entitydummy);
    }
}
