package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.block.BlockPeripheralBlock;
import cadiboo.wiptech.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityPeripheral extends TileEntityBase {

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		TileEntity tile = getTile();
		if (tile != null)
			return tile.getCapability(capability, facing);
		return super.getCapability(capability, facing);
	}

	private TileEntity getTile() {
		Block block = Utils.getBlockFromPos(getWorld(), getPos());
		if (block instanceof BlockPeripheralBlock)
			return getWorld().getTileEntity(((BlockPeripheralBlock) block).getTileEntityPos(getWorld(), getPos()));
		return null;
	}

	@Override
	public void writeNBT(NBTTagCompound nbt, NBTType type) {
		return;
	}

	@Override
	public void readNBT(NBTTagCompound nbt, NBTType type) {
		return;
	}

}
