package trapcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.ChestType;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import trapcraft.TrapcraftTileEntityTypes;
import trapcraft.block.tileentity.MagneticChestTileEntity;

/**
 * @author ProPercivalalb
 **/
public class MagneticChestBlock extends ChestBlock {

    public MagneticChestBlock() {
        super(Block.Properties.of(Material.WOOD).strength(2.5F, 2.0F).sound(SoundType.WOOD), TrapcraftTileEntityTypes.MAGNETIC_CHEST::get);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.WEST));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        final Direction direction = context.getHorizontalDirection().getOpposite();
        final FluidState ifluidstate = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState().setValue(FACING, direction).setValue(TYPE, ChestType.SINGLE).setValue(WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
     }

    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new MagneticChestTileEntity();
    }

    @Override
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST); //TODO
    }

    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isClientSide && entityIn.isAlive()) {
            if (entityIn instanceof ItemEntity) {
                MagneticChestTileEntity tileEntityMagneticChest = (MagneticChestTileEntity)worldIn.getBlockEntity(pos);
                tileEntityMagneticChest.insertStackFromEntity((ItemEntity)entityIn);
            }
        }
    }
}
