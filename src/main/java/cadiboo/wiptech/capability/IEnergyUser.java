package cadiboo.wiptech.capability;

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
		if (!getEnergy().canExtract())
			return;

		if (getEnergy().getEnergyStored() <= 0)
			return;

		getConnectedSides().forEach(side -> {
			transferEnergyTo(side, getConnectedSides().size());
		});

	}

	default public ArrayList<EnumFacing> getConnectedSides() {
		ArrayList<EnumFacing> connectedSides = new ArrayList<>();

		for (EnumFacing side : EnumFacing.VALUES)
			if (isConnectedTo(side))
				connectedSides.add(side);

		return connectedSides;
	}

	default public int transferEnergyTo(EnumFacing side, int energyToTransfer) {
		if (!getEnergy().canExtract())
			return 0;

		if (getWorld() == null)
			return 0;

		if (getWorld().getTileEntity(getPosition().offset(side)) == null)
			return 0;

		IEnergyStorage storage = getWorld().getTileEntity(getPosition().offset(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite());

		if (storage == null)
			return 0;

		if (!storage.canReceive())
			return 0;

		return getEnergy().extractEnergy(storage.receiveEnergy(Math.round(getEnergy().getEnergyStored() / energyToTransfer), false), false);
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
