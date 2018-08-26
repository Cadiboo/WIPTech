package cadiboo.wiptech.capability;

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

	public int setEnergy(int energy) {
		onEnergyChanged();
		int toRecieve = Math.min(energy, this.maxReceive);
		this.energy = toRecieve;
		return toRecieve - this.maxReceive;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
		this.onEnergyChanged();
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int removed = super.extractEnergy(maxExtract, simulate);
		if (!simulate && removed > 0)
			onEnergyChanged();
		return removed;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int recieved = super.receiveEnergy(maxReceive, simulate);
		if (!simulate && recieved > 0)
			onEnergyChanged();
		return recieved;
	}

	public void onEnergyChanged() {
	}

}
