package cadiboo.wiptech.capability.energy.network;

import java.util.HashSet;

import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEnergyNetworkList extends ITickable {

	public World getWorld();

	public HashSet<BlockPos> getConnections();

//	@Nullable
//	public EnergyNetwork getNetworkFor(T potentialConnection);
//
//	@Nonnull
//	public EnergyNetwork getCreateOrMergeNetworkFor(T potentialConnection);
//
//	public void splitNetworks(T potentialConnection);

	public void addConnection(BlockPos pos);

	public void removeConnection(BlockPos pos);

//	public void setNetworks(ArrayList<EnergyNetwork> networks);

}
