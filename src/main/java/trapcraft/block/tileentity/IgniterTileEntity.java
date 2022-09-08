package trapcraft.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import trapcraft.TrapcraftBlocks;
import trapcraft.TrapcraftItems;
import trapcraft.TrapcraftTileEntityTypes;
import trapcraft.block.IgniterBlock;
import trapcraft.inventory.IgniterContainer;
import trapcraft.inventory.IgniterInventory;

import javax.annotation.Nonnull;

/**
 * @author ProPercivalalb
 **/
public class IgniterTileEntity extends BlockEntity implements MenuProvider, ContainerListener {


    public IgniterInventory inventory;
    public int lastUpgrades;

    public IgniterTileEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(TrapcraftTileEntityTypes.IGNITER.get(), p_155229_, p_155230_);
        this.inventory = new IgniterInventory(6);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        final ListTag nbttaglist = nbt.getList("Items", 10);

        for (int i = 0; i < nbttaglist.size(); ++i) {
            final CompoundTag nbttagcompound1 = nbttaglist.getCompound(i);
            final byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.getContainerSize()) {
                this.inventory.setItem(b0, ItemStack.of(nbttagcompound1));
            }
        }
        this.inventory.addListener(this);
        this.lastUpgrades = this.getRangeUpgrades();
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ListTag nbttaglist = new ListTag();

        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            if (this.inventory.getItem(i) != null) {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.putByte("Slot", (byte)i);
                this.inventory.getItem(i).save(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        compound.put("Items", nbttaglist);
    }


    public int getRangeUpgrades() {
        int upgrades = 0;
        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            final ItemStack stack = this.inventory.getItem(i);
            if (stack.getItem() == TrapcraftItems.IGNITER_RANGE.get()) {
                upgrades += stack.getCount();
            }
        }

        return Math.min(upgrades, 100);
    }

    public static void tick(Level var1, BlockPos var2, BlockState var3, BlockEntity var4) {
        if (!var1.isClientSide) {
            ((IgniterBlock)TrapcraftBlocks.IGNITER.get()).updateIgniterState(var1, var2);
        }
    }

    @Override
    public void containerChanged(Container invBasic) {
        if (!this.level.isClientSide) {
            final int newUpgrades = this.getRangeUpgrades();
            if (newUpgrades != this.lastUpgrades) {
                ((IgniterBlock)TrapcraftBlocks.IGNITER.get()).updateIgniterState(this.level, this.worldPosition);
            }
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
        return new IgniterContainer(windowId, playerInventory, this.inventory);
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.translatable("container.trapcraft.igniter");
    }

}
