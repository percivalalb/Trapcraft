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
    	super(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.5F, 2.0F).sound(SoundType.WOOD), () -> TrapcraftTileEntityTypes.MAGNETIC_CHEST.get());
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.WEST));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getPlacementHorizontalFacing().getOpposite();
        FluidState ifluidstate = context.getWorld().getFluidState(context.getPos());

        return this.getDefaultState().with(FACING, direction).with(TYPE, ChestType.SINGLE).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
     }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
    	return new MagneticChestTileEntity();
	}

    @Override
    protected Stat<ResourceLocation> getOpenStat() {
    	return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST); //TODO
	}

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    	if(!worldIn.isRemote && entityIn.isAlive()) {
            if (entityIn instanceof ItemEntity) {
                MagneticChestTileEntity tileEntityMagneticChest = (MagneticChestTileEntity)worldIn.getTileEntity(pos);
                tileEntityMagneticChest.insertStackFromEntity((ItemEntity)entityIn);
            }
        }
    }
}
