package cadiboo.wiptech.capability.energy;

import net.minecraftforge.energy.EnergyStorage;

public class ModEnergyStorage extends EnergyStorage {

	public ModEnergyStorage(int capacity) {
		super(capacity);
	}

	public ModEnergyStorage(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public ModEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public ModEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	public int getMaxReceive() {
		return maxReceive;
	}

	public void setMaxReceive(int maxReceive) {
		this.maxReceive = maxReceive;
		this.onEnergyChanged();
	}

	public int getMaxExtract() {
		return maxExtract;
	}

	public void setMaxExtract(int maxExtract) {
		this.maxExtract = maxExtract;
		this.onEnergyChanged();
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
		this.onEnergyChanged();
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!canReceive())
			return 0;

		int energyReceived = Math.min(getCapacity() - getEnergyStored(), Math.min(getMaxReceive(), maxReceive));
		if (!simulate) {
			setEnergyStored(getEnergyStored() + energyReceived, false);
			onEnergyChanged();
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (!canExtract())
			return 0;

		int energyExtracted = Math.min(getEnergyStored(), Math.min(getMaxExtract(), maxExtract));
		if (!simulate)
			setEnergyStored(getEnergyStored() - energyExtracted, false);
		return energyExtracted;
	}

	@Override
	public int getMaxEnergyStored() {
		return getCapacity();
	}

	@Override
	public boolean canExtract() {
		return getMaxExtract() > 0;
	}

	@Override
	public boolean canReceive() {
		return getMaxReceive() > 0;
	}

	@Override
	public int getEnergyStored() {
		return energy;
	}

	// TODO
	/**
	 * @param energy the energy to set
	 * @return the amount of energy that was set
	 */
	public int setEnergyStored(int energy, boolean simulate) {
		onEnergyChanged();
		int toSet = Math.min(energy, getMaxReceive());
		if (!simulate)
			this.energy = toSet;
		return toSet;
	}

	public void onEnergyChanged() {
	}

}
