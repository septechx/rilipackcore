package com.siesque.rilipackcore.multiblock.controller;

import com.siesque.rilipackcore.multiblock.part.IMultiPart;
import com.siesque.rilipackcore.multiblock.pattern.BlockPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Collection;
import java.util.List;

public interface IMultiblockController {
    BlockPattern getPattern();

    void onStructureFormed();

    void onStructureInvalid();

    boolean isFormed();

    Direction getFrontFacing();

    List<Component> getDisplayText();

    void setDisplayText(List<Component> text);

    Collection<IMultiPart> getParts();

    List<IEnergyStorage> getEnergyContainers();

    BlockPos getPos();

    Level getLevel();

    BlockEntity getBlockEntity();

    void tick();

    void save(CompoundTag tag);

    void load(CompoundTag tag);
}