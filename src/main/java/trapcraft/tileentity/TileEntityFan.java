package trapcraft.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import trapcraft.TrapcraftTileEntityTypes;
import trapcraft.api.ConfigValues;
import trapcraft.block.BlockFan;

public class TileEntityFan extends TileEntity implements ITickableTileEntity
{
    public float speed = 1.0F;
    public double extraRange = 0.0D;

    public TileEntityFan() {
  		super(TrapcraftTileEntityTypes.FAN.get());
  	}

    @Override
    public void tick() {

        if(!this.world.getBlockState(this.pos).get(BlockFan.POWERED))
            return;

        Direction facing = this.world.getBlockState(this.pos).get(BlockFan.FACING);

        if(this.world.rand.nextInt(2) == 0)
        	spawnParticles(this.world, this.pos);
        List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, this.getDirection());

        for(Entity entity : list) {

        	if(!this.isPathClear(entity, facing))
        		continue;


        	double velocity = ConfigValues.FAN_ACCELERATION; // Affects acceleration
        	double threshholdVelocity = ConfigValues.FAN_MAX_SPEED; // Affects max speed
        	velocity *= this.speed;

        	if(entity instanceof ItemEntity) {
        		threshholdVelocity *= 1.8D;
        		velocity *= 1.3D;
        	}

        	if(entity instanceof PlayerEntity) {
        		if(((PlayerEntity)entity).abilities.isFlying)
        			continue;
        	}

        	if(entity instanceof MinecartEntity)
        		velocity *= 0.5D;

        	if((entity instanceof FallingBlockEntity) && facing == Direction.UP)
        		velocity = 0.0D;


        	if(facing == Direction.UP) {
        		threshholdVelocity *= 0.5D;
        	}

        	if(Math.abs(entity.getMotion().getCoordinate(facing.getAxis())) < threshholdVelocity)
        		entity.setMotion(entity.getMotion().add(facing.getXOffset() * velocity, facing.getYOffset() * velocity, facing.getZOffset() * velocity));

        }
    }

    public boolean isPathClear(Entity entity, Direction facing) {
    	int x = facing.getXOffset() * (MathHelper.floor(entity.getPosX()) - this.pos.getX());
    	int y = facing.getYOffset() * (MathHelper.floor(entity.getPosY()) - this.pos.getY());
    	int z = facing.getZOffset() * (MathHelper.floor(entity.getPosZ()) - this.pos.getZ());
    	boolean flag = true;

        for(int l2 = 1; l2 < Math.abs(x + y + z); l2++) {

            if(Block.hasEnoughSolidSide(this.world, this.pos.offset(facing, l2), facing.getOpposite())) {
                flag = false;
            }
        }

        return flag;
    }

    public String getSliderDisplay()
    {
        float f = speed;
        f *= 100F;
        f = Math.round(f) / 100F;
        return String.valueOf(f);
    }

    public AxisAlignedBB getDirection() {
    	Direction facing = this.world.getBlockState(this.pos).get(BlockFan.FACING);

        BlockPos endPos = this.pos.offset(facing, MathHelper.floor(ConfigValues.FAN_RANGE + this.extraRange));
        if(facing == Direction.WEST)
        	endPos = endPos.add(0, 1, 1);
        else if(facing == Direction.NORTH)
        	endPos = endPos.add(1, 1, 0);

        if(facing == Direction.EAST)
        	endPos = endPos.add(1, 1, 1);
        else if(facing == Direction.SOUTH)
        	endPos = endPos.add(1, 1, 1);

        if(facing == Direction.UP)
        	endPos = endPos.add(1, 1, 1);
        else if(facing == Direction.DOWN)
        	endPos = endPos.add(1, 0, 1);

        return new AxisAlignedBB(this.pos, endPos);
    }

    public static void spawnParticles(World world, BlockPos pos) {
    	double x = pos.getX() + world.rand.nextFloat();
        double y = pos.getY() + world.rand.nextFloat();
        double z = pos.getZ() + world.rand.nextFloat();

        Direction facing = world.getBlockState(pos).get(BlockFan.FACING);
        double velocity = 0.2F + world.rand.nextFloat() * 0.4F;

        double velX = facing.getXOffset() * velocity;
        double velY = facing.getYOffset() * velocity;
        double velZ = facing.getZOffset() * velocity;

        world.addParticle(ParticleTypes.SMOKE, x, y, z, velX, velY, velZ);
    }

    @Override
    public void read(CompoundNBT compound) {
    	super.read(compound);
    	this.speed = compound.getFloat("speed");
    	this.extraRange = compound.getDouble("extraRange");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
    	super.write(compound);
    	compound.putFloat("speed", this.speed);
    	compound.putDouble("extraRange", this.extraRange);

        return compound;
    }

    @Override
    public CompoundNBT getUpdateTag() {
		CompoundNBT tag = new CompoundNBT();
        return this.write(tag);
    }

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(getPos(), 0, this.write(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		super.onDataPacket(net, packet);
		this.read(packet.getNbtCompound());
		if(!this.world.isRemote)
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
		return;
	}

}
