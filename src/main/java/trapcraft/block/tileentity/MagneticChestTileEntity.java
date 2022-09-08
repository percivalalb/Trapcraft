package trapcraft.block.tileentity;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import trapcraft.TrapcraftTileEntityTypes;

/**
 * @author ProPercivalalb
 **/
public class MagneticChestTileEntity extends ChestBlockEntity {

    public MagneticChestTileEntity(BlockPos pos, BlockState initialBlockState) {
        super(TrapcraftTileEntityTypes.MAGNETIC_CHEST.get(), pos, initialBlockState);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, MagneticChestTileEntity entity) {
        ChestBlockEntity.lidAnimateTick(level, pos, state, entity);
        MagneticChestTileEntity.tick(level, pos, state, entity);
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, MagneticChestTileEntity blockEntity) {
        final List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(50), item -> item.distanceToSqr(pos.getX(),pos.getY(),pos.getZ()) < 10D);

        for (final ItemEntity itemEntity : entities) {
            final double centreX = pos.getX() + 0.5D;
            final double centreY = pos.getY() + 0.5D;
            final double centreZ = pos.getZ() + 0.5D;
            final double diffX = -itemEntity.getX() + centreX;
            final double diffY = -itemEntity.getY() + centreY;
            final double diffZ = -itemEntity.getZ() + centreZ;
            final double speedMultiper = 0.05D;
            double d11 = itemEntity.getX() - centreX;
            double d12 = itemEntity.getZ() - centreZ;
            final double horizDiffSq = Mth.sqrt((float) (diffX * diffX + diffZ * diffZ));
            final double angle = Math.asin(diffX / horizDiffSq);
            double d15 = Math.abs(Mth.sin((float)angle) * speedMultiper);
            double d16 = Math.abs(Mth.cos((float)angle) * speedMultiper);
            d15 = diffX <= 0.0D ? -d15 : d15;
            d16 = diffZ <= 0.0D ? -d16 : d16;
            if (itemEntity.getDeltaMovement().dot(itemEntity.getDeltaMovement()) >= 0.2D) {
                continue;
            }

            itemEntity.setDeltaMovement(d15, diffY >= 0.7 ? speedMultiper * 2 : itemEntity.getDeltaMovement().y(), d16);
        }
    }

    public boolean insertStackFromEntity(final ItemEntity entityItem) {
        boolean succesful = false;

        if (entityItem == null || !entityItem.isAlive())
            return false;
        else {
            final ItemStack itemstack = entityItem.getItem().copy();
            final ItemStack itemstack1 = this.addItem(itemstack);

            if (!itemstack1.isEmpty())
                entityItem.setItem(itemstack1);
            else {
                succesful = true;
                entityItem.remove(Entity.RemovalReason.DISCARDED);
            }

            return succesful;
        }
    }

    public ItemStack addItem(final ItemStack stack) {
        ItemStack itemstack = stack.copy();

        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemstack1 = this.getItem(i);
            if (itemstack1.isEmpty()) {
                this.setItem(i, itemstack);
                this.setChanged();
                return ItemStack.EMPTY;
            }

            if (ItemStack.isSame(itemstack1, itemstack)) {
                final int j = Math.min(this.getMaxStackSize(), itemstack1.getMaxStackSize());
                final int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());
                if (k > 0) {
                    itemstack1.grow(k);
                    itemstack.shrink(k);
                    if (itemstack.isEmpty()) {
                        this.setChanged();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (itemstack.getCount() != stack.getCount()) {
           this.setChanged();
        }

        return itemstack;
     }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.trapcraft.magnetic_chest");
    }
}
