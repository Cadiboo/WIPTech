package cadiboo.wiptech.capability.energy.network;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.World;

public interface IEnergyNetworkList<T> {

	public World getWorld();

	public ArrayList<EnergyNetwork> getNetworks();

	@Nullable
	public EnergyNetwork getNetworkFor(T potentialConnection);

	@Nonnull
	public EnergyNetwork getCreateOrMergeNetworkFor(T potentialConnection);

	public void splitNetworks(T potentialConnection);

	public void addConnection(T potentialConnection);

	public void removeConnection(T potentialConnection);

	public void setNetworks(ArrayList<EnergyNetwork> networks);

}
