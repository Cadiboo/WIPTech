package cadiboo.wiptech.capability.energy.network;

import java.util.HashSet;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.tileentity.TileEntityWire;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyNetworkList implements IEnergyNetworkList {

	private final HashSet<BlockPos> connections;
	private final World world;

	public EnergyNetworkList(World world) {
		this.connections = new HashSet<>(0);
		this.world = world;
	}

	@Override
	public HashSet<BlockPos> getConnections() {
		return connections;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void addConnection(BlockPos pos) {
		getConnections().add(pos);
	}

	@Override
	public void removeConnection(BlockPos pos) {
		getConnections().remove(pos);
	}

	@Override
	public void update() {
		int networkEnergy = 0;
		HashSet<ModEnergyStorage> storages = new HashSet<>();
		for (BlockPos pos : connections) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile == null) {
				connections.remove(pos);
				continue;
			}
			if (!(tile instanceof TileEntity)) {
				connections.remove(pos);
				continue;
			}

			ModEnergyStorage energy = ((TileEntityWire) tile).getEnergy();
			storages.add(energy);

			networkEnergy += energy.getEnergyStored();
		}

		int repetitions = 0;

		while (networkEnergy > 0) {

			repetitions++;

			final int[] sets = splitIntoParts(networkEnergy, storages.size());

			if (repetitions >= 5) {
				WIPTech.info("__", repetitions, networkEnergy, sets);
				break;
			}

			for (int i = 0; i < storages.size(); i++) {
				networkEnergy -= storages.toArray(new ModEnergyStorage[0])[i].setEnergyStored(sets[i], false);
			}

		}
	}

	private static int[] splitIntoParts(int whole, int parts) {
		int[] arr = new int[parts];
		int remain = whole;
		int partsLeft = parts;
		for (int i = 0; partsLeft > 0; i++) {
			int size = (remain + partsLeft - 1) / partsLeft; // rounded up, aka ceiling
			arr[i] = size;
			remain -= size;
			partsLeft--;
		}
		return arr;
	}

}
