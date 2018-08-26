package cadiboo.wiptech.network.play.client;

import java.io.IOException;

import cadiboo.wiptech.entity.IEntitySyncable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketSyncEntity implements IMessage, IMessageHandler<CPacketSyncEntity, IMessage> {

	private int entityDimension;
	private int entityId;
	private NBTTagCompound syncTag;

	public CPacketSyncEntity() {

	}

	public CPacketSyncEntity(int entityDimension, int entityId, NBTTagCompound syncTag) {
		this.entityDimension = entityDimension;
		this.entityId = entityId;
		this.syncTag = syncTag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		try {
			this.entityDimension = packet.readInt();
			this.entityId = packet.readInt();
			this.syncTag = packet.readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		packet.writeInt(entityDimension);
		packet.writeInt(entityId);
		packet.writeCompoundTag(this.syncTag);
	}

	/**
	 * Handle packet on server recieved from client
	 */
	@Override
	@SideOnly(Side.SERVER)
	public IMessage onMessage(CPacketSyncEntity message, MessageContext ctx) {
		World entityWorld = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.entityDimension);

		if (message.syncTag != null) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
				if (entityWorld == null)
					return;

				Entity entity = entityWorld.getEntityByID(message.entityId);
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
