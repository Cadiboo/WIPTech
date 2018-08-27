package cadiboo.wiptech.capability.energy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import cadiboo.wiptech.WIPTech;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyNetwork {

	private ModEnergyStorage energy;
	private World world;

	private HashMap<BlockPos, ModEnergyStorage> connections;

	public EnergyNetwork(World world) {
		this.world = world;
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
		this.connections = new HashMap<BlockPos, ModEnergyStorage>(0);
	}

	public ModEnergyStorage getEnergy() {
		return energy;
	}

	public HashMap<BlockPos, ModEnergyStorage> getConnections() {
		return connections;
	}

	public Set<BlockPos> getPositions() {
		return connections.keySet();
	}

	public Collection<ModEnergyStorage> getEnergyStorages() {
		return connections.values();
	}

	public ModEnergyStorage getEnergyStorage(BlockPos connectionPosition) {
		return connections.get(connectionPosition);
	}

	public EnergyNetwork add(BlockPos connectionPosition, ModEnergyStorage connectionEnergy) {
		getConnections().put(connectionPosition, connectionEnergy);
		return this;
	}

	public EnergyNetwork remove(BlockPos connectionPosition) {
		getConnections().remove(connectionPosition);
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

}
