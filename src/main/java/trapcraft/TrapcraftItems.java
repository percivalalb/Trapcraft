package trapcraft;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.api.Constants;
import trapcraft.item.IgniterRangeItem;

public class TrapcraftItems {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Constants.MOD_ID);

	public static final RegistryObject<IgniterRangeItem> IGNITER_RANGE = ITEMS.register("igniter_range", IgniterRangeItem::new);
}