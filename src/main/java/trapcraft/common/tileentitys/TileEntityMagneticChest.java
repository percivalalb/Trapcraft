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
    	List nearEntitys = worldObj.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, (double)xCoord + 1.0D, (double)yCoord + 1.0D, (double)zCoord + 1.0D).expand(2D, 1.0D, 2D));

        if (!nearEntitys.isEmpty())
        {
            for (int i = 0; i < nearEntitys.size(); i++)
            {
            	Entity target = (Entity)nearEntitys.get(i);
                if (!(target instanceof EntityItem)) {
                    return;
                }

                EntityItem entityitem = (EntityItem)nearEntitys.get(i);
                double d1 = (double)xCoord + 0.5D;
                double d4 = (double)yCoord + 0.5D;
                double d6 = (double)zCoord + 0.5D;
                double d7 = d1 <= entityitem.posX ? -(entityitem.posX - d1) : d1 - entityitem.posX;
                double d8 = d4 <= entityitem.posY ? -(entityitem.posY - d4) : d4 - entityitem.posY;
                double d9 = d6 <= entityitem.posZ ? -(entityitem.posZ - d6) : d6 - entityitem.posZ;
                double d10 = 0.050000000000000003D;
                double d11 = entityitem.posX - d1;
                double d12 = entityitem.posZ - d6;
                double d13 = MathHelper.sqrt_double(d7 * d7 + d9 * d9);
                double d14 = Math.asin(d7 / d13);
                double d15 = (double)MathHelper.sin((float)d14) * d10;
                double d16 = (double)MathHelper.cos((float)d14) * d10;
                d16 = d9 <= 0.0D ? -d16 : d16;

                if ((double)MathHelper.abs((float)(entityitem.motionX + entityitem.motionY + entityitem.motionZ)) >= 0.10000000000000001D)
                {
                    continue;
                }

                if (d7 != 0.0D && (double)MathHelper.abs((float)entityitem.motionZ) < 0.10000000000000001D)
                {
                    entityitem.motionX = d15;
                }

                if (d9 != 0.0D && (double)MathHelper.abs((float)entityitem.motionZ) < 0.10000000000000001D)
                {
                    entityitem.motionZ = d16;
                }
            }
        }
    }
    
    
    public static boolean addItem(IInventory par0IInventory, EntityItem par1EntityItem) {
        boolean flag = false;

        if (par1EntityItem == null) {
            return false;
        }
        else
        {
            ItemStack itemstack = par1EntityItem.getEntityItem().copy();
            ItemStack itemstack1 = tryToAdd(par0IInventory, itemstack, -1);

            if (itemstack1 != null && itemstack1.stackSize != 0) {
                par1EntityItem.setEntityItemStack(itemstack1);
            }
            else {
                flag = true;
                par1EntityItem.setDead();
            }

            return flag;
        }
    }
    
    public static ItemStack tryToAdd(IInventory par1IInventory, ItemStack par2ItemStack, int par3) {
        int j = 0;
        int k = par1IInventory.getSizeInventory();

        for (int l = j; l < k && par2ItemStack != null && par2ItemStack.stackSize > 0; ++l) {
            ItemStack itemstack1 = par1IInventory.getStackInSlot(l);

            if (itemstack1 == null) {
                par1IInventory.setInventorySlotContents(l, par2ItemStack);
                par2ItemStack = null;
            }
            else if (areStacksEqual(itemstack1, par2ItemStack)) {
                int i1 = par2ItemStack.getMaxStackSize() - itemstack1.stackSize;
                int j1 = Math.min(par2ItemStack.stackSize, i1);
                par2ItemStack.stackSize -= j1;
                itemstack1.stackSize += j1;
            }
        }

        if (par2ItemStack != null && par2ItemStack.stackSize == 0) {
            par2ItemStack = null;
        }

        return par2ItemStack;
    }
    
    private static boolean areStacksEqual(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return par1ItemStack != par2ItemStack ? false : (par1ItemStack.getItemDamage() != par2ItemStack.getItemDamage() ? false : (par1ItemStack.stackSize > par1ItemStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(par1ItemStack, par2ItemStack)));
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
