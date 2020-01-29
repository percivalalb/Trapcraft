package trapcraft.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class InventoryIgniter extends Inventory implements INamedContainerProvider {
	
	public InventoryIgniter(int slotCount) {
		super(slotCount);
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 8;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new ContainerIgniter(windowId, playerInventory, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.trapcraft.igniter");
	}
}
