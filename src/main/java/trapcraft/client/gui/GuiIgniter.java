package trapcraft.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import trapcraft.api.Properties;
import trapcraft.inventory.ContainerIgniter;
import trapcraft.tileentity.TileEntityIgniter;

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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s = this.igniter.inventory.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, 90 + this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Properties.RES_GUI_IGNITER);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

}
