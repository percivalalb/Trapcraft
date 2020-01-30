package trapcraft.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import trapcraft.api.Constants;

public class TrapcraftItemModelProvider extends ItemModelProvider {

    public TrapcraftItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Trapcraft Item Models";
    }

    @Override
    protected void registerModels() {

    }
}
