package trapcraft.block.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import trapcraft.block.FanBlock;

import javax.annotation.Nonnull;

public class FanTileEntity extends TileEntity implements ITickableTileEntity
{
    public float speed = 1.0F;
    public double extraRange = 0.0D;

    public FanTileEntity() {
          super(TrapcraftTileEntityTypes.FAN.get());
      }

    @Override
    public void tick() {

        if (!this.level.getBlockState(this.worldPosition).getValue(FanBlock.POWERED))
            return;

        final Direction facing = this.level.getBlockState(this.worldPosition).getValue(FanBlock.FACING);

        if (this.level.random.nextInt(2) == 0)
            spawnParticles(this.level, this.worldPosition);
        final List<Entity> list = this.level.getEntitiesOfClass(Entity.class, this.getDirection());

        for (final Entity entity : list) {

            if (!this.isPathClear(entity, facing))
                continue;


            double velocity = ConfigValues.FAN_ACCELERATION; // Affects acceleration
            double threshholdVelocity = ConfigValues.FAN_MAX_SPEED; // Affects max speed
            velocity *= this.speed;

            if (entity instanceof ItemEntity) {
                threshholdVelocity *= 1.8D;
                velocity *= 1.3D;
            }

            if (entity instanceof PlayerEntity) {
                if (((PlayerEntity)entity).abilities.flying)
                    continue;
            }

            if (entity instanceof MinecartEntity)
                velocity *= 0.5D;

            if ((entity instanceof FallingBlockEntity) && facing == Direction.UP)
                velocity = 0.0D;


            if (facing == Direction.UP) {
                threshholdVelocity *= 0.5D;
            }

            if (Math.abs(entity.getDeltaMovement().get(facing.getAxis())) < threshholdVelocity)
                entity.setDeltaMovement(entity.getDeltaMovement().add(facing.getStepX() * velocity, facing.getStepY() * velocity, facing.getStepZ() * velocity));

        }
    }

    public boolean isPathClear(final Entity entity, final Direction facing) {
        final int x = facing.getStepX() * (MathHelper.floor(entity.getX()) - this.worldPosition.getX());
        final int y = facing.getStepY() * (MathHelper.floor(entity.getY()) - this.worldPosition.getY());
        final int z = facing.getStepZ() * (MathHelper.floor(entity.getZ()) - this.worldPosition.getZ());
        boolean flag = true;

        for (int l2 = 1; l2 < Math.abs(x + y + z); l2++) {

            if (Block.canSupportCenter(this.level, this.worldPosition.relative(facing, l2), facing.getOpposite())) {
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
        final Direction facing = this.level.getBlockState(this.worldPosition).getValue(FanBlock.FACING);

        BlockPos endPos = this.worldPosition.relative(facing, MathHelper.floor(ConfigValues.FAN_RANGE + this.extraRange));
        if (facing == Direction.WEST)
            endPos = endPos.offset(0, 1, 1);
        else if (facing == Direction.NORTH)
            endPos = endPos.offset(1, 1, 0);

        if (facing == Direction.EAST)
            endPos = endPos.offset(1, 1, 1);
        else if (facing == Direction.SOUTH)
            endPos = endPos.offset(1, 1, 1);

        if (facing == Direction.UP)
            endPos = endPos.offset(1, 1, 1);
        else if (facing == Direction.DOWN)
            endPos = endPos.offset(1, 0, 1);

        return new AxisAlignedBB(this.worldPosition, endPos);
    }

    public static void spawnParticles(final World world, final BlockPos pos) {
        final double x = pos.getX() + world.random.nextFloat();
        final double y = pos.getY() + world.random.nextFloat();
        final double z = pos.getZ() + world.random.nextFloat();

        final Direction facing = world.getBlockState(pos).getValue(FanBlock.FACING);
        final double velocity = 0.2F + world.random.nextFloat() * 0.4F;

        final double velX = facing.getStepX() * velocity;
        final double velY = facing.getStepY() * velocity;
        final double velZ = facing.getStepZ() * velocity;

        world.addParticle(ParticleTypes.SMOKE, x, y, z, velX, velY, velZ);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.speed = nbt.getFloat("speed");
        this.extraRange = nbt.getDouble("extraRange");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putFloat("speed", this.speed);
        compound.putDouble("extraRange", this.extraRange);

        return compound;
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        final CompoundNBT tag = new CompoundNBT();
        return this.save(tag);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), 0, this.save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        this.load(null, packet.getTag()); // TODO Pass blockstate
        if (!this.level.isClientSide)
            this.level.sendBlockUpdated(this.worldPosition, this.level.getBlockState(this.worldPosition), this.level.getBlockState(this.worldPosition), 3);
        return;
    }

}
