package cadiboo.wiptech.capability;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;

public class SidedModEnergyStorage extends ModEnergyStorage {

	private final ArrayList<ModEnergyStorage> storages = new ArrayList<ModEnergyStorage>(EnumFacing.VALUES.length);
	private ModEnergyStorage lastRecieved;
	private ModEnergyStorage lastExtracted;

	public SidedModEnergyStorage(int capacity, EnumFacing... sides) {
		super(capacity);
		setup(sides);

	}

	public SidedModEnergyStorage(int capacity, int maxTransfer, EnumFacing... sides) {
		super(capacity, maxTransfer);
		setup(sides);
	}

	public SidedModEnergyStorage(int capacity, int maxReceive, int maxExtract, EnumFacing... sides) {
		super(capacity, maxReceive, maxExtract);
		setup(sides);
	}

	public SidedModEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, EnumFacing... sides) {
		super(capacity, maxReceive, maxExtract, energy);
		setup(sides);
	}

	public ModEnergyStorage getLastRecieved() {
		return lastRecieved;
	}

	public ModEnergyStorage getLastExtracted() {
		return lastExtracted;
	}

	private void setup(EnumFacing[] sides) {
		final ArrayList<EnumFacing> sidesList = new ArrayList<EnumFacing>(sides.length);
		for (EnumFacing side : sides) {
			sidesList.add(side);
		}

		for (EnumFacing facing : EnumFacing.VALUES) {
			if (!sidesList.contains(facing)) {
				storages.add(facing.getIndex(), null);
				continue;
			}
			storages.add(facing.getIndex(), new ModEnergyStorage(0) {
				@Override
				public boolean canExtract() {
					return SidedModEnergyStorage.this.canExtract();
				}

				@Override
				public boolean canReceive() {
					return SidedModEnergyStorage.this.canReceive();
				}

				@Override
				public int extractEnergy(int maxExtract, boolean simulate) {
					lastExtracted = this;
					return SidedModEnergyStorage.this.extractEnergy(maxExtract, simulate);
				}

				@Override
				public int getEnergyStored() {
					return SidedModEnergyStorage.this.getEnergyStored();
				}

				@Override
				public int getMaxEnergyStored() {
					return SidedModEnergyStorage.this.getMaxEnergyStored();
				}

				@Override
				public int receiveEnergy(int maxReceive, boolean simulate) {
					lastRecieved = this;
					return SidedModEnergyStorage.this.receiveEnergy(maxReceive, simulate);
				}

				@Override
				public int setEnergy(int energy) {
					return SidedModEnergyStorage.this.setEnergy(energy);
				}
			});
		}
	}

	@Nullable
	public ModEnergyStorage getStorage(EnumFacing side) {
		if (side == null)
			return this;
		return storages.get(side.getIndex());
	}

}
