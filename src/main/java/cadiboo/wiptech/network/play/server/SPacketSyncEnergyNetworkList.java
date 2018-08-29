package cadiboo.wiptech.network.play.server;

import java.io.IOException;

import cadiboo.wiptech.capability.energy.network.CapabilityEnergyNetworkList;
import cadiboo.wiptech.capability.energy.network.IEnergyNetworkList;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSyncEnergyNetworkList implements IMessage, IMessageHandler<SPacketSyncEnergyNetworkList, IMessage> {

	private NBTTagCompound syncTag;

	public SPacketSyncEnergyNetworkList() {

	}

	public SPacketSyncEnergyNetworkList(NBTTagCompound syncTag) {
		this.syncTag = syncTag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		try {
			this.syncTag = packet.readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		packet.writeCompoundTag(syncTag);
	}

	@Override
	public IMessage onMessage(SPacketSyncEnergyNetworkList message, MessageContext ctx) {
		if (message.syncTag != null) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				if (Minecraft.getMinecraft() == null)
					return;
				if (Minecraft.getMinecraft().world == null)
					return;

				IEnergyNetworkList list = Minecraft.getMinecraft().world.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null);
				if (list == null)
					return;

				CapabilityEnergyNetworkList.NETWORK_LIST.getStorage().readNBT(CapabilityEnergyNetworkList.NETWORK_LIST, list, null, message.syncTag);
			});
		}
		return null;
	}

}
