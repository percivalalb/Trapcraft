package trapcraft.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import trapcraft.api.Constants;

public class TrapcraftBlockstateProvider extends BlockStateProvider {

    public TrapcraftBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Constants.MOD_ID, exFileHelper);
    }

    public ExistingFileHelper getExistingHelper() {
        return this.models().existingFileHelper;
    }

    @Override
    public String getName() {
        return "Trapcraft Blockstates/Block Models";
    }

    @Override
    protected void registerStatesAndModels() {

    }

}
