package com.siesque.rilipackcore.block.entity.multiblock;

import com.siesque.rilipackcore.block.entity.RilipackcoreBlockEntities;
import com.siesque.rilipackcore.block.entity.capability.EnergyStorage;
import com.siesque.rilipackcore.multiblock.capability.IO;
import com.siesque.rilipackcore.multiblock.capability.PartAbility;
import com.siesque.rilipackcore.multiblock.part.IMultiPart;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EnergyInputHatchEntity extends BlockEntity implements IMultiPart {
    private final EnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> energyCapability;

    public EnergyInputHatchEntity(BlockPos pos, BlockState state) {
        super(RilipackcoreBlockEntities.ENERGY_INPUT_HATCH.get(), pos, state);
        this.energyStorage = new EnergyStorage(10000, 256);
        this.energyCapability = LazyOptional.of(() -> energyStorage);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnergyInputHatchEntity entity) {
        // Handle energy transfer here if needed
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putLong("energy", energyStorage.getEnergyStored());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        energyStorage.setEnergy(tag.getLong("energy"));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCapability.invalidate();
    }

    // IMultiPart implementation
    @Override
    public BlockEntity self() {
        return this;
    }

    @Override
    public PartAbility[] getAbilities() {
        return new PartAbility[]{PartAbility.ENERGY_INPUT};
    }

    @Override
    public IO getIO() {
        return IO.INPUT;
    }

    @Override
    public boolean isController() {
        return false;
    }

    @Override
    public BlockPos getPos() {
        return worldPosition;
    }
}