package cadiboo.wiptech.handler.network;

import cadiboo.wiptech.tileentity.TileEntityCrusher;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdateCrusher implements IMessage {

	private BlockPos pos;
	private int dimension;
	
	public PacketRequestUpdateCrusher(BlockPos pos, int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}
	
	public PacketRequestUpdateCrusher(TileEntityCrusher te) {
		this(te.getPos(), te.getWorld().provider.getDimension());
	}
	
	public PacketRequestUpdateCrusher() {
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
	
	public static class Handler implements IMessageHandler<PacketRequestUpdateCrusher, PacketUpdateCrusher> {
	
		@Override
		public PacketUpdateCrusher onMessage(PacketRequestUpdateCrusher message, MessageContext ctx) {
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
			TileEntityCrusher te = (TileEntityCrusher)world.getTileEntity(message.pos);
			if (te != null) {
				return new PacketUpdateCrusher(te);
			} else {
				return null;
			}
		}
	
	}

}