package cadiboo.wiptech.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author canitzp
 */
public class ItemEnergyStorage extends CustomEnergyStorage {

	private ItemStack stack;

	public ItemEnergyStorage(ItemStack stack, int capacity) {
		super(capacity);
		this.stack = stack;
		this.fixItemStack();
	}

	public ItemEnergyStorage(ItemStack stack, int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
		this.stack = stack;
		this.fixItemStack();
	}

	public ItemEnergyStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
		this.stack = stack;
		this.fixItemStack();
	}

	public ItemEnergyStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
		this.stack = stack;
		this.fixItemStack();
	}

	@Override
	public int getEnergyStored() {
		return this.stack.getTagCompound().getInteger("Energy");
	}

	@Override
	public void setEnergy(int energy) {
		this.stack.getTagCompound().setInteger("Energy", energy);
	}

	private void fixItemStack() {
		if (!this.stack.hasTagCompound()) {
			this.stack.setTagCompound(new NBTTagCompound());
		}
		// if (!this.stack.getTagCompound().hasKey("TileData",
		// Constants.NBT.TAG_COMPOUND)) {
		// this.stack.getTagCompound().setTag("TileData", new NBTTagCompound());
		// }
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!canReceive())
			return 0;

		int energyReceived = Math.min(capacity - this.getEnergyStored(), Math.min(this.maxReceive, maxReceive));
		if (!simulate)
			setEnergy(this.getEnergyStored() + energyReceived);
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (!canExtract())
			return 0;

		int energyExtracted = Math.min(this.getEnergyStored(), Math.min(this.maxExtract, maxExtract));
		if (!simulate)
			this.setEnergy(this.getEnergyStored() - energyExtracted);
		return energyExtracted;
	}

}