package trapcraft.client.renders;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trapcraft.api.Properties;

@SideOnly(Side.CLIENT)
public class ItemRenderHelperMagneticChest implements IItemRenderer {

    private ModelChest modelChest;

    public ItemRenderHelperMagneticChest() {
        modelChest = new ModelChest();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        switch (type) {
            case ENTITY: {
                rendeMagneticChest(0.5F, 0.5F, 0.5F);
                break;
            }
            case EQUIPPED: {
                rendeMagneticChest(1.0F, 1.0F, 1.0F);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                rendeMagneticChest(1.0F, 1.0F, 1.0F);
                break;
            }
            case INVENTORY: {
                rendeMagneticChest(0.0F, 0.075F, 0.0F);
                break;
            }
            default:
                break;
        }
    }

    private void rendeMagneticChest(float x, float y, float z) {
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(Properties.RES_BLOCK_MAGNETIC_CHEST);
        GL11.glPushMatrix(); //start
        GL11.glTranslatef(x, y, z); //size
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(-90, 0, 1, 0);
        modelChest.renderAll();
        GL11.glPopMatrix(); //end
    }
}
