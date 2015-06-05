package trapcraft.common.tileentitys;

import java.util.List;

import trapcraft.TrapcraftMod;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

/**
 * @author ProPercivalalb
 **/
public class TileEntityMagneticChest extends TileEntityTC implements IInventory {

    /** The current angle of the chest lid (between 0 and 1) */
    public float lidAngle;

    /** The angle of the chest lid last tick */
    public float prevLidAngle;

    /** The number of players currently using this chest */
    public int numUsingPlayers;

    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;

    private final int INVENTORY_SIZE = 27;
    private String customName;

    /**
     * The ItemStacks that hold the items currently being used in the Magnetic
     * Chest
     */
    private ItemStack[] inventory;

    public TileEntityMagneticChest() {

        super();
        inventory = new ItemStack[INVENTORY_SIZE];
    }

    @Override
    public int getSizeInventory() {

        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {

        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                setInventorySlotContents(slot, null);
            }
            else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {

        if (inventory[slot] != null) {
            ItemStack itemStack = inventory[slot];
            inventory[slot] = null;
            return itemStack;
        }
        else
            return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {

        inventory[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    /**
     * Called when a client event is received with the event number and
     * argument, see World.sendClientEvent
     */
    @Override
    public boolean receiveClientEvent(int eventID, int numUsingPlayers) {

        if (eventID == 1) {
            this.numUsingPlayers = numUsingPlayers;
            return true;
        }
        else
            return super.receiveClientEvent(eventID, numUsingPlayers);
    }
    
    @Override
    public void openInventory() {

        ++numUsingPlayers;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, TrapcraftMod.magneticChest, 1, numUsingPlayers);
    }

    @Override
    public void closeInventory() {

        --numUsingPlayers;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, TrapcraftMod.magneticChest, 1, numUsingPlayers);
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses,
     * e.g. the mob spawner uses this to count ticks and creates a new spawn
     * inside its implementation.
     */
    @Override
    public void updateEntity() {

        super.updateEntity();
        this.pullItemsIn();

        if (++ticksSinceSync % 20 * 4 == 0) {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, TrapcraftMod.magneticChest, 1, numUsingPlayers);
        }

        prevLidAngle = lidAngle;
        float angleIncrement = 0.1F;
        double adjustedXCoord, adjustedZCoord;

        if (numUsingPlayers > 0 && lidAngle == 0.0F) {
            adjustedXCoord = xCoord + 0.5D;
            adjustedZCoord = zCoord + 0.5D;
            worldObj.playSoundEffect(adjustedXCoord, yCoord + 0.5D, adjustedZCoord, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F) {
            float var8 = lidAngle;

            if (numUsingPlayers > 0) {
                lidAngle += angleIncrement;
            }
            else {
                lidAngle -= angleIncrement;
            }

            if (lidAngle > 1.0F) {
                lidAngle = 1.0F;
            }

            if (lidAngle < 0.5F && var8 >= 0.5F) {
                adjustedXCoord = xCoord + 0.5D;
                adjustedZCoord = zCoord + 0.5D;
                worldObj.playSoundEffect(adjustedXCoord, yCoord + 0.5D, adjustedZCoord, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (lidAngle < 0.0F) {
                lidAngle = 0.0F;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

        super.readFromNBT(nbtTagCompound);

        // Read in the ItemStacks in the inventory from NBT
        NBTTagList tagList = nbtTagCompound.getTagList("Items", 10);
        inventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = (NBTTagCompound) tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {

        super.writeToNBT(nbtTagCompound);

        // Write the ItemStacks in the inventory to NBT
        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if (inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        nbtTagCompound.setTag("Items", tagList);

    }
    
    public void pullItemsIn() {
    	List entity = this.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, (double)xCoord + 1.0D, (double)yCoord + 1.0D, (double)zCoord + 1.0D).expand(2D, 1.0D, 2D));

        if (!entity.isEmpty()) {
            for (int i = 0; i < entity.size(); i++) {
            	Entity target = (Entity)entity.get(i);
                if (!(target instanceof EntityItem))
                    continue;

                EntityItem entityItem = (EntityItem)entity.get(i);
                double centreX = (double)xCoord + 0.5D;
                double centreY = (double)yCoord + 0.5D;
                double centreZ = (double)zCoord + 0.5D;
                double d7 = centreX <= entityItem.posX ? -(entityItem.posX - centreX) : centreX - entityItem.posX;
                double d8 = centreY <= entityItem.posY ? -(entityItem.posY - centreY) : centreY - entityItem.posY;
                double d9 = centreZ <= entityItem.posZ ? -(entityItem.posZ - centreZ) : centreZ - entityItem.posZ;
                double speedMultiper = 0.050000000000000003D;
                double d11 = entityItem.posX - centreX;
                double d12 = entityItem.posZ - centreZ;
                double d13 = MathHelper.sqrt_double(d7 * d7 + d9 * d9);
                double d14 = Math.asin(d7 / d13);
                double d15 = (double)MathHelper.sin((float)d14) * speedMultiper;
                double d16 = (double)MathHelper.cos((float)d14) * speedMultiper;
                d16 = d9 <= 0.0D ? -d16 : d16;

                if ((double)MathHelper.abs((float)(entityItem.motionX + entityItem.motionY + entityItem.motionZ)) >= 0.10000000000000001D)
                    continue;

                if (d7 != 0.0D && (double)MathHelper.abs((float)entityItem.motionZ) < 0.10000000000000001D)
                    entityItem.motionX = d15;

                if (d9 != 0.0D && (double)MathHelper.abs((float)entityItem.motionZ) < 0.10000000000000001D)
                    entityItem.motionZ = d16;
            }
        }
    }
    
    
    public boolean insertStackFromEntity(EntityItem entityItem) {
	    boolean succesful = false;

	    if (entityItem == null || entityItem.isDead)
	        return false;
	    else {
	        ItemStack itemstack = entityItem.getEntityItem().copy();
	        ItemStack itemstack1 = this.insertStack(itemstack);

	        if (itemstack1 != null && itemstack1.stackSize != 0)
	        	entityItem.setEntityItemStack(itemstack1);
	        else {
	        	succesful = true;
	        	entityItem.setDead();
	        }

	        return succesful;
	    }
	}
	
    public ItemStack insertStack(ItemStack stack) {
    	int j = this.getSizeInventory();

        for (int k = 0; k < j && stack != null && stack.stackSize > 0; ++k)
        	stack = tryInsertStackToSlot(stack, k);

        if (stack != null && stack.stackSize == 0)
            stack = null;

        return stack;
    }
    
    public ItemStack tryInsertStackToSlot(ItemStack stack, int slot) {
        ItemStack slotStack = this.getStackInSlot(slot);

        if (this.isItemValidForSlot(slot, stack)) {
            boolean changed = false;

            if (slotStack == null) {
                int max = Math.min(stack.getMaxStackSize(), this.getInventoryStackLimit());
                if (max >= stack.stackSize) {
                	this.setInventorySlotContents(slot, stack);
                    stack = null;
                }
                else
                	this.setInventorySlotContents(slot, stack.splitStack(max));
                changed = true;
            }
            else if (this.areItemStacksEqualItem(slotStack, stack)) {
                int max = Math.min(stack.getMaxStackSize(), this.getInventoryStackLimit());
                if (max > slotStack.stackSize) {
                    int l = Math.min(stack.stackSize, max - slotStack.stackSize);
                    stack.stackSize -= l;
                    slotStack.stackSize += l;
                    changed = l > 0;
                }
            }

            if (changed)
                this.markDirty();
        }

        return stack;
    }
    
    private boolean areItemStacksEqualItem(ItemStack p_145894_0_, ItemStack p_145894_1_) {
        return p_145894_0_.getItem() != p_145894_1_.getItem() ? false : (p_145894_0_.getItemDamage() != p_145894_1_.getItemDamage() ? false : (p_145894_0_.stackSize > p_145894_0_.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(p_145894_0_, p_145894_1_)));
    }

    @Override
    public boolean isItemValidForSlot(int side, ItemStack itemStack) {
        return true;
    }

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
	
    public boolean hasCustomInventoryName() { return customName != null && customName.length() > 0; }
    public String getInventoryName() { return (this.hasCustomInventoryName() ? this.customName : "container.magneticchest"); }
    public void setInvName(String customName) { this.customName = customName; }
}
