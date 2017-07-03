package trapcraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import trapcraft.ModBlocks;
import trapcraft.ModItems;
import trapcraft.TrapcraftMod;
import trapcraft.block.BlockIgniter;

/**
 * @author ProPercivalalb
 **/
public class TileEntityIgniter extends TileEntity implements ITickable {

	public InventoryBasic inventory = new InventoryBasic("container.igniter", false, 6) {
		@Override
		public int getInventoryStackLimit() {
			return 8;
		}
		
		@Override
		public void markDirty() {
			if(hasWorldObj()) {
				if(worldObj.isRemote) {return;}
				((BlockIgniter)ModBlocks.IGNITER).updateIgniterState(worldObj, pos);
			}
		}
	};
	  
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);

        for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.getSizeInventory()) {
                this.inventory.setInventorySlotContents(b0, ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < this.inventory.getSizeInventory(); ++i) {
            if(this.inventory.getStackInSlot(i) != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory.getStackInSlot(i).writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
        
        return par1NBTTagCompound;
    }
    
    
    public int getRangeUpgrades() {
    	int upgrades = 0;
        for(int i = 0; i < this.inventory.getSizeInventory(); ++i) {
    		ItemStack stack = this.inventory.getStackInSlot(i);
    		if(stack != null) {
    			if(stack.getItem() == ModItems.IGNITER_RANGE) {
    				upgrades += stack.stackSize;
    			}
    		}
    	}
    	
    	return Math.min(upgrades, 100);
    }
    
    @Override
    public void update() {
    	if(!this.worldObj.isRemote) {
    		((BlockIgniter)ModBlocks.IGNITER).updateIgniterState(this.worldObj, this.pos);
    	}
    }
}
