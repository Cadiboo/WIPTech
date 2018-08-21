package cadiboo.wiptech.network.play.client;

import java.io.IOException;

import cadiboo.wiptech.tileentity.ITileEntitySyncable;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketSyncModTileEntity implements IMessage, IMessageHandler<CPacketSyncModTileEntity, IMessage> {

	private NBTTagCompound syncTag;
	private BlockPos pos;
	private int dimension;

	public CPacketSyncModTileEntity() {
	}

	public CPacketSyncModTileEntity(NBTTagCompound syncTag, BlockPos pos, int dimension) {
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

	/**
	 * Handle packet on server recieved from client
	 */
	@Override
	@SideOnly(Side.SERVER)
	public IMessage onMessage(CPacketSyncModTileEntity message, MessageContext ctx) {
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
		if (message.syncTag != null) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
				TileEntity tile = world.getTileEntity(message.pos);
				if (tile != null && tile instanceof ITileEntitySyncable) {
					((ITileEntitySyncable) tile).readNBT(message.syncTag);
				}
			});
		}
		return null;
	}

}