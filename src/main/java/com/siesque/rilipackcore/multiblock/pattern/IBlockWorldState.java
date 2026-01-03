package com.siesque.rilipackcore.multiblock.pattern;

import com.siesque.rilipackcore.multiblock.pattern.util.RelativeDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockWorldState {
    Level getWorld();
    BlockPos getPos();
    BlockState getBlockState();
    BlockEntity getBlockEntity();
    MatchContext getMatchContext();
    Direction getFacing();
    Direction getUpDirection();
    boolean isFlipped();

    default BlockPos getPosFromRelative(int leftOffset, int upOffset, int frontOffset) {
        return RelativeDirection.offsetPos(getPos(), getFacing(), getUpDirection(), isFlipped(), upOffset, leftOffset, frontOffset);
    }
}