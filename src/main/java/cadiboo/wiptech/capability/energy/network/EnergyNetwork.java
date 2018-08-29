package cadiboo.wiptech.capability.energy.network;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.tileentity.TileEntityWire;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EnergyNetwork {

	private ModEnergyStorage energy;

	private HashSet<TileEntityWire> connections;

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
		this.connections = new HashSet<TileEntityWire>(0);
	}

	public ModEnergyStorage getEnergy() {
		return energy;
	}

	public HashSet<TileEntityWire> getConnections() {
		return connections;
	}

	public Set<BlockPos> getPositions() {
		HashSet<BlockPos> positions = new HashSet<>();
		for (TileEntityWire connection : connections) {
			positions.add(connection.getPosition());
		}
		return positions;
	}

	public Collection<ModEnergyStorage> getEnergyStorages() {
		HashSet<ModEnergyStorage> storages = new HashSet<>();
		for (TileEntityWire connection : connections) {
			storages.add(connection.getEnergy());
		}
		return storages;
	}

	public EnergyNetwork add(TileEntityWire connection) {
		getConnections().add(connection);
		return this;
	}

	public EnergyNetwork remove(TileEntityWire connection) {
		getConnections().remove(connection);
		return this;
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
		for (BlockPos myConnectionPosition : this.getPositions()) {
			for (BlockPos otherConnectionPosition : other.getPositions()) {
				for (EnumFacing facing : EnumFacing.VALUES) {
					BlockPos pos = myConnectionPosition.offset(facing);
					if (otherConnectionPosition.equals(pos)) {
						EnergyNetwork newNetwork = new EnergyNetwork();
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

	@Override
	public int hashCode() {
		return connections.hashCode();
	}

}
