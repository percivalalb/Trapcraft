package trapcraft.common;

import trapcraft.client.gui.GuiIgniter;
import trapcraft.common.container.ContainerIgniter;
import trapcraft.common.tileentitys.TileEntityIgniter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class CommonProxy implements IGuiHandler {

	public void onModPre() {}
    public void onModLoad() {}
    public void onModPost() {}
    
    public void handleTileEntityPacket(int x, int y, int z, ForgeDirection direction, String owner, String customName, String state) {}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if(ID == 1) {
			if(tileentity instanceof TileEntityIgniter) {
				return new ContainerIgniter((TileEntityIgniter)tileentity, player);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if(ID == 1) {
			if(tileentity instanceof TileEntityIgniter) {
				return new GuiIgniter((TileEntityIgniter)tileentity, player);
			}
		}
		return null;
	}
	
	public EntityPlayer getClientPlayer() {
		return null;
	}
}
