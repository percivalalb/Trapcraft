package trapcraft.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import trapcraft.ModBlocks;
import trapcraft.ModItems;
import trapcraft.ModTileEntities;
import trapcraft.block.BlockIgniter;
import trapcraft.inventory.ContainerIgniter;
import trapcraft.inventory.InventoryIgniter;

/**
 * @author ProPercivalalb
 **/
public class TileEntityIgniter extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IInventoryChangedListener {


	public InventoryIgniter inventory;
	public int lastUpgrades;
	
	public TileEntityIgniter() {
		super(ModTileEntities.IGNITER);
		this.inventory = new InventoryIgniter(6);
	}
	
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        ListNBT nbttaglist = compound.getList("Items", 10);

        for(int i = 0; i < nbttaglist.size(); ++i) {
        	CompoundNBT nbttagcompound1 = (CompoundNBT)nbttaglist.getCompound(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.getSizeInventory()) {
                this.inventory.setInventorySlotContents(b0, ItemStack.read(nbttagcompound1));
            }
        }
		this.inventory.addListener(this);
        this.lastUpgrades = this.getRangeUpgrades();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ListNBT nbttaglist = new ListNBT();

        for(int i = 0; i < this.inventory.getSizeInventory(); ++i) {
            if(this.inventory.getStackInSlot(i) != null) {
            	CompoundNBT nbttagcompound1 = new CompoundNBT();
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
    		if(stack.getItem() == ModItems.IGNITER_RANGE) {
    			upgrades += stack.getCount();
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
	public void onInventoryChanged(IInventory invBasic) {
		if(!this.world.isRemote) {
			int newUpgrades = this.getRangeUpgrades();
			if(newUpgrades != this.lastUpgrades) {
				((BlockIgniter)ModBlocks.IGNITER).updateIgniterState(this.world, this.pos);
			}
		}
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new ContainerIgniter(windowId, playerInventory, this.inventory);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.trapcraft.igniter");
	}

}
