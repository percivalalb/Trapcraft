package trapcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import trapcraft.api.Constants;
import trapcraft.inventory.IgniterContainer;

public class IgniterScreen extends AbstractContainerScreen<IgniterContainer> {

    public IgniterScreen(IgniterContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageWidth = 238;
        this.imageHeight = 89;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // TODO 93 + this.xSize / 2 - this.font.getStringWidth(this.getTitle()) / 2
        graphics.drawString(this.font, this.title, 93 + this.imageWidth / 2 - 18, 10, 4210752, false);

    }

    @Override
    protected void renderBg(GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {
        final int k = (this.width - this.imageWidth) / 2;
        final int l = (this.height - this.imageHeight) / 2;
        graphics.blit(Constants.RES_GUI_IGNITER, k, l, 0, 0, this.imageWidth, this.imageHeight);
    }

}
