package cadiboo.wiptech.capability.attachments;

import cadiboo.wiptech.util.ModEnums.AttachmentPoint;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author Cadiboo
 */
public class CapabilityAttachmentList {
	@CapabilityInject(AttachmentList.class)
	public static Capability<AttachmentList> ATTACHMENT_LIST = null;

	public static void register() {

		CapabilityManager.INSTANCE.register(AttachmentList.class, new IStorage<AttachmentList>() {
			@Override
			public NBTTagList writeNBT(final Capability<AttachmentList> capability, final AttachmentList instance, final EnumFacing side) {
				final NBTTagList nbtTagList = new NBTTagList();

				return nbtTagList;
			}

			@Override
			public void readNBT(final Capability<AttachmentList> capability, final AttachmentList instance, final EnumFacing side, final NBTBase nbt) {

			}
		}, () -> new AttachmentList(AttachmentPoint.values()));
	}
}
