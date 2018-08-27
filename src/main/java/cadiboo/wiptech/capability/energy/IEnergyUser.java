package cadiboo.wiptech.capability.energy;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyUser extends ICapabilityProvider {

	@Nonnull
	public ModEnergyStorage getEnergy();

	default public void transferEnergyToAllAround() {
		if (!canTransferEnergyToAllAround())
			return;

		getConnectedSides().forEach(side -> {
			transferEnergyTo(side, (int) ((float) getEnergy().getEnergyStored() / (float) getConnectedSides().size()), false);
		});

	}

	public default boolean canTransferEnergyToAllAround() {
		if (getWorld().isRemote)
			return false;

		if (!getEnergy().canExtract())
			return false;

		if (getEnergy().getEnergyStored() <= 0)
			return false;
		return true;
	}

	default public ArrayList<EnumFacing> getConnectedSides() {
		ArrayList<EnumFacing> connectedSides = new ArrayList<>();

		for (EnumFacing side : EnumFacing.VALUES)
			if (isConnectedTo(side))
				connectedSides.add(side);

		return connectedSides;
	}

	default public int transferEnergyTo(EnumFacing side, int energyToTransfer, boolean simulate) {
		if (!canTransferEnergyTo(side, energyToTransfer))
			return 0;
		IEnergyStorage storage = getWorld().getTileEntity(getPosition().offset(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
		return getEnergy().extractEnergy(storage.receiveEnergy(energyToTransfer, simulate), simulate);
	}

	public default boolean canTransferEnergyTo(EnumFacing side, int energyToTransfer) {
		if (!getEnergy().canExtract())
			return false;

		if (getWorld() == null)
			return false;

		if (getWorld().isRemote)
			return false;

		if (getWorld().getTileEntity(getPosition().offset(side)) == null)
			return false;

		IEnergyStorage storage = getWorld().getTileEntity(getPosition().offset(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite());

		if (storage == null)
			return false;

		if (!storage.canReceive())
			return false;

		return true;
	}

	public BlockPos getPosition();

	public World getWorld();

	default public boolean isConnectedTo(EnumFacing side) {
		if (getWorld() == null)
			return false;

		TileEntity tile = this.getWorld().getTileEntity(getPosition().offset(side));

		if (tile == null)
			return false;

		return tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()) != null;
	}

}
