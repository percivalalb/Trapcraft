package trapcraft.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 **/
public class ItemIgniterRange extends Item {

	public ItemIgniterRange() {
		super();
		this.setMaxStackSize(8);
		this.setCreativeTab(CreativeTabs.REDSTONE);
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    	tooltip.add("+1 to Block range");
    }
}
