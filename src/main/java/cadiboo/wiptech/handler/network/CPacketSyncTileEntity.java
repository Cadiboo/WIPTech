package cadiboo.wiptech.handler.network;

import java.io.IOException;

import cadiboo.wiptech.tileentity.TileEntityBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketSyncTileEntity implements IMessage, IMessageHandler<CPacketSyncTileEntity, IMessage> {

	private NBTTagCompound	syncTag;
	private BlockPos		pos;
	private int				dimension;

	public CPacketSyncTileEntity() {
	}

	public CPacketSyncTileEntity(NBTTagCompound syncTag, BlockPos pos, int dimension) {
		this.syncTag = syncTag;
		this.pos = pos;
		this.dimension = dimension;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.syncTag = new PacketBuffer(buf).readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.pos = BlockPos.fromLong(buf.readLong());
		this.dimension = Integer.valueOf(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeCompoundTag(this.syncTag);
		buf.writeLong(this.pos.toLong());
		buf.writeInt(this.dimension);
	}

	@Override
	public IMessage onMessage(CPacketSyncTileEntity message, MessageContext ctx) {
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
		// WIPTech.info("RECIEVED CLIENT->SERVER SYNC");
		// WIPTech.info(message.syncTag);
		// WIPTech.info("_", "_");
		if (message.syncTag != null) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
				TileEntity tile = world.getTileEntity(message.pos);
				if (tile != null && tile instanceof TileEntityBase) {
					// WIPTech.logger.info("syncTag = " + message.syncTag);
					((TileEntityBase) tile).readNBT(message.syncTag, TileEntityBase.NBTType.SYNC);
				}
			});
		}
		return null;
	}

}