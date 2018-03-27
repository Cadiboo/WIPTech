package cadiboo.wiptech.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.handler.network.PacketHandler;
import cadiboo.wiptech.handler.network.PacketSyncTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileEntityBase extends TileEntity {

	@Override
	public final void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.readNBT(compound, NBTType.SAVE);
	}

	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		this.writeNBT(compound, NBTType.SAVE);
		return compound;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			IItemHandler inventory = getInventory(facing);
			if(inventory != null){
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
			}
		}
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			IFluidHandler tank = getTank(facing);
			if(tank != null){
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
			}
		}
		if(capability == CapabilityEnergy.ENERGY){
			IEnergyStorage energy = getEnergy(facing);
			if(energy != null){
				return CapabilityEnergy.ENERGY.cast(energy);
			}
		}
		return null;
	}

	@Nullable
	public IItemHandler getInventory(@Nullable EnumFacing side){
		return null;
	}

	@Nullable
	public IEnergyStorage getEnergy(@Nullable EnumFacing side){
		return null;
	}

	@Nullable
	public IFluidHandler getTank(@Nullable EnumFacing side){
		return null;
	}

	public void writeNBT(NBTTagCompound nbt, NBTType type){
		if(type != NBTType.DROP){
			NBTTagCompound caps = new NBTTagCompound();
			for(EnumFacing side : EnumFacing.values()){
				NBTTagCompound capsSided = new NBTTagCompound();
				this.writeCapabilities(capsSided, side);
				if(!capsSided.hasNoTags()){
					caps.setTag(side.toString().toLowerCase(Locale.ROOT), capsSided);
				}
			}
			NBTTagCompound capsSided = new NBTTagCompound();
			this.writeCapabilities(capsSided, null);
			caps.setTag("default", capsSided);
			nbt.setTag("TileBaseCapabilities", caps);
		} else if(this.getEnergy(null) != null){
			nbt.setInteger("Energy", this.getEnergy(null).getEnergyStored());
		}
	}

	public void readNBT(NBTTagCompound nbt, NBTType type) {
		if (type != NBTType.DROP) {
			NBTTagCompound caps = nbt.getCompoundTag("TileBaseCapabilities");
			for (EnumFacing side : EnumFacing.values()) {
				String name = side.toString().toLowerCase(Locale.ROOT);
				if (caps.hasKey(name, Constants.NBT.TAG_COMPOUND)) {
					this.readCapabilities(caps.getCompoundTag(name), side);
				}
			}
			if (caps.hasKey("default", Constants.NBT.TAG_COMPOUND)) {
				this.readCapabilities(caps.getCompoundTag("default"), null);
			}
		} else if (this.getEnergy(null) != null) {
			this.getEnergy(null).receiveEnergy(nbt.getInteger("Energy"), false);
		}
	}

	protected void readCapabilities(NBTTagCompound nbt, @Nullable EnumFacing side){
		IItemHandler inventory = this.getInventory(side);
		if(inventory != null && inventory instanceof IItemHandlerModifiable && nbt.hasKey("Inventory")){
			for(int i = 0; i < inventory.getSlots(); i++){ // clear the inventory, otherwise empty stacks doesn't get overriden while syncing. Forge Bug?
				((IItemHandlerModifiable) inventory).setStackInSlot(i, ItemStack.EMPTY);
			}
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, side, nbt.getTag("Inventory"));
		}
		IFluidHandler tank = getTank(side);
		if(tank != null && tank instanceof IFluidTank && nbt.hasKey("FluidTank")){
			CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tank, side, nbt.getCompoundTag("FluidTank"));
		}
		IEnergyStorage energy = getEnergy(side);
		if(energy != null && energy instanceof EnergyStorage && nbt.hasKey("Energy")){
			CapabilityEnergy.ENERGY.readNBT(energy, side, nbt.getTag("Energy"));
		}
	}

	protected void writeCapabilities(NBTTagCompound nbt, @Nullable EnumFacing side){
		IItemHandler inventory = this.getInventory(side);
		if(inventory != null && inventory instanceof IItemHandlerModifiable){
			nbt.setTag("Inventory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, side));
		}
		IFluidHandler tank = getTank(side);
		if(tank != null && tank instanceof IFluidTank){
			nbt.setTag("FluidTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tank, side));
		}
		IEnergyStorage energy = getEnergy(side);
		if(energy != null && energy instanceof EnergyStorage){
			nbt.setTag("Energy", CapabilityEnergy.ENERGY.writeNBT(energy, side));
		}
	}

	private boolean isSyncDirty = false;
	public void syncToClients(){
		if(this.world != null && !this.world.isRemote){
			if(world.getTotalWorldTime() % 10 == 0){
				NBTTagCompound syncTag = new NBTTagCompound();
				this.writeNBT(syncTag, NBTType.SYNC);
				for(EntityPlayer player : this.world.playerEntities){
					if(player instanceof EntityPlayerMP && player.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 64){
						PacketHandler.NETWORK.sendTo(new PacketSyncTileEntity(syncTag, this.pos), (EntityPlayerMP) player);
					}
				}
				this.isSyncDirty = false;
			} else {
				this.isSyncDirty = true;
			}
		}
	}

	private boolean isRenderDirty = false;
	@SideOnly(Side.CLIENT)
	public void markForRenderUpdate(){
		if(this.world != null && this.world.isRemote){
			if(this.isRenderDirty && this.world.getTotalWorldTime() % 10 == 0){
				this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
				this.isRenderDirty = true;
			} else {
				this.isRenderDirty = true;
			}
		}
	}

	protected void updateBase(){
		if(!this.world.isRemote && this.isSyncDirty){
			this.syncToClients();
		}
		if(this.world.isRemote && this.isRenderDirty){
			this.markForRenderUpdate();
		}
	}

	@SideOnly(Side.CLIENT)
	public void onSyncPacket(){}

	public void breakBlock(){
		if(!this.world.isRemote){
			List<IItemHandler> cached = new ArrayList<>();
			for(EnumFacing side : EnumFacing.values()){
				IItemHandler inv = this.getInventory(side);
				if(inv != null && !cached.contains(inv)){
					for(int i = 0; i < inv.getSlots(); i++){
						InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), inv.getStackInSlot(i));
					}
					cached.add(inv);
				}
			}
			cached.clear();
		}
	}

	public boolean isWorking(){
		return false;
	}

	public boolean canBeUsedBy(EntityPlayer player) {
		return player.getDistanceSq(this.getPos().getX()+0.5D, this.pos.getY()+0.5D, this.pos.getZ()+0.5D) <= 64 && !this.isInvalid() && this.world.getTileEntity(this.pos) == this;
	}

	public int getCurrentEnergyUsage(){
		return -1;
	}

	public enum NBTType{
		SAVE,
		DROP,
		SYNC
	}

}
