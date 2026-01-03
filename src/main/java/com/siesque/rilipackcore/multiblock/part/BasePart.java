package com.siesque.rilipackcore.multiblock.part;

import com.siesque.rilipackcore.multiblock.capability.IPartAbility;
import com.siesque.rilipackcore.multiblock.capability.IO;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BasePart extends BlockEntity implements IMultiPart {
    protected IO io = IO.BOTH;

    public BasePart(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public IPartAbility[] getAbilities() {
        return getSupportedAbilities();
    }

    @Override
    public IO getIO() {
        return io;
    }

    public void setIO(IO io) {
        this.io = io;
    }

    @Override
    public boolean isController() {
        return false;
    }

    @Override
    public BlockPos getPos() {
        return worldPosition;
    }

    @Override
    public BlockEntity self() {
        return this;
    }

    protected abstract IPartAbility[] getSupportedAbilities();
}