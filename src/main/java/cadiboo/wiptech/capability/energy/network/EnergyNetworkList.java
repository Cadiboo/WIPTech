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

	public EnergyNetworkList(final World world) {
		this.connections = new HashSet<>(0);
		this.world = world;
	}

	@Override
	public HashSet<BlockPos> getConnections() {
		return this.connections;
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public void addConnection(final BlockPos pos) {
		this.getConnections().add(pos);
	}

	@Override
	public void removeConnection(final BlockPos pos) {
		this.getConnections().remove(pos);
	}

	@Override
	public void update() {
		this.refreshConnections();
		this.distributeEnergy();
	}

	private void distributeEnergy(final BlockPos... dontDistribute) {
		int networkEnergy = this.getNetworkEnergy();

		final HashSet<ModEnergyStorage> storages = new HashSet<>();
		this.refreshConnections();
		for (final BlockPos pos : this.connections) {
			try {
				storages.add(((TileEntityWire) this.world.getTileEntity(pos)).getEnergy());
			} catch (final Exception e) {
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
		this.refreshConnections();
		int networkEnergy = 0;
		for (final BlockPos pos : this.connections) {
			try {
				final TileEntity tile = this.world.getTileEntity(pos);
				final ModEnergyStorage energy = ((TileEntityWire) tile).getEnergy();
				networkEnergy += energy.getEnergyStored();
			} catch (final Exception e) {
				// TODO: handle exception
			}
		}

		return networkEnergy;
	}

	private void refreshConnections() {
		for (final BlockPos pos : this.connections) {
			final TileEntity tile = this.world.getTileEntity(pos);
			if (tile == null) {
				this.connections.remove(pos);
				continue;
			}
			if (!(tile instanceof TileEntity)) {
				this.connections.remove(pos);
				continue;
			}
		}
	}

	private static int[] splitIntoParts(final int whole, final int parts) {
		final int[] arr = new int[parts];
		int remain = whole;
		int partsLeft = parts;
		for (int i = 0; partsLeft > 0; i++) {
			final int size = ((remain + partsLeft) - 1) / partsLeft; // rounded up, aka ceiling
			arr[i] = size;
			remain -= size;
			partsLeft--;
		}
		return arr;
	}

}
