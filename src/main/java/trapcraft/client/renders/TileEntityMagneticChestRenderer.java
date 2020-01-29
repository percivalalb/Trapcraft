package trapcraft.client.renders;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.client.renderer.tileentity.model.LargeChestModel;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trapcraft.api.Constants;
import trapcraft.tileentity.TileEntityMagneticChest;

@OnlyIn(Dist.CLIENT)
public class TileEntityMagneticChestRenderer extends TileEntityRenderer<TileEntityMagneticChest> {

    private final ChestModel simpleChest = new ChestModel();
    private final ChestModel largeChest = new LargeChestModel();

    @Override
    public void render(TileEntityMagneticChest tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        BlockState blockstate = tileEntityIn.hasWorld() ? tileEntityIn.getBlockState() : Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        ChestType chesttype = blockstate.has(ChestBlock.TYPE) ? blockstate.get(ChestBlock.TYPE) : ChestType.SINGLE;
        if (chesttype != ChestType.LEFT) {
            boolean flag = chesttype != ChestType.SINGLE;
            ChestModel chestmodel = this.getChestModel(tileEntityIn, destroyStage, flag);
            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.scalef(flag ? 8.0F : 4.0F, 4.0F, 1.0F);
                GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
                GlStateManager.matrixMode(5888);
            } else {
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.translatef((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GlStateManager.scalef(1.0F, -1.0F, -1.0F);
            float f = blockstate.get(ChestBlock.FACING).getHorizontalAngle();
            if (Math.abs(f) > 1.0E-5D) {
                GlStateManager.translatef(0.5F, 0.5F, 0.5F);
                GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
            }

            this.applyLidRotation(tileEntityIn, partialTicks, chestmodel);
            chestmodel.renderAll();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }

    public ChestModel getChestModel(TileEntityMagneticChest magneticChest, int destoryStage, boolean doubleChest) {
        ResourceLocation resourcelocation;
        if (destoryStage >= 0) {
            resourcelocation = DESTROY_STAGES[destoryStage];
        } else {
            resourcelocation = doubleChest ? Constants.RES_BLOCK_MAGNETIC_CHEST : Constants.RES_BLOCK_MAGNETIC_CHEST;
        }

        this.bindTexture(resourcelocation);
        return doubleChest ? this.largeChest : this.simpleChest;
	}

    private void applyLidRotation(TileEntityMagneticChest p_199346_1_, float p_199346_2_, ChestModel p_199346_3_) {
        float f = ((IChestLid)p_199346_1_).getLidAngle(p_199346_2_);
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        p_199346_3_.getLid().rotateAngleX = -(f * ((float)Math.PI / 2F));
     }
}