package trapcraft.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import trapcraft.ModEntities;
import trapcraft.TrapcraftMod;

/**
 * @author ProPercivalalb
 **/
public class EntityDummy extends EntityLiving {
	
	private static final DataParameter<Byte> VARIANT = EntityDataManager.<Byte>createKey(EntityDummy.class, DataSerializers.BYTE);
   
    public EntityDummy(World world) {
        super(ModEntities.DUMMY, world);
        this.setNoAI(true);
    }

    @Override
	protected void registerAttributes() {
	    super.registerAttributes();
	    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
	    this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
	    this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, Byte.valueOf((byte)0));
    }
    
    @Override
    protected void jump() {}
    
    @Override
    public void livingTick() {
        this.randomYawVelocity = 0.0F;
        this.motionX = 0.0D;
        this.motionZ = 0.0D;
        List<Entity> nearByEntities = this.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(this.getPosition()).expand(16D, 16D, 16D));

        for(int i = 0; i < nearByEntities.size(); i++) {
        	Entity entity = nearByEntities.get(0);
        	
        	if(entity instanceof EntityMob) {
            	EntityMob mob = (EntityMob)entity;

            	if(this.canEntityBeSeen(mob)) {
            		mob.setRevengeTarget(this);
            		mob.setAttackTarget(this);
            	}

            	continue;
        	}

        	if(entity instanceof EntitySlime) {
        		EntitySlime slime = (EntitySlime)entity;

        		if(this.canEntityBeSeen(slime)) {
        			slime.setRevengeTarget(this);
        			slime.setAttackTarget(this);
        		}

        		continue;
        	}

        	if(!(entity instanceof EntityWolf)) {
        		continue;
        	}

        	EntityWolf wolf = (EntityWolf)entity;

        	if(this.canEntityBeSeen(wolf) && wolf.isAngry()) {
        		wolf.setRevengeTarget(this);
        		wolf.setAttackTarget(this);
        	}
        }

        super.livingTick();
    }

    public Entity getClosestEntity(World world, double d, double d1, double d2, double d3) {
        double d4 = -1D;
        Entity entity = null;

        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity1 = (Entity)world.loadedEntityList.get(i);
            double d5 = entity1.getDistanceSq(d, d1, d2);

            if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1D || d5 < d4) && (entity1 instanceof EntityMob)) {
                d4 = d5;
                entity = entity1;
            }
        }

        return entity;
    }

    @Override
    public void onDeath(DamageSource damagesource) {
        super.onDeath(damagesource);
        if(!this.world.isRemote)
        	this.entityDropItem(Items.PLAYER_HEAD);
    }

    @Override
    public void writeAdditional(NBTTagCompound compound) {
        super.writeAdditional(compound);
        compound.putByte("variant", this.getVariant());
    }

    @Override
    public void readAdditional(NBTTagCompound compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getByte("variant"));
    }
    

    public void setVariant(byte index) {
    	this.dataManager.set(VARIANT, index);
    }
    
    public byte getVariant() {
    	return this.dataManager.get(VARIANT);
    }
}
