package cadiboo.wiptech.capability.energy;

import net.minecraftforge.energy.EnergyStorage;

public class ModEnergyStorage extends EnergyStorage {

	public ModEnergyStorage(final int capacity) {
		super(capacity);
	}

	public ModEnergyStorage(final int capacity, final int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public ModEnergyStorage(final int capacity, final int maxReceive, final int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public ModEnergyStorage(final int capacity, final int maxReceive, final int maxExtract, final int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	public int getMaxReceive() {
		return this.maxReceive;
	}

	public void setMaxReceive(final int maxReceive) {
		this.maxReceive = maxReceive;
		this.onEnergyChanged();
	}

	public int getMaxExtract() {
		return this.maxExtract;
	}

	public void setMaxExtract(final int maxExtract) {
		this.maxExtract = maxExtract;
		this.onEnergyChanged();
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(final int capacity) {
		this.capacity = capacity;
		this.onEnergyChanged();
	}

	@Override
	public int receiveEnergy(final int maxReceive, final boolean simulate) {
		if (!this.canReceive()) {
			return 0;
		}

		final int energyReceived = Math.min(this.getCapacity() - this.getEnergyStored(), Math.min(this.getMaxReceive(), maxReceive));
		if (!simulate) {
			this.setEnergyStored(this.getEnergyStored() + energyReceived, false);
			this.onEnergyChanged();
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(final int maxExtract, final boolean simulate) {
		if (!this.canExtract()) {
			return 0;
		}

		final int energyExtracted = Math.min(this.getEnergyStored(), Math.min(this.getMaxExtract(), maxExtract));
		if (!simulate) {
			this.setEnergyStored(this.getEnergyStored() - energyExtracted, false);
		}
		return energyExtracted;
	}

	@Override
	public int getMaxEnergyStored() {
		return this.getCapacity();
	}

	@Override
	public boolean canExtract() {
		return this.getMaxExtract() > 0;
	}

	@Override
	public boolean canReceive() {
		return this.getMaxReceive() > 0;
	}

	@Override
	public int getEnergyStored() {
		return this.energy;
	}

	// TODO
	/**
	 * @param  energy the energy to set
	 * @return        the amount of energy that was set
	 */
	public int setEnergyStored(final int energy, final boolean simulate) {
		this.onEnergyChanged();
		final int toSet = Math.min(energy, this.getMaxReceive());
		if (!simulate) {
			this.energy = toSet;
		}
		return toSet;
	}

	public void onEnergyChanged() {
	}

}
