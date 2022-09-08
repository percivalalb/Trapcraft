package trapcraft.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.chat.Component;

public class IgniterInventory extends SimpleContainer implements MenuProvider {

    public IgniterInventory(int slotCount) {
        super(slotCount);
    }

    @Override
    public int getMaxStackSize() {
        return 8;
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
        return new IgniterContainer(windowId, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.trapcraft.igniter");
    }
}
