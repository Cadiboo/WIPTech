package cadiboo.wiptech.network.play.server;

import java.io.IOException;

import cadiboo.wiptech.capability.energy.network.CapabilityEnergyNetworkList;
import cadiboo.wiptech.capability.energy.network.IEnergyNetworkList;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSyncEnergyNetworkList implements IMessage, IMessageHandler<SPacketSyncEnergyNetworkList, IMessage> {

	private NBTTagCompound syncTag;

	public SPacketSyncEnergyNetworkList() {

	}

	public SPacketSyncEnergyNetworkList(final NBTBase syncTag) {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("syncTag", syncTag);
		this.syncTag = compound;
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		final PacketBuffer packet = new PacketBuffer(buf);
		try {
			this.syncTag = packet.readCompoundTag();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		final PacketBuffer packet = new PacketBuffer(buf);
		packet.writeCompoundTag(this.syncTag);
	}

	@Override
	public IMessage onMessage(final SPacketSyncEnergyNetworkList message, final MessageContext ctx) {
		if ((message.syncTag != null) && message.syncTag.hasKey("syncTag")) {
			// WIPTech.info(message.syncTag.getTag("syncTag"));
			Minecraft.getMinecraft().addScheduledTask(() -> {
				if (Minecraft.getMinecraft() == null) {
					return;
				}
				if (Minecraft.getMinecraft().world == null) {
					return;
				}

				final IEnergyNetworkList list = Minecraft.getMinecraft().world.getCapability(CapabilityEnergyNetworkList.NETWORK_LIST, null);
				if (list == null) {
					return;
				}

				CapabilityEnergyNetworkList.NETWORK_LIST.getStorage().readNBT(CapabilityEnergyNetworkList.NETWORK_LIST, list, null, message.syncTag.getTag("syncTag"));
			});
		}
		return null;
	}

}
