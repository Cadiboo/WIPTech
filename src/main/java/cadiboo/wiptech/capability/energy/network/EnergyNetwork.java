package cadiboo.wiptech.capability.energy.network;

import java.util.Arrays;
import java.util.HashSet;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyNetwork {

	private final HashSet<BlockPos>	connections;
	private final World				world;

	public EnergyNetwork(final World world) {
		this.connections = new HashSet<>(0);
		this.world = world;
	}

	public HashSet<BlockPos> getConnections() {
		return this.connections;
	}

	public EnergyNetwork add(final BlockPos connection) {
		this.getConnections().add(connection);
		return this;
	}

	public EnergyNetwork remove(final BlockPos connection) {
		this.getConnections().remove(connection);
		return this;
	}

	@Override
	public boolean equals(final Object obj) {
		return (obj instanceof EnergyNetwork) && ((EnergyNetwork) obj).getConnections().equals(this.getConnections());
	}

	@Override
	public int hashCode() {
		return this.getConnections().hashCode();
	}

	void distributeEnergy(final BlockPos... dontDistribute) {
		int networkEnergy = this.getNetworkEnergy();

		final HashSet<BlockPos> hashedDontDistribute = new HashSet<>(Arrays.asList(dontDistribute));

		final HashSet<ModEnergyStorage> storages = new HashSet<>();

		for (final BlockPos pos : this.getConnections()) {
			try {
				if (!hashedDontDistribute.contains(pos)) {
					storages.add(((TileEntityWire) this.world.getTileEntity(pos)).getEnergy());
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		int repetitions = 0;
		while (networkEnergy > 0) {
			repetitions++;

			final int[] sets = ModUtil.splitIntoParts(networkEnergy, storages.size());

			if (repetitions >= 5) {
				WIPTech.warn("repetitions went over 5. repetitions were", repetitions, networkEnergy, sets);
				break;
			}

			for (int i = 0; i < storages.size(); i++) {
				networkEnergy -= storages.toArray(new ModEnergyStorage[0])[i].setEnergyStored(sets[i], false);
			}

		}
	}

	int getNetworkEnergy() {
		int networkEnergy = 0;
		for (final BlockPos pos : this.getConnections()) {
			try {
				final TileEntity tile = this.world.getTileEntity(pos);
				final ModEnergyStorage energy = ((TileEntityWire) tile).getEnergy();
				networkEnergy += energy.getEnergyStored();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return networkEnergy;
	}

}
