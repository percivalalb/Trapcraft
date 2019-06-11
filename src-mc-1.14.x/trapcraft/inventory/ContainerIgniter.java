package trapcraft.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import trapcraft.ModContainerTypes;
import trapcraft.ModItems;

public class ContainerIgniter extends Container {

	private IInventory igniter;
	
	public ContainerIgniter(int windowId, PlayerInventory playerInventory) {
		this(windowId, playerInventory, new Inventory(6));
	}
	
	
	public ContainerIgniter(int windowId, PlayerInventory playerInventory, IInventory igniter) {
		super(ModContainerTypes.IGNITER, windowId);
		this.igniter = igniter;
		func_216962_a(igniter, 6);
		
		int i;
		for (i = 0; i < 3; ++i) {
            this.addSlot(new Slot(igniter, i, 196, 26 + i * 18) {
            	@Override
            	public boolean isItemValid(ItemStack stack) {
            	    return stack.getItem() == ModItems.IGNITER_RANGE;
            	}
            });
            this.addSlot(new Slot(igniter, i + 3, 214, 26 + i * 18) {
            	@Override
            	public boolean isItemValid(ItemStack stack) {
            	    return stack.getItem() == ModItems.IGNITER_RANGE;
            	}
            });
        }
		for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 8 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 66));
        }
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity entityplayer) {
		return this.igniter.isUsableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(index < this.igniter.getSizeInventory()) {
                if(!this.mergeItemStack(itemstack1, this.igniter.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, this.igniter.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if(itemstack1.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }

        return itemstack;
    }
}
