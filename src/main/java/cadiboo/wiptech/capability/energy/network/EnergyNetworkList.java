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
		refreshConnections();
		distributeEnergy();
	}

	private void distributeEnergy(BlockPos... dontDistribute) {
		int networkEnergy = getNetworkEnergy();

		HashSet<ModEnergyStorage> storages = new HashSet<>();
		refreshConnections();
		for (BlockPos pos : connections) {
			try {
				storages.add(((TileEntityWire) world.getTileEntity(pos)).getEnergy());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		int repetitions = 0;
		while (networkEnergy > 0) {
			repetitions++;

			final int[] sets = splitIntoParts(networkEnergy, storages.size());

			if (repetitions >= 5) {
				WIPTech.info("repetitions went over 5. repetitions were", repetitions, networkEnergy, sets);
				break;
			}

			for (int i = 0; i < storages.size(); i++) {
				networkEnergy -= storages.toArray(new ModEnergyStorage[0])[i].setEnergyStored(sets[i], false);
			}

		}
	}

	private int getNetworkEnergy() {
		refreshConnections();
		int networkEnergy = 0;
		for (BlockPos pos : connections) {
			try {
				TileEntity tile = world.getTileEntity(pos);
				ModEnergyStorage energy = ((TileEntityWire) tile).getEnergy();
				networkEnergy += energy.getEnergyStored();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return networkEnergy;
	}

	private void refreshConnections() {
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
