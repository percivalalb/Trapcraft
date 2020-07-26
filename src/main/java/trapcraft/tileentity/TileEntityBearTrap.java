package trapcraft.tileentity;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;


public class TileEntityBearTrap extends TileEntity implements ITickable {
    
	private DamageSource damageSource = new DamageSource("trapcraft.bear_trap").setDamageBypassesArmor();
	@Nullable
	private EntityLiving entityliving;
	private EntityAIBase doNothingGoal;
	private UUID id;
	private int nextDamageTick;
	
    public TileEntityBearTrap() {
		super();
	}
    
    @Override
    public void update() {
    	EntityLiving trapped = this.getTrappedEntity();
    	
    	if(!this.world.isRemote) {
	    	if(trapped != null) {
	    		// Has escaped
	    		if(!trapped.getEntityBoundingBox().intersects(new AxisAlignedBB(this.pos)) || !trapped.isEntityAlive()) {
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
    
    class DoNothingGoal extends EntityAIBase {
    	private EntityLiving trappedEntity;
    	private TileEntityBearTrap trap;
        public DoNothingGoal(EntityLiving trappedEntity, TileEntityBearTrap trap) {
        	this.setMutexBits(3);
        	this.trappedEntity = trappedEntity;
        	this.trap = trap;
        }

        @Override
        public boolean shouldExecute() {
        	return this.trap.isEntityTrapped(this.trappedEntity);
        }
     }
    
    
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
    	super.readFromNBT(compound);
    	if(compound.hasUniqueId("trapped_entity")) {
    		this.id = compound.getUniqueId("trapped_entity");
    	}
    }


	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	super.writeToNBT(compound);
    	if(this.entityliving != null && this.entityliving.isEntityAlive()) {
    		compound.setUniqueId("trapped_entity", this.entityliving.getUniqueID());
    	}
    	return compound;
    }
    
    public boolean setTrappedEntity(@Nullable EntityLiving livingEntity) {
    	if(this.hasTrappedEntity() && livingEntity != null) {
    		return false;
    	} else {
	    	
	    	if(livingEntity == null) {
	        	if(this.entityliving != null) {
	    			this.entityliving.tasks.removeTask(this.doNothingGoal);
	    		}
	    		this.id = null;
	    		this.doNothingGoal = null;
	    		this.nextDamageTick = 0;
	    	} else {
	    		livingEntity.tasks.taskEntries.stream().filter(task -> task.using).forEach(task -> task.action.resetTask());
	    		livingEntity.tasks.addTask(0, this.doNothingGoal = new DoNothingGoal(livingEntity, this));
	    	}
	    	
	    	this.entityliving = livingEntity;
	    	return true;
    	}
    }
    
    public EntityLiving getTrappedEntity() {
    	if(this.id != null && this.world instanceof WorldServer) {
    		Entity entity = ((WorldServer)this.world).getEntityFromUuid(this.id);
    		this.id = null;
    		if(entity instanceof EntityLiving)
    			this.setTrappedEntity((EntityLiving)entity);
    	}
    	return this.entityliving;
    }
    
    public boolean hasTrappedEntity() {
		return this.getTrappedEntity() != null;
	}
    
    public boolean isEntityTrapped(EntityLiving trappedEntity) {
		return this.getTrappedEntity() == trappedEntity;
	}
}
