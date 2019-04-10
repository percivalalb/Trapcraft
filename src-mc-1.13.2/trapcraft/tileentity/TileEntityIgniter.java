package trapcraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import trapcraft.ModBlocks;
import trapcraft.ModItems;
import trapcraft.block.BlockIgniter;
import trapcraft.inventory.ContainerIgniter;

/**
 * @author ProPercivalalb
 **/
public class TileEntityIgniter extends TileEntity implements ITickable, IInteractionObject {

	public TileEntityIgniter() {
		super(ModBlocks.TILE_IGNITER);
	}

	public InventoryBasic inventory = new InventoryBasic(new TextComponentTranslation("container.igniter"), 6) {
		@Override
		public int getInventoryStackLimit() {
			return 8;
		}
		
		@Override
		public void markDirty() {
			if(hasWorld()) {
				if(world.isRemote) {return;}
				((BlockIgniter)ModBlocks.IGNITER).updateIgniterState(world, pos);
			}
		}
	};
	  
    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        NBTTagList nbttaglist = compound.getList("Items", 10);

        for(int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompound(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.getSizeInventory()) {
                this.inventory.setInventorySlotContents(b0, ItemStack.read(nbttagcompound1));
            }
        }
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < this.inventory.getSizeInventory(); ++i) {
            if(this.inventory.getStackInSlot(i) != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.putByte("Slot", (byte)i);
                this.inventory.getStackInSlot(i).write(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        compound.put("Items", nbttaglist);
        
        return compound;
    }
    
    
    public int getRangeUpgrades() {
    	int upgrades = 0;
        for(int i = 0; i < this.inventory.getSizeInventory(); ++i) {
    		ItemStack stack = this.inventory.getStackInSlot(i);
    		if(stack != null) {
    			if(stack.getItem() == ModItems.IGNITER_RANGE) {
    				upgrades += stack.getCount();
    			}
    		}
    	}
    	
    	return Math.min(upgrades, 100);
    }
    
    @Override
    public void tick() {
    	if(!this.world.isRemote) {
    		((BlockIgniter)ModBlocks.IGNITER).updateIgniterState(this.world, this.pos);
    	}
    }

	@Override
	public ITextComponent getCustomName() {
		return null;
	}

	@Override
	public ITextComponent getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public Container createContainer(InventoryPlayer inventory, EntityPlayer player) {
		return new ContainerIgniter(this, player);
	}

	@Override
	public String getGuiID() {
		return "trapcraft:igniter";
	}
}
