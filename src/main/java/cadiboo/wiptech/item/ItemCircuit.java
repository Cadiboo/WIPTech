package cadiboo.wiptech.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.capability.attachments.circuitdata.CapabilityCircuitData;
import cadiboo.wiptech.capability.attachments.circuitdata.CircuitData;
import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * @author Cadiboo
 */
public class ItemCircuit extends Item implements IItemAttachment, IModItem {

	private final CircuitTypes type;

	public ItemCircuit(final String name, final CircuitTypes type) {
		ModUtil.setRegistryNames(this, type.getNameLowercase() + "_" + name);
		this.type = type;
	}

	public CircuitTypes getType() {
		return this.type;
	}

	@Override
	public AttachmentPoints getAttachmentPoint() {
		return AttachmentPoints.CIRCUIT;
	}

	@Override
	public final ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
		return new ICapabilitySerializable<NBTTagCompound>() {

			final ItemStack		itemStack		= stack;
			final CircuitData	data			= new CircuitData(ItemCircuit.this.getType());
			final String		BURST_SHOTS_TAG	= "burstShots";

			@Override
			public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
				return this.getCapability(capability, facing) != null;
			}

			@Nullable
			@Override
			public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
				if (capability == CapabilityCircuitData.CIRCUIT_DATA) {
					return (T) this.data;
				}
				return null;
			}

			@Override
			public NBTTagCompound serializeNBT() {
				final NBTTagCompound compound = new NBTTagCompound();
				compound.setTag(this.BURST_SHOTS_TAG, this.data.serializeNBT());
				return compound;
			}

			@Override
			public void deserializeNBT(final NBTTagCompound compound) {
				if (compound.hasKey(this.BURST_SHOTS_TAG)) {
					this.data.deserializeNBT(compound.getCompoundTag(this.BURST_SHOTS_TAG));
				}

			}
		};
	}

}
