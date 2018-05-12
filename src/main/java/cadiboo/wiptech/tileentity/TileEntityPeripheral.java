package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.block.BlockPeripheralBlock;
import cadiboo.wiptech.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityPeripheral extends TileEntityBase implements ITickable {

	@Override
	public void update() {

		TileEntity tile = getTile();
		if (tile == null)
			return;
		if (tile instanceof ITickable)
			return;
		((ITickable) tile).update();
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		TileEntity tile = getTile();
		if (tile != null)
			return tile.getCapability(capability, facing);
		return super.getCapability(capability, facing);
	}

	private TileEntity getTile() {
		Block block = Utils.getBlockFromPos(getWorld(), getPos());
		if (block instanceof BlockPeripheralBlock) {
			TileEntity tile = getWorld().getTileEntity(((BlockPeripheralBlock) block).getTileEntityPos(getWorld(), getPos()));
			if (tile != null)
				return tile;
		}

		return null;
	}

}
