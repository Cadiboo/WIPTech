package cadiboo.wiptech.capability;

import javax.annotation.Nullable;

import cadiboo.wiptech.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityModularProvidor implements ICapabilitySerializable<NBTTagCompound> {
	/**
	 * Unique key to identify the attached provider from others
	 */
	public static final ResourceLocation KEY = new ResourceLocation(Reference.ID, "modular_data");

	/**
	 * The instance that we are providing
	 */
	final ModularData slots = new ModularData();

	/**gets called before world is initiated. player.worldObj will return null here !*/
	public CapabilityModularProvidor(EntityLivingBase entity){
		slots.setPlayer(entity);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability == ModularDataCapability.CAPABILITY)
			return true;
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
		if (capability == ModularDataCapability.CAPABILITY)
			return (T)slots;
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT(){
		return (NBTTagCompound) ModularDataCapability.CAPABILITY.writeNBT(slots, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		ModularDataCapability.CAPABILITY.readNBT(slots, null, nbt);
	}
}
