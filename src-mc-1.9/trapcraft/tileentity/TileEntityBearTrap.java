package trapcraft.tileentity;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class TileEntityBearTrap extends TileEntity {
    
	public EntityLiving entityliving;
    public float prevHealth;
    public float moveSpeed;
    public double posX;
    public double posY;
    public double posZ;
    public Random rand;

    public TileEntityBearTrap() {
        rand = new Random();
    }
    
    public void updateEntity() {
        if (entityliving != null) {
            if (entityliving.getDistance((double)xCoord + 0.5D, (double)yCoord + 0.20000000000000001D, (double)zCoord + 0.5D) > 2D) {
                entityliving = null;
                return;
            }

            entityliving.motionZ = 0.0D;
            entityliving.motionX = 0.0D;
            entityliving.setAIMoveSpeed(0.0F);
            entityliving.motionY = -0.01D;

            if (prevHealth > entityliving.getHealth()) {
            	entityliving.setAIMoveSpeed(this.moveSpeed);
                entityliving = null;
                return;
            }

            if (rand.nextInt(30) == 0) {
                entityliving.attackEntityFrom(DamageSource.inWall, 1);
            }

            prevHealth = entityliving.getHealth();
        }
    }
}
