package trapcraft.tileentity;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import trapcraft.ModBlocks;
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
    private NonNullList<ItemStack> inventory;

    public TileEntityMagneticChest() {
        super();
        this.inventory = NonNullList.<ItemStack>withSize(INVENTORY_SIZE, ItemStack.EMPTY);
    }

    @Override
    public int getSizeInventory() {

        return inventory.size();
    }

    public ItemStack getStackInSlot(int index)
    {
        return (ItemStack)this.inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventory, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

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
            this.world.addBlockEvent(this.pos, ModBlocks.MAGNETIC_CHEST, 1, this.numPlayersUsing);

        this.prevLidAngle = this.lidAngle;
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        float f = 0.1F;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            double d0 = (double)i + 0.5D;
            double d1 = (double)k + 0.5D;
            this.world.playSound((EntityPlayer)null, d0, (double)j + 0.5D, d1, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
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
                this.world.playSound((EntityPlayer)null, d3, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
                this.lidAngle = 0.0F;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        super.readFromNBT(compound);

        // Read in the ItemStacks in the inventory from NBT
        ItemStackHelper.loadAllItems(compound, this.inventory);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.inventory);

        return compound;
    }
    
    public void pullItemsIn() {
   		double centreX = (double)this.pos.getX() + 0.5D;
   		double centreY = (double)this.pos.getY() + 0.5D;
		double centreZ = (double)this.pos.getZ() + 0.5D;
    	
    	List<EntityItem> entities = this.world.getEntities(EntityItem.class, item -> item.getDistanceSq(centreX, centreY, centreZ) < 16D);

    	for(EntityItem itemEntity : entities) {
    		double diffX = -itemEntity.posX + centreX;
    		double diffY = -itemEntity.posY + centreY;
    		double diffZ = -itemEntity.posZ + centreZ;
    		double speedMultiper = 0.05D;
    		double d11 = itemEntity.posX - centreX;
    		double d12 = itemEntity.posZ - centreZ;
    		double horizDiffSq = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
    		double angle = Math.asin(diffX / horizDiffSq);
    		double d15 = Math.abs((double)MathHelper.sin((float)angle) * speedMultiper);
    		double d16 = Math.abs((double)MathHelper.cos((float)angle) * speedMultiper);
    		d15 = diffX <= 0.0D ? -d15 : d15;
    		d16 = diffZ <= 0.0D ? -d16 : d16;
    		if(MathHelper.abs((float)(itemEntity.motionX + itemEntity.motionY + itemEntity.motionZ))  >= 0.2D)
    			continue;

    		
    		itemEntity.motionX = d15;
    		itemEntity.motionY = diffY >= 0.7 ? speedMultiper * 2 : itemEntity.motionY;
    		itemEntity.motionZ = d16;
        }
    }
    

    public boolean insertStackFromEntity(EntityItem entityItem) {
	    boolean succesful = false;

	    if (entityItem == null || entityItem.isDead)
	        return false;
	    else {
	        ItemStack itemstack = entityItem.getItem().copy();
	        ItemStack itemstack1 = this.insertStack(itemstack);

	        if (!itemstack1.isEmpty())
	        	entityItem.setItem(itemstack1);
	        else {
	        	succesful = true;
	        	entityItem.setDead();
	        }

	        return succesful;
	    }
	}
	
    public ItemStack insertStack(ItemStack stack) {
    	int j = this.getSizeInventory();

        for (int k = 0; k < j && stack.getCount() > 0; ++k)
        	stack = tryInsertStackToSlot(stack, k);

        if (stack.isEmpty())
            stack = ItemStack.EMPTY;

        return stack;
    }
    
    public ItemStack tryInsertStackToSlot(ItemStack stack, int slot) {
        ItemStack slotStack = this.getStackInSlot(slot);

        if (this.isItemValidForSlot(slot, stack)) {
            boolean changed = false;

            if (slotStack.isEmpty()) {
                int max = Math.min(stack.getMaxStackSize(), this.getInventoryStackLimit());
                if (max >= stack.getCount()) {
                	this.setInventorySlotContents(slot, stack);
                    stack = ItemStack.EMPTY;
                }
                else
                	this.setInventorySlotContents(slot, stack.splitStack(max));
                changed = true;
            }
            else if (this.areItemStacksEqualItem(slotStack, stack)) {
                int max = Math.min(stack.getMaxStackSize(), this.getInventoryStackLimit());
                if (max > slotStack.getCount()) {
                    int l = Math.min(stack.getCount(), max - slotStack.getCount());
                    stack.shrink(l);
                    slotStack.grow(l);
                    changed = l > 0;
                }
            }

            if (changed)
                this.markDirty();
        }

        return stack;
    }
    
    private boolean areItemStacksEqualItem(ItemStack p_145894_0_, ItemStack p_145894_1_) {
        return p_145894_0_.getItem() != p_145894_1_.getItem() ? false : (p_145894_0_.getItemDamage() != p_145894_1_.getItemDamage() ? false : (p_145894_0_.getCount() > p_145894_0_.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(p_145894_0_, p_145894_1_)));
    }

    
    @Override
    public boolean isItemValidForSlot(int side, ItemStack itemStack) {
        return true;
    }

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
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
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
        }
    }

	@Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockMagneticChest) {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
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
        this.inventory.clear();
    }

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		 return new ContainerChest(playerInventory, this, playerIn);
	}

	@Override
	public String getGuiID() {
		return "trapcraft:magnetic_chest";
	}
	
	@Override
	public boolean isEmpty()
    {
        for (ItemStack itemstack : this.inventory)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }
}
