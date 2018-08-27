package cadiboo.wiptech.capability.energy;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum EnergyNetworkList {

	INSTANCE;

	private final ArrayList<EnergyNetwork> networks;

	EnergyNetworkList() {
		networks = new ArrayList<>(0);
	}

	public ArrayList<EnergyNetwork> getNetworks() {
		return networks;
	}

	@Nullable
	public EnergyNetwork getNetworkFor(World world, BlockPos position) {
		refresh();
		for (EnergyNetwork network : networks) {
			if (network.getWorld() != world)
				continue;
			for (BlockPos connectionPosition : network.getPositions()) {
				if (connectionPosition.equals(position))
					return network;
			}
		}

		return null;
	}

	@Nonnull
	public EnergyNetwork getOrCreateNetworkFor(World world, BlockPos position, ModEnergyStorage energy) {
		refresh();
		EnergyNetwork foundNetwork = getNetworkFor(world, position);
		if (foundNetwork == null) {

			for (EnergyNetwork network : networks) {
				if (network.getWorld() != world)
					continue;
				for (BlockPos connectionPosition : network.getPositions()) {
					for (EnumFacing facing : EnumFacing.VALUES) {
						BlockPos pos = position.offset(facing);
						if (connectionPosition.equals(pos))
							return network;
					}
				}
			}

			foundNetwork = createNetwork(world, position, energy);
		}
		return foundNetwork;
	}

	private EnergyNetwork createNetwork(World world, BlockPos position, ModEnergyStorage energy) {
		refresh();
		EnergyNetwork network = new EnergyNetwork(world).add(position, energy);
		if (!world.isRemote)
			getNetworks().add(network);
		return network;
	}

	public void refresh() {
//		getNetworks().clear();
		for (int i = 0; i < getNetworks().size(); i++) {
			if (getNetworks().get(i).getConnections().size() > 0)
				continue;
			getNetworks().remove(i);
		}
	}

	public void reload() {
		refresh();
		final ArrayList<EnergyNetwork> networksCopy = new ArrayList<EnergyNetwork>(getNetworks());
		getNetworks().clear();
		for (EnergyNetwork network : networksCopy) {
			for (BlockPos position : network.getPositions()) {
				this.getOrCreateNetworkFor(network.getWorld(), position, network.getEnergyStorage(position));
			}
			network = null;
		}
	}

}
