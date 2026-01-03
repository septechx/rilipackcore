package com.siesque.rilipackcore.multiblock.pattern;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public record BlockInfo(BlockState blockState, BlockEntity blockEntity) {
    public static BlockInfo of(BlockState state) {
        return new BlockInfo(state, null);
    }

    public static BlockInfo of(BlockState state, BlockEntity be) {
        return new BlockInfo(state, be);
    }

    public Block getBlock() {
        return blockState.getBlock();
    }
}