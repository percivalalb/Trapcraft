package trapcraft.tileentity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import trapcraft.block.BlockFan;

public class TileEntityFan extends TileEntity implements ITickable
{
    public float speed = 1.0F;
    public double extraRange = 0.0D;

    public TileEntityFan() {
          super();
      }

    @Override
    public void update() {

        if(!this.world.getBlockState(this.pos).getValue(BlockFan.POWERED))
            return;

        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockFan.FACING);

        if(this.world.rand.nextInt(2) == 0)
            spawnParticles(this.world, this.pos);
        List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, this.getEnumFacing());

        for(Entity entity : list) {

            if(!this.isPathClear(entity, facing))
                continue;


            double velocity = 0.05D; // Affects acceleration
            double threshholdVelocity = 0.3D; // Affects max speed
            velocity *= this.speed;

            if(entity instanceof EntityItem) {
                threshholdVelocity *= 1.8D;
                velocity *= 1.3D;
            }

            if(entity instanceof EntityPlayer) {
                if(((EntityPlayer)entity).capabilities.isFlying)
                    continue;
            }

            if(entity instanceof EntityMinecart)
                velocity *= 0.5D;

            if((entity instanceof EntityFallingBlock) && facing == EnumFacing.UP)
                velocity = 0.0D;


            if(facing == EnumFacing.UP) {
                threshholdVelocity *= 0.5D;
            }

            if(facing.getAxis() == Axis.X) {
                if(Math.abs(entity.motionX) < threshholdVelocity) {
                    entity.motionX += facing.getXOffset() * velocity;
                }
            } else if(facing.getAxis() == Axis.Y) {
                if(Math.abs(entity.motionY) < threshholdVelocity) {
                    entity.motionY += facing.getYOffset() * velocity;
                }
            } else if(facing.getAxis() == Axis.Z) {
                if(Math.abs(entity.motionZ) < threshholdVelocity) {
                    entity.motionZ += facing.getZOffset() * velocity;
                }
            }
        }
    }

    public boolean isPathClear(Entity entity, EnumFacing facing) {
        int x = facing.getXOffset() * (MathHelper.floor(entity.posX) - this.pos.getX());
        int y = facing.getYOffset() * (MathHelper.floor(entity.posY) - this.pos.getY());
        int z = facing.getZOffset() * (MathHelper.floor(entity.posZ) - this.pos.getZ());
        boolean flag = true;

        for(int l2 = 1; l2 < Math.abs(x + y + z); l2++) {

            if(this.world.isBlockFullCube(this.pos.offset(facing, l2))) {
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

    public AxisAlignedBB getEnumFacing() {
        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockFan.FACING);

        BlockPos endPos = this.pos.offset(facing, MathHelper.floor(5 + this.extraRange));
        if(facing == EnumFacing.WEST)
            endPos = endPos.add(0, 1, 1);
        else if(facing == EnumFacing.NORTH)
            endPos = endPos.add(1, 1, 0);

        if(facing == EnumFacing.EAST)
            endPos = endPos.add(1, 1, 1);
        else if(facing == EnumFacing.SOUTH)
            endPos = endPos.add(1, 1, 1);

        if(facing == EnumFacing.UP)
            endPos = endPos.add(1, 1, 1);
        else if(facing == EnumFacing.DOWN)
            endPos = endPos.add(1, 0, 1);

        return new AxisAlignedBB(this.pos, endPos);
    }

    public static void spawnParticles(World world, BlockPos pos) {
        double x = (double)((float)pos.getX() + world.rand.nextFloat());
        double y = (double)((float)pos.getY() + world.rand.nextFloat());
        double z = (double)((float)pos.getZ() + world.rand.nextFloat());

        EnumFacing facing = world.getBlockState(pos).getValue(BlockFan.FACING);
        double velocity = 0.2F + world.rand.nextFloat() * 0.4F;

        double velX = facing.getXOffset() * velocity;
        double velY = facing.getYOffset() * velocity;
        double velZ = facing.getZOffset() * velocity;

        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, velX, velY, velZ);
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

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = new NBTTagCompound();
        return this.writeToNBT(tag);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        this.readFromNBT(packet.getNbtCompound());
        if(!this.world.isRemote)
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        return;
    }

}
