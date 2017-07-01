package trapcraft.tileentity;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import trapcraft.block.BlockFan;

public class TileEntityFan extends TileEntity implements ITickable
{
    public double direction;
    public boolean mode;
    public float speed = 1.0F;
    public double extraRange = 0.0D;

    public TileEntityFan()
    {
        if (direction != 1.0D && direction != -1D)
        {
            direction = 1.0D;
        }

        mode = true;
    }

    @Override
    public void update() {

        if(this.worldObj.isBlockIndirectlyGettingPowered(this.pos) == 0)
            return;
        
        if(this.worldObj.rand.nextInt(2) == 0)
        	this.spawnParticles(this.worldObj, this.pos);
        
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(null, getDirection());

        if (!list.isEmpty())
        {
            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);
                int j = getBlockMetadata();
                double d = 0.050000000000000003D;
                double d1 = 0.29999999999999999D;
                d *= direction;
                d *= speed;

                if (entity instanceof EntityItem)
                {
                    d1 *= 1.8D;
                    d *= 1.3D;
                }

                if (!(entity instanceof EntityItem) && !mode)
                {
                    d = 0.0D;
                }

                if (entity instanceof EntityMinecart)
                {
                    d *= 0.5D;
                }

                if ((entity instanceof EntityFallingBlock) && j == 1)
                {
                    d = 0.0D;
                }

                if (!isPathClear(entity, j))
                {
                    continue;
                }

                if (j == 0 && entity.motionY > -d1)
                {
                    entity.motionY += -d;
                }

                if (j == 1 && entity.motionY < d1 * 0.5D)
                {
                    entity.motionY += d;
                }

                if (j == 2 && entity.motionZ > -d1)
                {
                    entity.motionZ += -d;
                }

                if (j == 3 && entity.motionZ < d1)
                {
                    entity.motionZ += d;
                }

                if (j == 4 && entity.motionX > -d1)
                {
                    entity.motionX += -d;
                }

                if (j == 5 && entity.motionX < d1)
                {
                    entity.motionX += d;
                }
            }
        }
    }

    public boolean isPathClear(Entity entity, int i)
    {
        int j = 0;

        if (i == 0 || i == 1)
        {
            int k = MathHelper.floor_double(entity.posY);
            int j1 = k - this.pos.getY();
            j = j1 <= 0 ? -j1 : j1;
        }

        if (i == 2 || i == 3)
        {
            int l = MathHelper.floor_double(entity.posZ);
            int k1 = l - this.pos.getZ();
            j = k1 <= 0 ? -k1 : k1;
        }

        if (i == 4 || i == 5)
        {
            int i1 = MathHelper.floor_double(entity.posX);
            int l1 = i1 - this.pos.getX();
            j = l1 <= 0 ? -l1 : l1;
        }

        World world = this.worldObj;
        int i2 = this.pos.getX();
        int j2 = this.pos.getY();
        int k2 = this.pos.getZ();
        boolean flag = true;

        for (int l2 = 0; l2 < j; l2++)
        {
            if (i == 0)
            {
                j2--;
            }

            if (i == 1)
            {
                j2++;
            }

            if (i == 2)
            {
                k2--;
            }

            if (i == 3)
            {
                k2++;
            }

            if (i == 4)
            {
                i2--;
            }

            if (i == 5)
            {
                i2++;
            }

            if (world.isBlockFullCube(new BlockPos(i2, j2, k2)))
            {
                flag = false;
            }
        }

        return flag;
    }

    public String getSliderDisplay()
    {
        float f = speed;
        f *= 100F;
        f = (float)Math.round(f) / 100F;
        return String.valueOf(f);
    }

    public AxisAlignedBB getDirection() {
    	EnumFacing facing = this.worldObj.getBlockState(this.pos).getValue(BlockFan.FACING);
        
        BlockPos endPos = this.pos.offset(facing, MathHelper.floor_double(5 + this.extraRange));

        return new AxisAlignedBB(this.pos, endPos.add(1, 1, 1));
    }

    public static void spawnParticles(World world, BlockPos pos) {
    	double var7 = (double)((float)pos.getX() + world.rand.nextFloat());
        double var9 = (double)((float)pos.getY() + world.rand.nextFloat());
        double var11 = (double)((float)pos.getZ() + world.rand.nextFloat());
        double velX = 0.0D;
        double velY = 0.0D;
        double velZ = 0.0D;

        EnumFacing facing = world.getBlockState(pos).getValue(BlockFan.FACING);
        
        switch(facing) {
        case DOWN:
        	velY = -(double)(world.rand.nextFloat() * 0.6F);
        	break;
        case UP:
        	velY = (double)(world.rand.nextFloat() * 0.6F);
        	break;
        case NORTH:
        	velZ = -(double)(world.rand.nextFloat() * 0.6F);
        	break;
        case SOUTH:
        	velZ = (double)(world.rand.nextFloat() * 0.6F);
        	break;
        case WEST:
        	velX = -(double)(world.rand.nextFloat() * 0.6F);
        	break;
        case EAST:
        	velX = (double)(world.rand.nextFloat() * 0.6F);
        	break;
        }
        
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7, var9, var11, velX, velY, velZ);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
    	super.readFromNBT(compound);
    	this.speed = compound.getFloat("speed");
    	this.extraRange = compound.getDouble("extraRange");
    	this.direction = compound.getDouble("direction");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	super.writeToNBT(compound);
    	compound.setFloat("speed", this.speed);
    	compound.setDouble("extraRange", this.extraRange);
    	compound.setDouble("direction", this.direction);
        
        return compound;
    }
 
}
