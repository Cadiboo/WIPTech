package cadiboo.wiptech.tileentity;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCapacitorBank extends TileEntity implements ITickable {

	public static int ENERGY_CAPACITY = 5000;
	private ItemStackHandler handler;
	private EnergyStorage storage;
	//private CustomEnergyStorage storage;
	
	public TileEntityCapacitorBank()
	{
		this.handler = new ItemStackHandler(2);
		this.storage = new EnergyStorage(ENERGY_CAPACITY, 0);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) this.handler;
		if(capability == CapabilityEnergy.ENERGY)
			return (T) this.storage;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		if(capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Inventory", this.handler.serializeNBT());
		//this.storage.w
		return super.writeToNBT(nbt);
	}

	@Override
	public void update() {
		if (this.world != null) {
			if (!this.world.isRemote) {
				this.storage.extractEnergy(100, false);
				/*int before = this.storage.getEnergyStored();
				int receive = this.storage.getMaxEnergyStored() - this.storage.getEnergyStored() < this.transfer
						? this.storage.getMaxEnergyStored() - this.storage.getEnergyStored() : this.transfer;
				int extract = this.storage.getEnergyStored() > this.transfer ? this.transfer
						: this.storage.getEnergyStored();
				if (this.storage.getEnergyStored() < this.storage.getMaxEnergyStored()) {
					this.storage.receiveEnergyInternal((int) EnergyUtils.takeEnergyAllFaces(this.world, this.pos,
							receive, EnergyUnits.FORGE_ENERGY, false), false);
					this.storage.receiveEnergyInternal((int) EnergyUtils.takeEnergy(this.handler.getStackInSlot(1),
							receive, EnergyUnits.FORGE_ENERGY, false, null), false);
				}
				this.storage.extractEnergyInternal((int) EnergyUtils.giveEnergyAllFaces(this.world, this.pos, extract,
						EnergyUnits.FORGE_ENERGY, false), false);
				this.storage.extractEnergyInternal((int) EnergyUtils.giveEnergy(this.handler.getStackInSlot(0), extract,
						EnergyUnits.FORGE_ENERGY, false, null), false);
				this.energyDifference = this.storage.getEnergyStored() - before;
				*/
			}
		}
	}
	
}
