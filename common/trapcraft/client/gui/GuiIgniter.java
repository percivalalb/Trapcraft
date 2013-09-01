package trapcraft.client.gui;

import org.lwjgl.opengl.GL11;

import trapcraft.api.Properties;
import trapcraft.common.container.ContainerIgniter;
import trapcraft.common.tileentitys.TileEntityIgniter;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.StatCollector;

public class GuiIgniter extends GuiContainer {

	private TileEntityIgniter igniter;
	private EntityPlayer player;
	
	public GuiIgniter(TileEntityIgniter par1TileEntityIgniter, EntityPlayer par2EntityPlayer) {
		super(new ContainerIgniter(par1TileEntityIgniter, par2EntityPlayer));
		this.igniter = par1TileEntityIgniter;
		this.player = par2EntityPlayer;
		this.xSize = 238;
		this.ySize = 89;
	}

	@Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s = this.igniter.isInvNameLocalized() ? this.igniter.getInvName() : StatCollector.translateToLocal(this.igniter.getInvName());
        this.fontRenderer.drawString(s, 90 + this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(Properties.RES_GUI_IGNITER);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

}
