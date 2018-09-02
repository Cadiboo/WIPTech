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
				for (final ModEnergyStorage energy : EnergyNetwork.this.getEnergyStorages()) {
					capacity += energy.getCapacity();
				}
				return capacity;
			}

			@Override
			public int getEnergyStored() {
				int energyStored = 0;
				for (final ModEnergyStorage energy : EnergyNetwork.this.getEnergyStorages()) {
					energyStored += energy.getEnergyStored();
				}
				return energyStored;
			}

			@Override
			public int getMaxExtract() {
				int maxExtract = 0;
				for (final ModEnergyStorage energy : EnergyNetwork.this.getEnergyStorages()) {
					maxExtract += energy.getMaxExtract();
				}
				return maxExtract;
			}

			@Override
			public int getMaxReceive() {
				int maxReceive = 0;
				for (final ModEnergyStorage energy : EnergyNetwork.this.getEnergyStorages()) {
					maxReceive += energy.getMaxReceive();
				}
				return maxReceive;
			}
		};
		this.connections = new HashSet<>(0);
	}

	public ModEnergyStorage getEnergy() {
		return this.energy;
	}

	public HashSet<TileEntityWire> getConnections() {
		return this.connections;
	}

	public Set<BlockPos> getPositions() {
		final HashSet<BlockPos> positions = new HashSet<>();
		for (final TileEntityWire connection : this.connections) {
			positions.add(connection.getPosition());
		}
		return positions;
	}

	public Collection<ModEnergyStorage> getEnergyStorages() {
		final HashSet<ModEnergyStorage> storages = new HashSet<>();
		for (final TileEntityWire connection : this.connections) {
			storages.add(connection.getEnergy());
		}
		return storages;
	}

	public EnergyNetwork add(final TileEntityWire connection) {
		this.getConnections().add(connection);
		return this;
	}

	public EnergyNetwork remove(final TileEntityWire connection) {
		this.getConnections().remove(connection);
		return this;
	}

	public void update() {
		final int eachEnergy = (int) ((float) this.getEnergy().getEnergyStored() / (float) this.getConnections().size());
		int energyRemaining = this.getEnergy().getEnergyStored();

		int repetitions = 0;

		while (energyRemaining > 0) {
			repetitions++;
			if (repetitions > 3) {
				WIPTech.warn("repetitions went over 3 when trying to distribute energy inside the network!");
				new Exception().printStackTrace();
				break;
			}
			for (final ModEnergyStorage energy : this.getEnergyStorages()) {
				energyRemaining -= energy.extractEnergy(eachEnergy, false);
			}

		}

	}

	@Nullable
	public EnergyNetwork tryMerge(final EnergyNetwork other) {
		for (final BlockPos myConnectionPosition : this.getPositions()) {
			for (final BlockPos otherConnectionPosition : other.getPositions()) {
				for (final EnumFacing facing : EnumFacing.VALUES) {
					final BlockPos pos = myConnectionPosition.offset(facing);
					if (otherConnectionPosition.equals(pos)) {
						final EnergyNetwork newNetwork = new EnergyNetwork();
						newNetwork.getConnections().addAll(this.getConnections());
						newNetwork.getConnections().addAll(other.getConnections());
						return newNetwork;
					}
				}
			}
		}

		return null;
	}

	@Override
	public boolean equals(final Object obj) {
		return (obj instanceof EnergyNetwork) && ((EnergyNetwork) obj).getConnections().equals(this.getConnections());
	}

	@Override
	public int hashCode() {
		return this.connections.hashCode();
	}

}
