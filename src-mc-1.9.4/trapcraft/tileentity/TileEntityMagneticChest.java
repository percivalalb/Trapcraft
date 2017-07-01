package trapcraft.tileentity;

import java.util.List;

import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import trapcraft.ModBlocks;
import trapcraft.TrapcraftMod;
import trapcraft.block.BlockMagneticChest;

/**
 * @author ProPercivalalb
 **/
public class TileEntityMagneticChest extends TileEntityLockable implements ITickable, IInventory {

	public float lidAngle;
    /** The angle of the ender chest lid last tick */
    public float prevLidAngle;
    public int numPlayersUsing;
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
    public ItemStack removeStackFromSlot(int index) {
        if(this.inventory[index] != null) {
            ItemStack itemStack = this.inventory[index];
            this.inventory[index] = null;
            return itemStack;
        }
        else
            return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        this.inventory[slot] = itemStack;

        if(itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
            itemStack.stackSize = this.getInventoryStackLimit();

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        }
        else {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void update() {
        this.pullItemsIn();
        
        if (++this.ticksSinceSync % 20 * 4 == 0)
            this.worldObj.addBlockEvent(this.pos, ModBlocks.MAGNETIC_CHEST, 1, this.numPlayersUsing);

        this.prevLidAngle = this.lidAngle;
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        float f = 0.1F;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            double d0 = (double)i + 0.5D;
            double d1 = (double)k + 0.5D;
            this.worldObj.playSound((EntityPlayer)null, d0, (double)j + 0.5D, d1, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0)
                this.lidAngle += f;
            else
                this.lidAngle -= f;

            if (this.lidAngle > 1.0F)
                this.lidAngle = 1.0F;

            float f1 = 0.5F;

            if (this.lidAngle < f1 && f2 >= f1) {
                double d3 = (double)i + 0.5D;
                double d2 = (double)k + 0.5D;
                this.worldObj.playSound((EntityPlayer)null, d3, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
                this.lidAngle = 0.0F;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        super.readFromNBT(compound);

        // Read in the ItemStacks in the inventory from NBT
        NBTTagList tagList = compound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];
        for(int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = (NBTTagCompound) tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if(slot >= 0 && slot < this.inventory.length)
            	this.inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        super.writeToNBT(compound);

        // Write the ItemStacks in the inventory to NBT
        NBTTagList tagList = new NBTTagList();
        for(int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if(this.inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                this.inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        compound.setTag("Items", tagList);

        return compound;
    }
    
    public void pullItemsIn() {
    	List entity = this.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.pos).expand(2D, 1.0D, 2D));

        if (!entity.isEmpty()) {
            for (int i = 0; i < entity.size(); i++) {
            	Entity target = (Entity)entity.get(i);
                if (!(target instanceof EntityItem))
                    continue;

                EntityItem entityItem = (EntityItem)entity.get(i);
                double centreX = (double)this.pos.getX() + 0.5D;
                double centreY = (double)this.pos.getY() + 0.5D;
                double centreZ = (double)this.pos.getZ() + 0.5D;
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
	public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }
	
    public void setInvName(String customName) { this.customName = customName; }

	@Override
	public String getName() {
		return (this.hasCustomName() ? this.customName : "container.magnetic_chest");
	}

	@Override
	public boolean hasCustomName() {
		return customName != null && customName.length() > 0;
	}

	@Override
	public void openInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0)
                this.numPlayersUsing = 0;

            ++this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }

	@Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockMagneticChest) {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for(int i = 0; i < this.INVENTORY_SIZE; i++)
			this.inventory[i] = (ItemStack)null;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		 return new ContainerChest(playerInventory, this, playerIn);
	}

	@Override
	public String getGuiID() {
		return "trapcraft:magnetic_chest";
	}
}
