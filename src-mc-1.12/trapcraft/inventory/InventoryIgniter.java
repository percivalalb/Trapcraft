package trapcraft.inventory;

import net.minecraft.inventory.InventoryBasic;

public class InventoryIgniter extends InventoryBasic {

	public InventoryIgniter(int slotCount) {
		super("container.trapcraft.igniter", false, slotCount);
	}

	@Override
	public int getInventoryStackLimit() {
		return 8;
	}
}
