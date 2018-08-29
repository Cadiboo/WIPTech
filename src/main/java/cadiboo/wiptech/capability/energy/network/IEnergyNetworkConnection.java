package cadiboo.wiptech.capability.energy.network;

import cadiboo.wiptech.capability.energy.IEnergyUser;
import net.minecraft.tileentity.TileEntity;

public interface IEnergyNetworkConnection<T extends TileEntity> extends IEnergyUser {

	public EnergyNetwork getNetwork();

}
