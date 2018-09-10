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

	@CapabilityInject(EnergyNetworkList.class)
	public static Capability<EnergyNetworkList> NETWORK_LIST = null;

	public static void register() {

		CapabilityManager.INSTANCE.register(EnergyNetworkList.class, new IStorage<EnergyNetworkList>() {
			@Override
			public NBTTagList writeNBT(final Capability<EnergyNetworkList> capability, final EnergyNetworkList instance, final EnumFacing side) {
				final NBTTagList nbtTagList = new NBTTagList();

				for (final BlockPos pos : instance.getConnections()) {
					final NBTTagCompound compound = new NBTTagCompound();
					compound.setLong("pos", pos.toLong());
					nbtTagList.appendTag(compound);
				}

				return nbtTagList;
			}

			@Override
			public void readNBT(final Capability<EnergyNetworkList> capability, final EnergyNetworkList instance, final EnumFacing side, final NBTBase nbt) {

				final EnergyNetworkList energyNetworkList = instance;

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
