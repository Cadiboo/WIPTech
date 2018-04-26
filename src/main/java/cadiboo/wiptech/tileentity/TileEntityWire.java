package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.util.Utils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityWire extends TileEntityBase implements ITickable {

	@Override
	public void update() {
		super.updateBase();
	}

	public boolean isConnectedTo(EnumFacing face) {
		return Utils.getBlockFromPos(world, pos.offset(face)) instanceof BlockWire;
	}

}
