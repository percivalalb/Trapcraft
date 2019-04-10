package trapcraft.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryIgniter extends InventoryBasic {

	public InventoryIgniter(int slotCount) {
		super(new TextComponentTranslation("container.igniter"), slotCount);
	}

}
