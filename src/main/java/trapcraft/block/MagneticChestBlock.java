package trapcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import trapcraft.TrapcraftTileEntityTypes;
import trapcraft.block.tileentity.BearTrapTileEntity;
import trapcraft.block.tileentity.MagneticChestTileEntity;

import javax.annotation.Nullable;

/**
 * @author ProPercivalalb
 **/
public class MagneticChestBlock extends ChestBlock {

    public MagneticChestBlock() {
        super(Block.Properties.of(Material.WOOD).strength(2.5F, 2.0F).sound(SoundType.WOOD), TrapcraftTileEntityTypes.MAGNETIC_CHEST::get);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.WEST));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        final Direction direction = context.getHorizontalDirection().getOpposite();
        final FluidState ifluidstate = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState().setValue(FACING, direction).setValue(TYPE, ChestType.SINGLE).setValue(WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
     }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new MagneticChestTileEntity(pos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, TrapcraftTileEntityTypes.MAGNETIC_CHEST.get(), level.isClientSide ? MagneticChestTileEntity::clientTick : MagneticChestTileEntity::tick);
    }

    @Override
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST); //TODO
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isClientSide && entityIn.isAlive()) {
            if (entityIn instanceof ItemEntity) {
                MagneticChestTileEntity tileEntityMagneticChest = (MagneticChestTileEntity)worldIn.getBlockEntity(pos);
                tileEntityMagneticChest.insertStackFromEntity((ItemEntity)entityIn);
            }
        }
    }
}
