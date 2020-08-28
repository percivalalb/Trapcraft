package trapcraft.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
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
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

	@Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
	    // TODO 93 + this.xSize / 2 - this.font.getStringWidth(this.getTitle()) / 2
        this.font.func_243248_b(matrixStack, this.title, 93 + this.xSize / 2F - 18, 10, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(Constants.RES_GUI_IGNITER);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.xSize, this.ySize);
    }

}
