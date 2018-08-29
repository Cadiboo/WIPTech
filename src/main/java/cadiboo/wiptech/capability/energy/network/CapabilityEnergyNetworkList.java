package cadiboo.wiptech.capability.energy.network;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
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
			public NBTTagCompound writeNBT(Capability<IEnergyNetworkList> capability, IEnergyNetworkList instance, EnumFacing side) {
				NBTTagCompound compound = new NBTTagCompound();
//				compound.setInteger("networksSize", instance.getNetworks().size());
//
//				ArrayList<EnergyNetwork> networks = instance.getNetworks();
//
//				for (int i = 0; i < networks.size(); i++) {
//					NBTTagCompound networkCompound = new NBTTagCompound();
//					Set<BlockPos> positions = networks.get(i).getPositions();
//					networkCompound.setInteger("positionsSize", positions.size());
//					Iterator<BlockPos> it = positions.iterator();
//
//					int j = 0;
//					while (it.hasNext()) {
//						j++;
//						networkCompound.setString("" + j, "" + it.next().toLong());
//					}
//
//					compound.setTag("" + i, networkCompound);
//				}

				return compound;
			}

			@Override
			public void readNBT(Capability<IEnergyNetworkList> capability, IEnergyNetworkList instance, EnumFacing side, NBTBase nbt) {
				if (!(instance instanceof EnergyNetworkList))
					throw new IllegalArgumentException("Can't deserialize to an instance that isn't the default implementation");

//				if (!(nbt instanceof NBTTagCompound))
//					throw new IllegalArgumentException("Can't deserialize from a NBT type that isn't a NBTTagCompound");
//
//				NBTTagCompound compound = (NBTTagCompound) nbt;
//
//				int networkSize = compound.getInteger("networksSize");
//
//				ArrayList<EnergyNetwork> networks = new ArrayList<>();
//
//				for (int i = 0; i < networkSize; i++) {
//					NBTTagCompound networkCompound = compound.getCompoundTag("" + i);
//					int positionsSize = networkCompound.getInteger("positionsSize");
//
//					EnergyNetwork network = new EnergyNetwork();
//
//					for (int j = 0; j < positionsSize; j++) {
//						network.add((TileEntityWire) instance.getWorld().getTileEntity(BlockPos.fromLong(Long.parseLong(networkCompound.getString("" + j)))));
//					}
//
//				}
//
//				instance.setNetworks(networks);
			}
		}, () -> new EnergyNetworkList(null));
	}

}
