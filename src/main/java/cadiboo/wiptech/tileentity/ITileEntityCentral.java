package cadiboo.wiptech.tileentity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITileEntityCentral extends IModTileEntity {

	BlockPos getPosition();

	World getWorld();

}
