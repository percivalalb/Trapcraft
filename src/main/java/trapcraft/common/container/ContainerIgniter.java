package trapcraft.common.container;

import trapcraft.TrapcraftMod;
import trapcraft.common.tileentitys.TileEntityIgniter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerIgniter extends Container {

	private TileEntityIgniter igniter;
	private EntityPlayer player;
	
	public ContainerIgniter(TileEntityIgniter par1TileEntityIgniter, EntityPlayer par2EntityPlayer) {
		this.igniter = par1TileEntityIgniter;
		this.player = par2EntityPlayer;
		int i;
		for (i = 0; i < 3; ++i) {
            this.addSlotToContainer(new Slot(par1TileEntityIgniter, i, 196, 26 + i * 18) {
            	@Override
            	public boolean isItemValid(ItemStack stack) {
            	    return stack != null && stack.getItem() == TrapcraftMod.igniter_Range;
            	}
            });
            this.addSlotToContainer(new Slot(par1TileEntityIgniter, i + 3, 214, 26 + i * 18) {
            	@Override
            	public boolean isItemValid(ItemStack stack) {
            	    return stack != null && stack.getItem() == TrapcraftMod.igniter_Range;
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
		return igniter.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 > 5 && itemstack1.getItem() == TrapcraftMod.igniter_Range)
            {
                if (!this.mergeItemStack(itemstack1, 0, 6, false))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 < 6) {
                if (!this.mergeItemStack(itemstack1, 6, inventorySlots.size(), false)) {
                    return null;
                }
              
            }
            else if(par2 > 6 && par2 < inventorySlots.size() - 9) {
            	if (!this.mergeItemStack(itemstack1, inventorySlots.size() - 9, inventorySlots.size(), false)) {
            		return null;
            	}
            }
            else if (!this.mergeItemStack(itemstack1, 6, inventorySlots.size() - 9, false)) {
        		return null;
        	}

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
	
	protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        int k = par2;

        if (par4)
        {
            k = par3 - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (par1ItemStack.isStackable())
        {
            while (par1ItemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2))
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1))
                {
                    int l = itemstack1.stackSize + par1ItemStack.stackSize;

                    if (l <= par1ItemStack.getMaxStackSize())
                    {
                        par1ItemStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize())
                    {
                        par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = par1ItemStack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (par4)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        if (par1ItemStack.stackSize > 0)
        {
            if (par4)
            {
                k = par3 - 1;
            }
            else
            {
                k = par2;
            }

            while (!par4 && k < par3 || par4 && k >= par2)
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 == null)
                {
                    slot.putStack(par1ItemStack.copy());
                    slot.onSlotChanged();
                    par1ItemStack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                if (par4)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return flag1;
    }
}
