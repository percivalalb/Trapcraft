package trapcraft.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.item.Item;
import net.minecraft.world.item.ItemStack;
import trapcraft.TrapcraftBlocks;
import trapcraft.block.tileentity.MagneticChestTileEntity;

public class ItemStackTileEntityMagneticChestRenderer extends BlockEntityWithoutLevelRenderer {

    private static MagneticChestTileEntity chestBasic;

    @Override
    //public void render(ItemStack stack, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType p_239207_2_, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() == TrapcraftBlocks.MAGNETIC_CHEST_ITEM.get()) {
            BlockEntityRenderDispatcher.instance.renderItem(chestBasic, matrixStack, buffer, combinedLight, combinedOverlay);
        }
    }

    public static void setDummyTE() {
        ItemStackTileEntityMagneticChestRenderer.chestBasic = new MagneticChestTileEntity();
    }
}
