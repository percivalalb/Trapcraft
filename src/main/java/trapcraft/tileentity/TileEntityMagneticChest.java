package trapcraft.tileentity;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import trapcraft.TrapcraftTileEntityTypes;

/**
 * @author ProPercivalalb
 **/
public class TileEntityMagneticChest extends ChestTileEntity implements IChestLid, ITickableTileEntity {

    public TileEntityMagneticChest() {
        super(TrapcraftTileEntityTypes.MAGNETIC_CHEST.get());
    }

    @Override
    public void tick() {
    	super.tick();
    	this.pullItemsIn();
    }

    public void pullItemsIn() {
    	List<ItemEntity> entities = this.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(this.getPos()).grow(50), item -> item.getDistanceSq(this.getPos().getX(),this.getPos().getY(),this.getPos().getZ()) < 10D);

    	for(ItemEntity itemEntity : entities) {
       		double centreX = this.pos.getX() + 0.5D;
       		double centreY = this.pos.getY() + 0.5D;
    		double centreZ = this.pos.getZ() + 0.5D;
    		double diffX = -itemEntity.getPosX() + centreX;
    		double diffY = -itemEntity.getPosY() + centreY;
    		double diffZ = -itemEntity.getPosZ() + centreZ;
    		double speedMultiper = 0.05D;
    		double d11 = itemEntity.getPosX() - centreX;
    		double d12 = itemEntity.getPosZ() - centreZ;
    		double horizDiffSq = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
    		double angle = Math.asin(diffX / horizDiffSq);
    		double d15 = Math.abs(MathHelper.sin((float)angle) * speedMultiper);
    		double d16 = Math.abs(MathHelper.cos((float)angle) * speedMultiper);
    		d15 = diffX <= 0.0D ? -d15 : d15;
    		d16 = diffZ <= 0.0D ? -d16 : d16;
    		if(itemEntity.getMotion().dotProduct(itemEntity.getMotion()) >= 0.2D)
    			continue;

    		itemEntity.setMotion(d15, diffY >= 0.7 ? speedMultiper * 2 : itemEntity.getMotion().getY(), d16);
        }
    }


    public boolean insertStackFromEntity(ItemEntity entityItem) {
	    boolean succesful = false;

	    if (entityItem == null || !entityItem.isAlive())
	        return false;
	    else {
	        ItemStack itemstack = entityItem.getItem().copy();
	        ItemStack itemstack1 = this.addItem(itemstack);

	        if (!itemstack1.isEmpty())
	        	entityItem.setItem(itemstack1);
	        else {
	        	succesful = true;
	        	entityItem.remove();
	        }

	        return succesful;
	    }
	}

    public ItemStack addItem(ItemStack stack) {
    	ItemStack itemstack = stack.copy();

    	for(int i = 0; i < this.getSizeInventory(); ++i) {
    		ItemStack itemstack1 = this.getStackInSlot(i);
    		if(itemstack1.isEmpty()) {
    			this.setInventorySlotContents(i, itemstack);
    			this.markDirty();
    			return ItemStack.EMPTY;
    		}

    		if(ItemStack.areItemsEqual(itemstack1, itemstack)) {
    			int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
    			int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());
    			if(k > 0) {
    				itemstack1.grow(k);
    				itemstack.shrink(k);
    				if(itemstack.isEmpty()) {
    					this.markDirty();
    					return ItemStack.EMPTY;
    				}
    			}
    		}
    	}

    	if(itemstack.getCount() != stack.getCount()) {
           this.markDirty();
        }

        return itemstack;
     }

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.trapcraft.magnetic_chest");
	}
}
