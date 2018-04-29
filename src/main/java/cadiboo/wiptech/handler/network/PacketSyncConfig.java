package cadiboo.wiptech.handler.network;

import java.io.IOException;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.config.Configuration;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncConfig implements IMessage, IMessageHandler<PacketSyncConfig, IMessage> {

	private NBTTagCompound syncTag;

	public PacketSyncConfig() {
	}

	public PacketSyncConfig(NBTTagCompound syncTag) {
		this.syncTag = syncTag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.syncTag = new PacketBuffer(buf).readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeCompoundTag(this.syncTag);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IMessage onMessage(PacketSyncConfig message, MessageContext ctx) {
		WIPTech.info("message: " + message);
		if (message.syncTag != null) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				Configuration.energy.TurbineProduction = syncTag.getInteger("TurbineProduction");
				Configuration.energy.CrusherUsage = syncTag.getInteger("CrusherUsage");
				Configuration.energy.CoilerUsage = syncTag.getInteger("CoilerUsage");
				Configuration.energy.BaseWireStorage = syncTag.getInteger("BaseWireStorage");

				Configuration.projectile.HurtEnderman = syncTag.getBoolean("HurtEnderman");
				Configuration.projectile.HurtWither = syncTag.getBoolean("HurtWither");
			});
		}
		return null;
	}

}