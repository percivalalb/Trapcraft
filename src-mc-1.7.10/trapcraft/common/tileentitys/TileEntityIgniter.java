package trapcraft.common.tileentitys;

import trapcraft.TrapcraftMod;
import trapcraft.common.blocks.BlockIgniter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;

/**
 * @author ProPercivalalb
 **/
public class TileEntityIgniter extends TileEntity implements IInventory {

	private ItemStack[] contents = new ItemStack[6];
	                                             
	public int getSizeInventory() {
        return this.contents.length;
    }

    public ItemStack getStackInSlot(int par1) {
        return this.contents[par1];
    }

    public ItemStack decrStackSize(int par1, int par2) {
    	this.markDirty();
    	if (this.contents[par1] != null) {
            ItemStack itemstack;

            if (this.contents[par1].stackSize <= par2) {
                itemstack = this.contents[par1];
                this.contents[par1] = null;
                return itemstack;
            }
            else {
                itemstack = this.contents[par1].splitStack(par2);

                if (this.contents[par1].stackSize == 0) {
                    this.contents[par1] = null;
                }

                return itemstack;
            }
        }
        else {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int par1) {
        if (this.contents[par1] != null) {
            ItemStack itemstack = this.contents[par1];
            this.contents[par1] = null;
            return itemstack;
        }
        else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
    	this.markDirty();
        this.contents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.contents.length) {
                this.contents[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.contents.length; ++i) {
            if (this.contents[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 8;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
        return true;
    }
    
    @Override
    public void markDirty() {
        if (this.worldObj != null) {
        	if(this.worldObj.isRemote) {return;}
            int x = this.xCoord;
        	int y = this.yCoord;
        	int z = this.zCoord;
        	
        	if(z != 0) {
        		if(z < 0) {
        			z -= this.getRangeUpgrades();
        		}
        		else if(z > 0) {
        			z += this.getRangeUpgrades();
        		}	
        	}
        	
        	if(x != 0) {
        		if(x < 0) {
        			x -= this.getRangeUpgrades();
        		}
        		else if(x > 0) {
        			x += this.getRangeUpgrades();
        		}	
        	}
        	
        	if(y != 0) {
        		if(y < 0) {
        			y -= this.getRangeUpgrades();
        		}
        		else if(y > 0) {
        			y += this.getRangeUpgrades();
        		}	
        	}
        	
        	if(Block.isEqualTo(this.worldObj.getBlock(x, y, z), Blocks.fire)) {
        		this.worldObj.setBlockToAir(x, y, z);
        		this.worldObj.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.fizz", 0.5F, 2.6F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.8F);
        	}
            ((BlockIgniter)TrapcraftMod.igniter).updateIgniterState(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
    }
    
    public int getRangeUpgrades() {
    	int upgrades = 0;
    	for(ItemStack stack : contents) {
    		if(stack != null) {
    			if(stack.getItem() == TrapcraftMod.igniter_Range) {
    				upgrades += stack.stackSize;
    			}
    		}
    	}
    	if(upgrades > 100) {
    		upgrades = 100;
    	}
    	return upgrades;
    }
    
    public void updateEntity() {
    	if(!this.worldObj.isRemote) {
    		((BlockIgniter)TrapcraftMod.igniter).updateIgniterState(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    	}
    }

	@Override
	public String getInventoryName() {
		return "container.igniter";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}
}
