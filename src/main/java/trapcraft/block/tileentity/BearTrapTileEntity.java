package trapcraft.block.tileentity;

import java.util.EnumSet;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;
import trapcraft.TrapcraftTileEntityTypes;

public class BearTrapTileEntity extends TileEntity implements ITickableTileEntity {

	private final DamageSource damageSource = new DamageSource("trapcraft.bear_trap").setDamageBypassesArmor();
	@Nullable
	private MobEntity entityliving;
	private Goal doNothingGoal;
	private UUID id;
	private int nextDamageTick;

    public BearTrapTileEntity() {
		super(TrapcraftTileEntityTypes.BEAR_TRAP.get());
	}

    @Override
    public void tick() {
    	final MobEntity trapped = this.getTrappedEntity();

    	if(!this.world.isRemote) {
	    	if(trapped != null) {
	    		// Has escaped
	    		if(!trapped.getBoundingBox().intersects(new AxisAlignedBB(this.pos)) || !trapped.isAlive()) {
	    			this.setTrappedEntity(null);

	    		} else  {
	    			if(this.nextDamageTick == 0) {
	    				trapped.attackEntityFrom(damageSource, 1);
	    				this.nextDamageTick = 15 + this.world.rand.nextInt(20);
	    			}

	    			if(this.nextDamageTick > 0) {
	    				this.nextDamageTick--;
	    			}
	    		}
	    	}
    	}
    }

    class DoNothingGoal extends Goal {
    	private MobEntity trappedEntity;
    	private BearTrapTileEntity trap;
        public DoNothingGoal(MobEntity trappedEntity, BearTrapTileEntity trap) {
        	this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
        	this.trappedEntity = trappedEntity;
        	this.trap = trap;
        }

        @Override
        public boolean shouldExecute() {
        	return this.trap.isEntityTrapped(this.trappedEntity);
        }
     }



    @Override
    public void read(CompoundNBT compound) {
    	super.read(compound);
    	if(compound.hasUniqueId("trapped_entity")) {
    		this.id = compound.getUniqueId("trapped_entity");
    	}
    }


	@Override
    public CompoundNBT write(CompoundNBT compound) {
    	super.write(compound);
    	if(this.entityliving != null && this.entityliving.isAlive()) {
    		compound.putUniqueId("trapped_entity", this.entityliving.getUniqueID());
    	}
    	return compound;
    }

    public boolean setTrappedEntity(@Nullable MobEntity livingEntity) {
    	if(this.hasTrappedEntity() && livingEntity != null) {
    		return false;
    	} else {

	    	if(livingEntity == null) {
	        	if(this.entityliving != null) {
	    			this.entityliving.goalSelector.removeGoal(this.doNothingGoal);
	    		}
	    		this.id = null;
	    		this.doNothingGoal = null;
	    		this.nextDamageTick = 0;
	    	} else {
	    		livingEntity.goalSelector.getRunningGoals().filter(PrioritizedGoal::isRunning).forEach(PrioritizedGoal::resetTask);
	    		livingEntity.goalSelector.addGoal(0, this.doNothingGoal = new DoNothingGoal(livingEntity, this));
	    	}

	    	this.entityliving = livingEntity;
	    	return true;
    	}
    }

    public MobEntity getTrappedEntity() {
    	if(this.id != null && this.world instanceof ServerWorld) {
    		Entity entity = ((ServerWorld)this.world).getEntityByUuid(this.id);
    		this.id = null;
    		if(entity instanceof MobEntity)
    			this.setTrappedEntity((MobEntity)entity);
    	}
    	return this.entityliving;
    }

    public boolean hasTrappedEntity() {
		return this.getTrappedEntity() != null;
	}

    public boolean isEntityTrapped(final MobEntity trappedEntity) {
		return this.getTrappedEntity() == trappedEntity;
	}
}
