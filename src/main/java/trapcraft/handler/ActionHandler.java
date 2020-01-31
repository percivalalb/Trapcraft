package trapcraft.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import trapcraft.entity.EntityDummy;

/**
 * @author ProPercivalalb
 **/
public class ActionHandler {

	public final static List<Block> PLANKS = Collections.<Block>unmodifiableList(Arrays.asList(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS));

	@SubscribeEvent
	public void action(final EntityPlaceEvent event) {
		World world = event.getBlockSnapshot().getWorld().getWorld();
		BlockState state = event.getPlacedBlock();
		BlockPos tPos = event.getBlockSnapshot().getPos();

		if(state.getBlock() == Blocks.PLAYER_HEAD) {
			Block top = world.getBlockState(tPos.down()).getBlock();
			Block bottom = world.getBlockState(tPos.down(2)).getBlock();
			if(top == bottom && PLANKS.contains(top)) {
				world.setBlockState(tPos, Blocks.AIR.getDefaultState());
				world.setBlockState(tPos.down(), Blocks.AIR.getDefaultState());
				world.setBlockState(tPos.down(2), Blocks.AIR.getDefaultState());

				if(!world.isRemote) {
					float rotation = event.getEntity().rotationYaw + 180F;
					this.spawnDummy(world, tPos, rotation, (byte)PLANKS.indexOf(top));
				}
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntitySpawn(final EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof MobEntity) {
			MobEntity mob = (MobEntity)entity;
			mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, EntityDummy.class, 10, true, false, (dummy) -> {
	          return Math.abs(dummy.getPosY() - mob.getPosY()) <= 6.0D;
	       }));
		}
	}

	public void spawnDummy(World world, BlockPos tPos, float rotation, byte variant) {
		EntityDummy entitydummy = new EntityDummy(world);
		entitydummy.setVariant(variant);
		entitydummy.setLocationAndAngles(tPos.getX() + 0.5D, tPos.getY() - 1.95D, tPos.getZ() + 0.5D, MathHelper.wrapDegrees(rotation), 0.0F);
		world.addEntity(entitydummy);
	}
}
