package trapcraft;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trapcraft.item.ItemIgniterRange;
import trapcraft.lib.Reference;

public class TrapcraftItems {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Reference.MOD_ID);

	public static final RegistryObject<ItemIgniterRange> IGNITER_RANGE = ITEMS.register("igniter_range", ItemIgniterRange::new);
}