package trapcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import trapcraft.api.Constants;
import trapcraft.inventory.IgniterContainer;

public class IgniterScreen extends ContainerScreen<IgniterContainer> {

	public IgniterScreen(IgniterContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.xSize = 238;
		this.ySize = 89;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.getTitle().getFormattedText();
        this.font.drawString(s, 93 + this.xSize / 2 - this.font.getStringWidth(s) / 2, 10, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(Constants.RES_GUI_IGNITER);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.blit(k, l, 0, 0, this.xSize, this.ySize);
    }

}
