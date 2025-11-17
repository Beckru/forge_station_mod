package net.beckru.forge_station_mod.block.custom;

import net.beckru.forge_station_mod.block.entity.custom.ForgeStationBasicEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class forge_station_basic extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 13, 14);

    public forge_station_basic(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ForgeStationBasicEntity(pPos, pState);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean movedByPiston) {

        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof ForgeStationBasicEntity entity) {
                entity.drops();
                level.updateNeighborsAt(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {

        if (level.isClientSide) return InteractionResult.SUCCESS;

        ItemStack held = player.getItemInHand(hand);

        if (level.getBlockEntity(pos) instanceof ForgeStationBasicEntity entity) {

            // ---- INSERTAR ----
            if (!held.isEmpty()) {

                // SLOT 0 → solo si está vacío
                if (entity.inventory.getStackInSlot(0).isEmpty()) {
                    entity.inventory.insertItem(0, held.copy(), false);
                    held.shrink(1);
                    return InteractionResult.SUCCESS;
                }

                // SLOT 1 → stack completo
                ItemStack leftover = entity.inventory.insertItem(1, held.copy(), false);
                if (leftover.getCount() != held.getCount()) {
                    held.setCount(leftover.getCount());
                    return InteractionResult.SUCCESS;
                }
            }

            // ---- RETIRAR ----
            else {

                // Primero sacar slot 0
                ItemStack extracted0 = entity.inventory.extractItem(0, 1, false);
                if (!extracted0.isEmpty()) {
                    player.setItemInHand(hand, extracted0);
                    return InteractionResult.SUCCESS;
                }

                // Luego slot 1
                ItemStack extracted1 = entity.inventory.extractItem(1, 64, false);
                if (!extracted1.isEmpty()) {
                    player.setItemInHand(hand, extracted1);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}
