package cadiboo.wiptech.capability.attachments;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IAttachmentUser extends ICapabilitySerializable<NBTTagCompound> {

	String ATTACHMENTS_TAG = "attachments";

	@Nonnull
	AttachmentList getAttachments();

	@Override
	default NBTTagCompound serializeNBT() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setTag(ATTACHMENTS_TAG, getAttachments().serializeNBT());
		return compound;
	}

	@Override
	default void deserializeNBT(final NBTTagCompound compound) {
		if (compound.hasKey(ATTACHMENTS_TAG)) {
			this.getAttachments().deserializeNBT(compound.getCompoundTag(ATTACHMENTS_TAG));
		}
	}

}
