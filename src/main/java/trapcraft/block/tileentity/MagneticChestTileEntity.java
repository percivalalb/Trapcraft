package trapcraft.block.tileentity;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import trapcraft.TrapcraftTileEntityTypes;

/**
 * @author ProPercivalalb
 **/
public class MagneticChestTileEntity extends ChestTileEntity implements ITickableTileEntity {

    public MagneticChestTileEntity() {
        super(TrapcraftTileEntityTypes.MAGNETIC_CHEST.get());
    }

    @Override
    public void tick() {
        super.tick();
        this.pullItemsIn();
    }

    public void pullItemsIn() {
        final List<ItemEntity> entities = this.level.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(this.getBlockPos()).inflate(50), item -> item.distanceToSqr(this.getBlockPos().getX(),this.getBlockPos().getY(),this.getBlockPos().getZ()) < 10D);

        for (final ItemEntity itemEntity : entities) {
            final double centreX = this.worldPosition.getX() + 0.5D;
            final double centreY = this.worldPosition.getY() + 0.5D;
            final double centreZ = this.worldPosition.getZ() + 0.5D;
            final double diffX = -itemEntity.getX() + centreX;
            final double diffY = -itemEntity.getY() + centreY;
            final double diffZ = -itemEntity.getZ() + centreZ;
            final double speedMultiper = 0.05D;
            double d11 = itemEntity.getX() - centreX;
            double d12 = itemEntity.getZ() - centreZ;
            final double horizDiffSq = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
            final double angle = Math.asin(diffX / horizDiffSq);
            double d15 = Math.abs(MathHelper.sin((float)angle) * speedMultiper);
            double d16 = Math.abs(MathHelper.cos((float)angle) * speedMultiper);
            d15 = diffX <= 0.0D ? -d15 : d15;
            d16 = diffZ <= 0.0D ? -d16 : d16;
            if (itemEntity.getDeltaMovement().dot(itemEntity.getDeltaMovement()) >= 0.2D)
                continue;

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
                entityItem.remove();
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
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.trapcraft.magnetic_chest");
    }
}
