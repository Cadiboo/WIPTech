package cadiboo.wiptech.capability.energy.network;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.network.play.server.SPacketSyncEnergyNetworkList;
import cadiboo.wiptech.tileentity.TileEntityWire;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyNetworkList implements IEnergyNetworkList<TileEntityWire> {

	private volatile ArrayList<EnergyNetwork> networks;
	private final World world;

	public EnergyNetworkList(World world) {
		this.networks = new ArrayList<>(0);
		this.world = world;
	}

	@Override
	public ArrayList<EnergyNetwork> getNetworks() {
		return networks;
	}

	@Override
	@Nullable
	public EnergyNetwork getNetworkFor(TileEntityWire potentialConnection) {
		refresh();
//		reload();
		for (EnergyNetwork network : networks) {
			for (TileEntityWire connectionPosition : network.getConnections()) {
				if (connectionPosition == potentialConnection)
					return network;
			}
		}

		return null;
	}

	@Override
	@Nonnull
	public EnergyNetwork getCreateOrMergeNetworkFor(TileEntityWire potentialConnection) {
		EnergyNetwork foundNetwork = getNetworkFor(potentialConnection);
		if (foundNetwork == null) {

			for (EnergyNetwork network : networks) {
				for (TileEntityWire connection : network.getConnections()) {

					for (EnumFacing facing : EnumFacing.VALUES) {
						TileEntity tile = world.getTileEntity(potentialConnection.getPosition().offset(facing));
						if (tile == null)
							continue;

						if (!(tile instanceof TileEntityWire))
							continue;

						TileEntityWire networkConnection = (TileEntityWire) tile;

						EnergyNetwork networkConnectionNetwork = networkConnection.getNetwork();
						if (networkConnectionNetwork == null)
							continue;

						if (!networkConnectionNetwork.equals(network))
							continue;

						network.add(potentialConnection);
						ModNetworkManager.NETWORK.sendToAll(new SPacketSyncEnergyNetworkList((NBTTagCompound) CapabilityEnergyNetworkList.NETWORK_LIST.getStorage().writeNBT(
								CapabilityEnergyNetworkList.NETWORK_LIST, this, null)));
						return network;
					}
				}
			}

//			refresh();
			foundNetwork = new EnergyNetwork().add(potentialConnection);
			getNetworks().add(foundNetwork);

//			foundNetwork = createNetwork(potentialConnection);
		}
		return foundNetwork;
	}

//	@Nullable
//	public EnergyNetwork getNetworkFor(World world, BlockPos position) {
//		refresh();
//		for (EnergyNetwork network : networks) {
//			if (network.getWorld() != world)
//				continue;
//			for (BlockPos connectionPosition : network.getPositions()) {
//				if (connectionPosition.equals(position))
//					return network;
//			}
//		}
//
//		return null;
//	}

//	@Nonnull
//	public EnergyNetwork getOrCreateNetworkFor(World world, BlockPos position, ModEnergyStorage energy) {
//		refresh();
//		EnergyNetwork foundNetwork = getNetworkFor(world, position);
//		if (foundNetwork == null) {
//
//			for (EnergyNetwork network : networks) {
//				if (network.getWorld() != world)
//					continue;
//				for (BlockPos connectionPosition : network.getPositions()) {
//					for (EnumFacing facing : EnumFacing.VALUES) {
//						BlockPos pos = position.offset(facing);
//						if (connectionPosition.equals(pos))
//							return network;
//					}
//				}
//			}
//
//			foundNetwork = createNetwork(world, position, energy);
//		}
//		return foundNetwork;
//	}

	private EnergyNetwork createNetwork(TileEntityWire connection) {
		refresh();
		EnergyNetwork network = new EnergyNetwork().add(connection);
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
//				this.getOrCreateNetworkFor(network.getWorld(), position, network.getEnergyStorage(position));
			}
			network = null;
		}
	}

	@Override
	public void splitNetworks(TileEntityWire potentialConnection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addConnection(TileEntityWire potentialConnection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeConnection(TileEntityWire potentialConnection) {
		// TODO Auto-generated method stub

	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void setNetworks(ArrayList<EnergyNetwork> networksIn) {
		getNetworks().clear();
		getNetworks().addAll(networksIn);

	}

}
