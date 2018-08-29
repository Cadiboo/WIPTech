package cadiboo.wiptech.capability.energy.network;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyNetwork {

	private ModEnergyStorage energy;
	private World world;

	private HashSet<IEnergyNetworkConnection> connections;

	public EnergyNetwork(World world) {
		this();
		this.world = world;
	}

	public EnergyNetwork() {
		this.energy = new ModEnergyStorage(0) {
			@Override
			public int getCapacity() {
				int capacity = 0;
				for (ModEnergyStorage energy : getEnergyStorages()) {
					capacity += energy.getCapacity();
				}
				return capacity;
			}

			@Override
			public int getEnergyStored() {
				int energyStored = 0;
				for (ModEnergyStorage energy : getEnergyStorages()) {
					energyStored += energy.getEnergyStored();
				}
				return energyStored;
			}

			@Override
			public int getMaxExtract() {
				int maxExtract = 0;
				for (ModEnergyStorage energy : getEnergyStorages()) {
					maxExtract += energy.getMaxExtract();
				}
				return maxExtract;
			}

			@Override
			public int getMaxReceive() {
				int maxReceive = 0;
				for (ModEnergyStorage energy : getEnergyStorages()) {
					maxReceive += energy.getMaxReceive();
				}
				return maxReceive;
			}
		};
		this.connections = new HashSet<IEnergyNetworkConnection>(0);
	}

	public ModEnergyStorage getEnergy() {
		return energy;
	}

	public HashSet<IEnergyNetworkConnection> getConnections() {
		return connections;
	}

	public Set<BlockPos> getPositions() {
		HashSet<BlockPos> positions = new HashSet<>();
		for (IEnergyNetworkConnection connection : connections) {
			positions.add(connection.getPosition());
		}
		return positions;
	}

	public Collection<ModEnergyStorage> getEnergyStorages() {
		HashSet<ModEnergyStorage> storages = new HashSet<>();
		for (IEnergyNetworkConnection connection : connections) {
			storages.add(connection.getEnergy());
		}
		return storages;
	}

	public EnergyNetwork add(IEnergyNetworkConnection connection) {
		getConnections().add(connection);
		if (world == null)
			world = connection.getWorld();
		return this;
	}

	public EnergyNetwork remove(IEnergyNetworkConnection connection) {
		getConnections().remove(connection);
		return this;
	}

	public World getWorld() {
		return world;
	}

	public void update() {
		final int eachEnergy = (int) ((float) getEnergy().getEnergyStored() / (float) getConnections().size());
		int energyRemaining = getEnergy().getEnergyStored();

		int repetitions = 0;

		while (energyRemaining > 0) {
			repetitions++;
			if (repetitions > 3) {
				WIPTech.warn("repetitions went over 3 when trying to distribute energy inside the network!");
				new Exception().printStackTrace();
				break;
			}
			for (ModEnergyStorage energy : getEnergyStorages()) {
				energyRemaining -= energy.extractEnergy(eachEnergy, false);
			}

		}

	}

	@Nullable
	public EnergyNetwork tryMerge(EnergyNetwork other) {
		if (other.getWorld() != this.getWorld())
			return null;
		for (BlockPos myConnectionPosition : this.getPositions()) {
			for (BlockPos otherConnectionPosition : other.getPositions()) {
				for (EnumFacing facing : EnumFacing.VALUES) {
					BlockPos pos = myConnectionPosition.offset(facing);
					if (otherConnectionPosition.equals(pos)) {
						EnergyNetwork newNetwork = new EnergyNetwork(getWorld());
						newNetwork.getConnections().addAll(getConnections());
						newNetwork.getConnections().addAll(other.getConnections());
						return newNetwork;
					}
				}
			}
		}

		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof EnergyNetwork && ((EnergyNetwork) obj).getConnections().equals(this.getConnections());
	}

}
