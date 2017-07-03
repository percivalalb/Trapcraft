package trapcraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import trapcraft.ModItems;
import trapcraft.TrapcraftMod;
import trapcraft.tileentity.TileEntityIgniter;

public class ContainerIgniter extends Container {

	private TileEntityIgniter igniter;
	private EntityPlayer player;
	
	public ContainerIgniter(TileEntityIgniter par1TileEntityIgniter, EntityPlayer par2EntityPlayer) {
		this.igniter = par1TileEntityIgniter;
		this.player = par2EntityPlayer;
		int i;
		for (i = 0; i < 3; ++i) {
            this.addSlotToContainer(new Slot(par1TileEntityIgniter.inventory, i, 196, 26 + i * 18) {
            	@Override
            	public boolean isItemValid(ItemStack stack) {
            	    return stack != null && stack.getItem() == ModItems.IGNITER_RANGE;
            	}
            });
            this.addSlotToContainer(new Slot(par1TileEntityIgniter.inventory, i + 3, 214, 26 + i * 18) {
            	@Override
            	public boolean isItemValid(ItemStack stack) {
            	    return stack != null && stack.getItem() == ModItems.IGNITER_RANGE;
            	}
            });
        }
		for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(par2EntityPlayer.inventory, j + i * 9 + 9, 8 + j * 18, 8 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(par2EntityPlayer.inventory, i, 8 + i * 18, 66));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.igniter.inventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(index < this.igniter.inventory.getSizeInventory()) {
                if(!this.mergeItemStack(itemstack1, this.igniter.inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, this.igniter.inventory.getSizeInventory(), false)) {
                return null;
            }

            if(itemstack1.stackSize == 0)
                slot.putStack((ItemStack)null);
            else
                slot.onSlotChanged();
        }

        return itemstack;
    }
}
