package cadiboo.wiptech.capability.energy.network;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IEnergyNetworkList {

	public ArrayList<EnergyNetwork> getNetworks();

	@Nullable
	public EnergyNetwork getNetworkFor(IEnergyNetworkConnection potentialConnection);

	@Nonnull
	public EnergyNetwork getCreateOrMergeNetworkFor(IEnergyNetworkConnection potentialConnection);

	public void splitNetworks(IEnergyNetworkConnection potentialConnection);

	public void addConnection(IEnergyNetworkConnection potentialConnection);

	public void removeConnection(IEnergyNetworkConnection potentialConnection);

}
