package trapcraft.common.entity;

import java.util.List;

import trapcraft.TrapcraftMod;
import trapcraft.api.Properties;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class EntityDummy extends EntityLiving {
   
	public int defaultRotation;
    public int myHealth;

    public EntityDummy(World world) {
        super(world);
        this.defaultRotation = 0;
    }

    @Override
	protected void applyEntityAttributes() {
	    super.applyEntityAttributes();
	    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
	    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);
	    this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
	}

    @Override
    protected void jump() {
    
    }
    
    @Override
    public void onLivingUpdate() {
        randomYawVelocity = 0.0F;
        motionX = 0.0D;
        motionZ = 0.0D;
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 16D, 16D));

        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if ((Entity)list.get(i) instanceof EntityMob) {
                    EntityMob entitymob = (EntityMob)list.get(i);

                    if (canEntityBeSeen(entitymob)) {
                        entitymob.setTarget(this);
                        entitymob.setRevengeTarget(this);
                        entitymob.setAttackTarget(this);
                    }

                    continue;
                }

                if ((Entity)list.get(i) instanceof EntitySlime) {
                    EntitySlime entityslime = (EntitySlime)list.get(i);

                    if (canEntityBeSeen(entityslime)) {
                        entityslime.setRevengeTarget(this);
                        entityslime.setAttackTarget(this);
                    }

                    continue;
                }

                if (!((Entity)list.get(i) instanceof EntityWolf)) {
                    continue;
                }

                EntityWolf entitywolf = (EntityWolf)list.get(i);

                if (canEntityBeSeen(entitywolf) && entitywolf.isAngry()) {
                	entitywolf.setTarget(this);
                    entitywolf.setRevengeTarget(this);
                    entitywolf.setAttackTarget(this);
                }
            }
        }

        super.onLivingUpdate();
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
    protected void updateEntityActionState() {}

    @Override
    public void onDeath(DamageSource damagesource) {
        super.onDeath(damagesource);
        if(!this.worldObj.isRemote) {
        	this.entityDropItem(new ItemStack(Items.skull, 1, 3), 0.0F);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
    }
}
