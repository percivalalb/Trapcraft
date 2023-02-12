package trapcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkHooks;
import trapcraft.TrapcraftTileEntityTypes;
import trapcraft.block.tileentity.IgniterTileEntity;

import javax.annotation.Nullable;

/**
 * @author ProPercivalalb
 **/
public class IgniterBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public IgniterBlock() {
        super(Block.Properties.of(Material.STONE).strength(3.5F, 2.0F).sound(SoundType.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.WEST));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            final IgniterTileEntity tileentityigniter = (IgniterTileEntity)worldIn.getBlockEntity(pos);

            if (tileentityigniter != null) {
                if (player instanceof ServerPlayer && !(player instanceof FakePlayer)) {
                    final ServerPlayer entityPlayerMP = (ServerPlayer) player;

                    NetworkHooks.openScreen(entityPlayerMP, tileentityigniter, pos);
                }
            }

            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        this.updateIgniterState((Level)world, pos);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!worldIn.isClientSide) {
            this.updateIgniterState(worldIn, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            this.updateIgniterState(worldIn, pos);
        }
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            if (!worldIn.isClientSide) {
                this.updateIgniterState(worldIn, pos);
            }
        }
    }

    @Override
       public BlockState getStateForPlacement(BlockPlaceContext context) {
           return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
       }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public void updateIgniterState(Level world, BlockPos pos) {
       final Direction facing = world.getBlockState(pos).getValue(FACING);

       int distance = 1, oldDistance = 1;
       BlockEntity tileEntity = world.getBlockEntity(pos);
       if (tileEntity instanceof IgniterTileEntity) {
           final IgniterTileEntity igniter = (IgniterTileEntity)world.getBlockEntity(pos);
           distance = igniter.getRangeUpgrades() + 1;
           oldDistance = igniter.lastUpgrades + 1;
       }

       updateIgniterState(world, pos, world.hasNeighborSignal(pos), facing, distance, oldDistance);

       if (tileEntity instanceof IgniterTileEntity) {
           final IgniterTileEntity igniter = (IgniterTileEntity)world.getBlockEntity(pos);
           igniter.lastUpgrades = distance - 1;
       }
    }

    private void updateIgniterState(final Level world, final BlockPos pos, final boolean powered, final Direction direction, final int newDistance, final int previousDistance) {
        // If distance has changed remove old fire
        if (newDistance != previousDistance) {
             final BlockPos oldPos = pos.relative(direction, previousDistance);
             removePossibleFire(world, oldPos);
        }

        final BlockPos firePos = pos.relative(direction, newDistance);

        if (powered) {
            BlockState fire = Blocks.FIRE.defaultBlockState();
             if (!world.getBlockState(firePos).is(Blocks.FIRE) && world.isEmptyBlock(firePos) && fire.canSurvive(world, firePos)) {
                world.setBlockAndUpdate(firePos, fire);
             }
        }
        else if (!powered) {
            removePossibleFire(world, firePos);
        }
     }

    public void removePossibleFire(final Level world, final BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == Blocks.FIRE) {
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            world.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5F, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F, true);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new IgniterTileEntity(pos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, TrapcraftTileEntityTypes.IGNITER.get(), IgniterTileEntity::tick);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            final BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof IgniterTileEntity) {
                final int upgrades = ((IgniterTileEntity) tileentity).getRangeUpgrades() + 1;
                updateIgniterState(worldIn, pos, false, state.getValue(FACING), upgrades, upgrades);

                Containers.dropContents(worldIn, pos, ((IgniterTileEntity)tileentity).inventory);

                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
}
