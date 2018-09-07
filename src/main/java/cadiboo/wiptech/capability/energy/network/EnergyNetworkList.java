package cadiboo.wiptech.capability.energy.network;

import java.util.HashSet;

import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.network.play.server.SPacketSyncEnergyNetworkList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyNetworkList implements ITickable {

	private final HashSet<BlockPos>			connections;
	private final HashSet<EnergyNetwork>	networks;
	private final World						world;

	public EnergyNetworkList(final World world) {
		this.connections = new HashSet<>(0);
		this.networks = new HashSet<>(0);
		this.world = world;
	}

	public HashSet<BlockPos> getConnections() {
		return this.connections;
	}

	public World getWorld() {
		return this.world;
	}

	public void addConnection(final BlockPos pos) {
		this.getConnections().add(pos);
		this.onChange(pos);
	}

	public void removeConnection(final BlockPos pos) {
		this.getConnections().remove(pos);
		this.onChange(pos);
	}

	public HashSet<EnergyNetwork> getNetworks() {
		return this.networks;
	}

	@Override
	public void update() {
		if (this.world.isRemote) {
			return;
		}

		if (this.shouldRefreshConnections()) {
			this.refreshConnections();
		}
		if (this.shouldRefreshNetworks()) {
			this.refreshNetworks();
		}
		if (this.shouldDistributeEnergy()) {
			for (final EnergyNetwork network : this.networks) {
				network.distributeEnergy();
			}
		}
	}

	private boolean shouldRefreshConnections() {
		final int check = (int) (this.getConnections().size() / 1000f);
		if (check == 0) {
			return true;
		}
		return (this.world.getTotalWorldTime() % check) == 0;
	}

	private boolean shouldRefreshNetworks() {
		final int check = (int) (this.getConnections().size() / 100f);
		if (check == 0) {
			return true;
		}
		return (this.world.getTotalWorldTime() % check) == 0;
	}

	private boolean shouldDistributeEnergy() {
		final int check = (int) (this.getConnections().size() / 10f);
		if (check == 0) {
			return true;
		}
		return (this.world.getTotalWorldTime() % check) == 0;
	}

	public void refreshNetworks() {
		this.networks.clear();
		final HashSet<BlockPos> done = new HashSet<>(0);

		for (final BlockPos pos : this.getConnections()) {
			if (!done.contains(pos)) {
				done.add(pos);

				final EnergyNetwork network = new EnergyNetwork(this.world);
				this.generateNetwork(network, pos);
				done.addAll(network.getConnections());
				this.networks.add(network);

			}
		}
	}

	private void generateNetwork(final EnergyNetwork network, final BlockPos pos) {

		if (network.getConnections().size() > 250) {
			// WIPTech.error("Energy network reached max size! " + pos.toString() + " and any more connections cannot be added to the network!");
			return;
		}

		final TileEntity tile = this.world.getTileEntity(pos);
		if (tile == null) {
			return;
		}
		if (!(tile instanceof TileEntity)) {
			return;
		}

		network.add(pos);

		for (final EnumFacing facing : EnumFacing.VALUES) {
			final BlockPos offset = pos.offset(facing);
			if (!network.getConnections().contains(offset)) {
				this.generateNetwork(network, offset);
			}
		}
	}

	public void refreshConnections() {
		for (final BlockPos pos : this.getConnections()) {
			final TileEntity tile = this.world.getTileEntity(pos);
			if (tile == null) {
				this.removeConnection(pos);
				continue;
			}
			if (!(tile instanceof TileEntity)) {
				this.removeConnection(pos);
				continue;
			}
		}
	}

	public void onChange(final BlockPos pos) {
		if (!this.world.isRemote) {
			final NBTTagList syncTag = (NBTTagList) CapabilityEnergyNetworkList.NETWORK_LIST.writeNBT(this, null);
			for (final EntityPlayer player : this.world.playerEntities) {
				if (player instanceof EntityPlayerMP) {
					ModNetworkManager.NETWORK.sendTo(new SPacketSyncEnergyNetworkList(syncTag), (EntityPlayerMP) player);
				}
			}
		} else {

		}
	}

}
