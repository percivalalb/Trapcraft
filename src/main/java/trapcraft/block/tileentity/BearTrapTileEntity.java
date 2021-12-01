package trapcraft.block.tileentity;

import java.util.EnumSet;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.server.level.ServerLevel;
import trapcraft.TrapcraftTileEntityTypes;

public class BearTrapTileEntity extends BlockEntity {

    private static final DamageSource damageSource = new DamageSource("trapcraft.bear_trap").bypassArmor();
    @Nullable
    private Mob entityliving;
    private Goal doNothingGoal;
    private UUID id;
    private int nextDamageTick;

    public BearTrapTileEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(TrapcraftTileEntityTypes.BEAR_TRAP.get(), p_155229_, p_155230_);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BearTrapTileEntity blockEntity) {
        final Mob trapped = blockEntity.getTrappedEntity();

        if (!level.isClientSide) {
            if (trapped != null) {
                // Has escaped
                if (!trapped.getBoundingBox().intersects(new AABB(blockPos)) || !trapped.isAlive()) {
                    blockEntity.setTrappedEntity(null);

                } else  {
                    if (blockEntity.nextDamageTick == 0) {
                        trapped.hurt(damageSource, 1);
                        blockEntity.nextDamageTick = 15 + level.random.nextInt(20);
                    }

                    if (blockEntity.nextDamageTick > 0) {
                        blockEntity.nextDamageTick--;
                    }
                }
            }
        }
    }

    class DoNothingGoal extends Goal {
        private Mob trappedEntity;
        private BearTrapTileEntity trap;
        public DoNothingGoal(Mob trappedEntity, BearTrapTileEntity trap) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
            this.trappedEntity = trappedEntity;
            this.trap = trap;
        }

        @Override
        public boolean canUse() {
            return this.trap.isEntityTrapped(this.trappedEntity);
        }
     }



    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.hasUUID("trapped_entity")) {
            this.id = nbt.getUUID("trapped_entity");
        }
    }


    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.entityliving != null && this.entityliving.isAlive()) {
            compound.putUUID("trapped_entity", this.entityliving.getUUID());
        }
    }

    public boolean setTrappedEntity(@Nullable Mob livingEntity) {
        if (this.hasTrappedEntity() && livingEntity != null) {
            return false;
        } else {

            if (livingEntity == null) {
                if (this.entityliving != null) {
                    this.entityliving.goalSelector.removeGoal(this.doNothingGoal);
                }
                this.id = null;
                this.doNothingGoal = null;
                this.nextDamageTick = 0;
            } else {
                livingEntity.goalSelector.getRunningGoals().filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);
                livingEntity.goalSelector.addGoal(0, this.doNothingGoal = new DoNothingGoal(livingEntity, this));
            }

            this.entityliving = livingEntity;
            return true;
        }
    }

    public Mob getTrappedEntity() {
        if (this.id != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level).getEntity(this.id);
            this.id = null;
            if (entity instanceof Mob)
                this.setTrappedEntity((Mob)entity);
        }
        return this.entityliving;
    }

    public boolean hasTrappedEntity() {
        return this.getTrappedEntity() != null;
    }

    public boolean isEntityTrapped(final Mob trappedEntity) {
        return this.getTrappedEntity() == trappedEntity;
    }
}
