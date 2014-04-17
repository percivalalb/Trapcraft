package trapcraft.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import trapcraft.api.Properties;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 **/
public class ItemIgniterRange extends Item {

	public ItemIgniterRange() {
		super();
		this.setMaxStackSize(8);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(Properties.TEX_PACKAGE + "igniter_Range_Upgrade");
    }
    

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    	par3List.add("+1 to Block range");
    }
}
