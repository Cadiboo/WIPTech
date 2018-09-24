package cadiboo.wiptech.capability.energy;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyUserAdvanced extends IEnergyUser {

	default void transferEnergyToAllAround() {
		if (!canTransferEnergyToAllAround()) {
			return;
		}

		getConnectedSides().forEach(side -> {
			transferEnergyTo(side, (int) ((float) getEnergy().getEnergyStored() / (float) getConnectedSides().size()), false);
		});

	}

	default boolean canTransferEnergyToAllAround() {
		if (getWorld().isRemote) {
			return false;
		}

		if (!getEnergy().canExtract()) {
			return false;
		}

		if (getEnergy().getEnergyStored() <= 0) {
			return false;
		}
		return true;
	}

	default ArrayList<EnumFacing> getConnectedSides() {
		final ArrayList<EnumFacing> connectedSides = new ArrayList<>();

		for (final EnumFacing side : EnumFacing.VALUES) {
			if (isConnectedTo(side)) {
				connectedSides.add(side);
			}
		}

		return connectedSides;
	}

	default int transferEnergyTo(final EnumFacing side, final int energyToTransfer, final boolean simulate) {
		if (!canTransferEnergyTo(side, energyToTransfer)) {
			return 0;
		}
		final IEnergyStorage storage = getWorld().getTileEntity(getPosition().offset(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
		return getEnergy().extractEnergy(storage.receiveEnergy(energyToTransfer, simulate), simulate);
	}

	default boolean canTransferEnergyTo(final EnumFacing side, final int energyToTransfer) {
		if (!getEnergy().canExtract()) {
			return false;
		}

		if (getWorld() == null) {
			return false;
		}

		if (getWorld().isRemote) {
			return false;
		}

		if (getWorld().getTileEntity(getPosition().offset(side)) == null) {
			return false;
		}

		final IEnergyStorage storage = getWorld().getTileEntity(getPosition().offset(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite());

		if (storage == null) {
			return false;
		}

		if (!storage.canReceive()) {
			return false;
		}

		return true;
	}

	BlockPos getPosition();

	World getWorld();

	default boolean isConnectedTo(final EnumFacing side) {
		if (getWorld() == null) {
			return false;
		}

		final TileEntity tile = this.getWorld().getTileEntity(getPosition().offset(side));

		if (tile == null) {
			return false;
		}

		return tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()) != null;
	}

}
