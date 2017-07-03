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
import net.minecraftforge.fml.common.FMLLog;
import trapcraft.block.BlockFan;

public class TileEntityFan extends TileEntity implements ITickable
{
    public double direction;
    public boolean mode;
    public float speed = 1.0F;
    public double extraRange = 0.0D;

    public TileEntityFan()
    {
        
    }

    @Override
    public void update() {

        if(!(this.worldObj.isBlockPowered(this.pos) || this.worldObj.isBlockPowered(this.pos.up())))
            return;
        
        EnumFacing facing = this.worldObj.getBlockState(this.pos).getValue(BlockFan.FACING);
        
        if(this.worldObj.rand.nextInt(2) == 0)
        	this.spawnParticles(this.worldObj, this.pos);
        
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, this.getDirection());

        if(!list.isEmpty())
        {
            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);
            	FMLLog.info("%s", entity.toString());
                double d = 0.050000000000000003D;
                double d1 = 0.29999999999999999D;
                d *= speed;

                if (entity instanceof EntityItem)
                {
                    d1 *= 1.8D;
                    d *= 1.3D;
                }

                //Item mode only
                //if (!(entity instanceof EntityItem))
               // {
                 //   d = 0.0D;
                //}

                if (entity instanceof EntityMinecart)
                {
                    d *= 0.5D;
                }

                if ((entity instanceof EntityFallingBlock) && facing == EnumFacing.UP)
                {
                    d = 0.0D;
                }

                if (!this.isPathClear(entity, facing))
                {
                    continue;
                }
                
                

                if(facing == EnumFacing.DOWN && entity.motionY > -d1)
                {
                    entity.motionY += -d;
                }

                if (facing == EnumFacing.UP && entity.motionY < d1 * 0.5D)
                {
                    entity.motionY += d;
                }

                if (facing == EnumFacing.NORTH && entity.motionZ > -d1)
                {
                    entity.motionZ += -d;
                }

                if (facing == EnumFacing.SOUTH && entity.motionZ < d1)
                {
                    entity.motionZ += d;
                }

                if (facing == EnumFacing.WEST && entity.motionX > -d1)
                {
                    entity.motionX += -d;
                }

                if (facing == EnumFacing.EAST && entity.motionX < d1)
                {
                    entity.motionX += d;
                }
            }
        }
    }

    public boolean isPathClear(Entity entity, EnumFacing facing) {
    	int x = facing.getFrontOffsetX() * (MathHelper.floor_double(entity.posX) - this.pos.getX());
    	int y = facing.getFrontOffsetY() * (MathHelper.floor_double(entity.posY) - this.pos.getY());
    	int z = facing.getFrontOffsetZ() * (MathHelper.floor_double(entity.posZ) - this.pos.getZ());
    	boolean flag = true;
    	
        for(int l2 = 1; l2 < Math.abs(x + y + z); l2++) {
            
            if(this.worldObj.isBlockFullCube(this.pos.offset(facing, l2))) {
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
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	super.writeToNBT(compound);
    	compound.setFloat("speed", this.speed);
    	compound.setDouble("extraRange", this.extraRange);
        
        return compound;
    }
 
}
