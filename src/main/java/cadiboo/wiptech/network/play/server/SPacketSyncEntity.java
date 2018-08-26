package cadiboo.wiptech.network.play.server;

import java.io.IOException;

import cadiboo.wiptech.entity.IEntitySyncable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSyncEntity implements IMessage, IMessageHandler<SPacketSyncEntity, IMessage> {

	private int entityId;
	private NBTTagCompound syncTag;

	public SPacketSyncEntity() {

	}

	public SPacketSyncEntity(int entityId, NBTTagCompound syncTag) {
		this.entityId = entityId;
		this.syncTag = syncTag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		try {
			this.entityId = packet.readInt();
			this.syncTag = packet.readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		packet.writeInt(entityId);
		packet.writeCompoundTag(this.syncTag);
	}

	@Override
	public IMessage onMessage(SPacketSyncEntity message, MessageContext ctx) {
		if (message.syncTag != null) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				if (Minecraft.getMinecraft() == null)
					return;
				if (Minecraft.getMinecraft().world == null)
					return;

				Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
				if (entity == null)
					return;
				if (!(entity instanceof IEntitySyncable))
					return;

				((IEntitySyncable) entity).readSyncTag(message.syncTag);
			});
		}
		return null;
	}

}
