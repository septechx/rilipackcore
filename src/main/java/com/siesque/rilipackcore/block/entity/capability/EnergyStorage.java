package com.siesque.rilipackcore.block.entity.capability;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorage implements IEnergyStorage {
    protected long energy;
    protected long capacity;
    protected long maxReceive;
    protected long maxExtract;

    public EnergyStorage(long capacity, long maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public EnergyStorage(long capacity, long maxReceive, long maxExtract, long energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = energy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        long energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            this.energy += energyReceived;
        }
        return (int) energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        long energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            this.energy -= energyExtracted;
        }
        return (int) energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return (int) this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) this.capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    public void setEnergy(long energy) {
        this.energy = energy;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }
}