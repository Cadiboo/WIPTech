package cadiboo.wiptech.tileentity;

import cadiboo.wiptech.capability.inventory.IInventoryUser;
import cadiboo.wiptech.capability.inventory.ModItemStackHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityModFurnace extends TileEntity implements ITickable, ITileEntitySyncable, IInventoryUser {

	private final ModItemStackHandler inventory;

	public TileEntityModFurnace() {
		this.inventory = new ModItemStackHandler(3);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public ModItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public BlockPos getPosition() {
		return this.pos;
	}

	@Override
	public void readNBT(final NBTTagCompound syncTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeNBT(final NBTTagCompound syncTag) {
		// TODO Auto-generated method stub

	}

}
