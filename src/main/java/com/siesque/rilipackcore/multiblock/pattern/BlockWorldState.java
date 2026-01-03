package com.siesque.rilipackcore.multiblock.pattern;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockWorldState implements IBlockWorldState {
    private final Level world;
    private final BlockPos pos;
    private final Direction facing;
    private final Direction upDirection;
    private final boolean isFlipped;
    private BlockState blockState;
    private BlockEntity blockEntity;

    public BlockWorldState(Level world, BlockPos pos, Direction facing, Direction upDirection, boolean isFlipped) {
        this.world = world;
        this.pos = pos;
        this.facing = facing;
        this.upDirection = upDirection;
        this.isFlipped = isFlipped;
    }

    @Override
    public Level getWorld() {
        return world;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public BlockState getBlockState() {
        if (blockState == null) {
            blockState = world.getBlockState(pos);
        }
        return blockState;
    }

    @Override
    public BlockEntity getBlockEntity() {
        if (blockEntity == null) {
            blockEntity = world.getBlockEntity(pos);
        }
        return blockEntity;
    }

    @Override
    public MatchContext getMatchContext() {
        return null;
    }

    @Override
    public Direction getFacing() {
        return facing;
    }

    @Override
    public Direction getUpDirection() {
        return upDirection;
    }

    @Override
    public boolean isFlipped() {
        return isFlipped;
    }
}