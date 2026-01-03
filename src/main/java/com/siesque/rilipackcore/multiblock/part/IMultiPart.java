package com.siesque.rilipackcore.multiblock.part;

import com.siesque.rilipackcore.multiblock.capability.IPartAbility;
import com.siesque.rilipackcore.multiblock.capability.IO;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IMultiPart {
    BlockEntity self();
    IPartAbility[] getAbilities();
    IO getIO();
    boolean isController();
    BlockPos getPos();
}