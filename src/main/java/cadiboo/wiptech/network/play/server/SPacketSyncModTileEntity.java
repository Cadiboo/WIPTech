package cadiboo.wiptech.network.play.server;

import java.io.IOException;

import cadiboo.wiptech.tileentity.ModTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketSyncModTileEntity implements IMessage, IMessageHandler<SPacketSyncModTileEntity, IMessage> {

	private NBTTagCompound	syncTag;
	private BlockPos		pos;

	public SPacketSyncModTileEntity() {
	}

	public SPacketSyncModTileEntity(NBTTagCompound syncTag, BlockPos pos) {
		this.syncTag = syncTag;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.syncTag = new PacketBuffer(buf).readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeCompoundTag(this.syncTag);
		buf.writeLong(this.pos.toLong());
	}

	/**
	 * Handle packet on client recieved from server
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(SPacketSyncModTileEntity message, MessageContext ctx) {
		if (message.syncTag != null) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				if (Minecraft.getMinecraft() == null)
					return;
				if (Minecraft.getMinecraft().world == null)
					return;
				if (message.pos == null)
					return;

				TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(message.pos);
				if (tile != null && tile instanceof ModTileEntity) {
					((ModTileEntity) tile).readNBT(message.syncTag);
//					((ModTileEntity) tile).onSyncPacket();
				}
			});
		}
		return null;
	}

}