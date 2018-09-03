package cadiboo.wiptech.capability.energy.network;

import java.util.HashSet;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityEnergyNetworkList {

	@CapabilityInject(IEnergyNetworkList.class)
	public static Capability<IEnergyNetworkList> NETWORK_LIST = null;

	public static void register() {

		CapabilityManager.INSTANCE.register(IEnergyNetworkList.class, new IStorage<IEnergyNetworkList>() {
			@Override
			public NBTTagList writeNBT(final Capability<IEnergyNetworkList> capability, final IEnergyNetworkList instance, final EnumFacing side) {
				final NBTTagList nbtTagList = new NBTTagList();

				for (final BlockPos pos : instance.getConnections()) {
					final NBTTagCompound compound = new NBTTagCompound();
					compound.setLong("pos", pos.toLong());
					nbtTagList.appendTag(compound);
				}

				return nbtTagList;
			}

			@Override
			public void readNBT(final Capability<IEnergyNetworkList> capability, final IEnergyNetworkList instance, final EnumFacing side, final NBTBase nbt) {
				if (!(instance instanceof EnergyNetworkList)) {
					throw new IllegalArgumentException("Can't deserialize to an instance that isn't the default implementation");
				}

				final EnergyNetworkList energyNetworkList = (EnergyNetworkList) instance;

				final NBTTagList tagList = (NBTTagList) nbt;

				final HashSet<BlockPos> connections = new HashSet<>();

				for (int i = 0; i < tagList.tagCount(); i++) {
					final NBTTagCompound compound = tagList.getCompoundTagAt(i);

					final long posLong = compound.getLong("pos");
					final BlockPos pos = BlockPos.fromLong(posLong);

					connections.add(pos);

				}

				energyNetworkList.getConnections().clear();
				energyNetworkList.getConnections().addAll(connections);

				energyNetworkList.refreshNetworks();

			}
		}, () -> new EnergyNetworkList(null));
	}

}
