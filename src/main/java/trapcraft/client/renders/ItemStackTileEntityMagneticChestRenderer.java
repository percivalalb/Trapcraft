package trapcraft.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import trapcraft.TrapcraftBlocks;
import trapcraft.block.tileentity.MagneticChestTileEntity;

public class ItemStackTileEntityMagneticChestRenderer extends BlockEntityWithoutLevelRenderer {

    private MagneticChestTileEntity chestBasic;

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public ItemStackTileEntityMagneticChestRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
        this.chestBasic = new MagneticChestTileEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
        this.blockEntityRenderDispatcher = p_172550_;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType p_239207_2_, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() == TrapcraftBlocks.MAGNETIC_CHEST_ITEM.get()) {
           this.blockEntityRenderDispatcher.renderItem(chestBasic, matrixStack, buffer, combinedLight, combinedOverlay);
        }
    }
}
