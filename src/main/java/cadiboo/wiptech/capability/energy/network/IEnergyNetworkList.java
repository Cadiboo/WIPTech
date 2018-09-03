package cadiboo.wiptech.capability.energy.network;

import java.util.HashSet;

import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEnergyNetworkList extends ITickable {

	World getWorld();

	HashSet<BlockPos> getConnections();

	void addConnection(BlockPos pos);

	void removeConnection(BlockPos pos);

	void onChange(BlockPos pos);

}
