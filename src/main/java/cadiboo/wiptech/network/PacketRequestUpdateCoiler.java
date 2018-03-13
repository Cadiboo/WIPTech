package cadiboo.wiptech.network;

import cadiboo.wiptech.block.coiler.TileEntityCoiler;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdateCoiler implements IMessage {

	private BlockPos pos;
	private int dimension;
	
	public PacketRequestUpdateCoiler(BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}
	
	public PacketRequestUpdateCoiler(TileEntityCoiler te) {
		this(te.getPos(), te.getWorld().provider.getDimension());
	}
	
	public PacketRequestUpdateCoiler() {
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		buf.writeInt(dimension);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		dimension = buf.readInt();
	}
	
	public static class Handler implements IMessageHandler<PacketRequestUpdateCoiler, PacketUpdateCoiler> {
	
		@Override
		public PacketUpdateCoiler onMessage(PacketRequestUpdateCoiler message, MessageContext ctx) {
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
			TileEntityCoiler te = (TileEntityCoiler)world.getTileEntity(message.pos);
			if (te != null) {
				return new PacketUpdateCoiler(te);
			} else {
				return null;
			}
		}
	
	}

}