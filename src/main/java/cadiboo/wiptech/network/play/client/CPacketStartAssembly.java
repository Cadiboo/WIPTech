package cadiboo.wiptech.network.play.client;

import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketStartAssembly implements IMessage, IMessageHandler<CPacketStartAssembly, IMessage> {

	private BlockPos	pos;
	private int			dimension;

	public CPacketStartAssembly() {
	}

	public CPacketStartAssembly(final BlockPos pos, final int dimension) {
		this.pos = pos;
		this.dimension = dimension;
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.dimension = Integer.valueOf(buf.readInt());
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeInt(this.dimension);
	}

	/**
	 * Handle packet on server recieved from client
	 */
	@Override
//	@SideOnly(Side.SERVER)
	public IMessage onMessage(final CPacketStartAssembly message, final MessageContext ctx) {
		final World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
			final TileEntity tile = world.getTileEntity(message.pos);

			if ((tile != null) && (tile instanceof TileEntityAssemblyTable)) {
				((TileEntityAssemblyTable) tile).startAssembly();
			}
		});
		return null;
	}

}
