package com.siesque.rilipackcore.block.entity.multiblock;

import com.siesque.rilipackcore.block.entity.RilipackcoreBlockEntities;
import com.siesque.rilipackcore.block.entity.capability.EnergyStorage;
import com.siesque.rilipackcore.multiblock.capability.IO;
import com.siesque.rilipackcore.multiblock.capability.PartAbility;
import com.siesque.rilipackcore.multiblock.cleanroom.CleanroomController;
import com.siesque.rilipackcore.multiblock.part.IMultiPart;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CleanroomControllerEntity extends BlockEntity implements IMultiPart {
    private final CleanroomController controller;
    private final LazyOptional<IEnergyStorage> energyCapability;

    public CleanroomControllerEntity(BlockPos pos, BlockState state) {
        super(RilipackcoreBlockEntities.CLEANROOM_CONTROLLER.get(), pos, state);
        this.controller = new CleanroomController(this);
        this.energyCapability = LazyOptional.of(this::getEnergyStorage);
    }

    private IEnergyStorage getEnergyStorage() {
        long totalEnergy = 0;
        long totalCapacity = 0;
        for (IEnergyStorage storage : controller.getEnergyContainers()) {
            totalEnergy += storage.getEnergyStored();
            totalCapacity += storage.getMaxEnergyStored();
        }
        return new EnergyStorage(totalCapacity, totalCapacity, totalCapacity, totalEnergy);
    }

    public void tick() {
        if (controller != null) {
            controller.tick();
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (controller != null) {
            controller.save(tag);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (controller != null) {
            controller.load(tag);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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

    @Override
    public BlockEntity self() {
        return this;
    }

    @Override
    public PartAbility[] getAbilities() {
        return new PartAbility[0];
    }

    @Override
    public IO getIO() {
        return IO.NONE;
    }

    @Override
    public boolean isController() {
        return true;
    }

    @Override
    public BlockPos getPos() {
        return worldPosition;
    }

    @Nullable
    public CleanroomController getController() {
        return controller;
    }

    public boolean isFormed() {
        return controller != null && controller.isFormed();
    }

    public Direction getFrontFacing() {
        return controller != null ? controller.getFrontFacing() : Direction.NORTH;
    }

    public List<Component> getDisplayText() {
        return controller != null ? controller.getDisplayText() : List.of();
    }

    public void onStructureFormed() {
        if (controller != null) {
            controller.onStructureFormed();
        }
    }

    public void onStructureInvalid() {
        if (controller != null) {
            controller.onStructureInvalid();
        }
    }
}