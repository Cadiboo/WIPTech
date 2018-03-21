package cadiboo.wiptech.provider;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.ModularData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TestLauncherProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

	//IItemHandler inventory = (IItemHandler)TestLauncherProvider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
	
	private final ItemStackHandler inventory;
	private final ModularData modules;

	public TestLauncherProvider() {
		inventory = new ItemStackHandler( 54 );
		modules = new ModularData();
	}

	@Override
	public boolean hasCapability( Capability<?> capability, EnumFacing facing ) {
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability( Capability<T> capability, EnumFacing facing ) {
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) {
			return (T) inventory; 
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return this.inventory.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		WIPTech.logger.info(nbt);
		//this.modules.circuit = nbt.getCompoundTag("circuit");
		this.inventory.deserializeNBT(nbt);
	}
	
}
