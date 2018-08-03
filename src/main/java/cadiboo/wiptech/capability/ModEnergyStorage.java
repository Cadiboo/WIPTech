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
		int toRecieve = Math.min(energy, this.maxReceive);
		this.energy = toRecieve;
		return toRecieve - this.maxReceive;
	}

}
