package cadiboo.wiptech.capability.energy.network;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
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
			public NBTBase writeNBT(Capability<IEnergyNetworkList> capability, IEnergyNetworkList instance, EnumFacing side) {
				return new NBTTagList();
			}

			@Override
			public void readNBT(Capability<IEnergyNetworkList> capability, IEnergyNetworkList instance, EnumFacing side, NBTBase nbt) {
				if (!(instance instanceof EnergyNetworkList))
					throw new IllegalArgumentException("Can't deserialize to an instance that isn't the default implementation");
//					((EnergyNetworkList) instance).energy = ((NBTTagInt) nbt).getInt();
			}
		}, () -> new EnergyNetworkList());
	}

}
