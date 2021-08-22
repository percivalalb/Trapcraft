package trapcraft.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import trapcraft.api.Constants;
import trapcraft.inventory.IgniterContainer;

public class IgniterScreen extends AbstractContainerScreen<IgniterContainer> {

    public IgniterScreen(IgniterContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageWidth = 238;
        this.imageHeight = 89;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        // TODO 93 + this.xSize / 2 - this.font.getStringWidth(this.getTitle()) / 2
        this.font.draw(matrixStack, this.title, 93 + this.imageWidth / 2F - 18, 10, 4210752);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, Constants.RES_GUI_IGNITER);
        this.minecraft.getTextureManager().bindForSetup(Constants.RES_GUI_IGNITER);
        final int k = (this.width - this.imageWidth) / 2;
        final int l = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
    }

}
