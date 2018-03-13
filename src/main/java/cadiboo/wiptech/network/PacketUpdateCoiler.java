package cadiboo.wiptech.network;

import cadiboo.wiptech.block.coiler.TileEntityCoiler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateCoiler implements IMessage {
	
	
	private BlockPos pos;
	private ItemStack stack;
	private long lastChangeTime;
	private float windTime;

	public PacketUpdateCoiler(BlockPos pos, ItemStack stack, long lastChangeTime, float windTime) {
		this.pos = pos;
		this.stack = stack;
		this.lastChangeTime = lastChangeTime;
		this.windTime = windTime;
	}
	
	public PacketUpdateCoiler(TileEntityCoiler te) {
		this(te.getPos(), te.inventory.getStackInSlot(0), te.lastChangeTime, te.windTime);
	}
	
	public PacketUpdateCoiler() {
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeItemStack(buf, stack);
		buf.writeLong(lastChangeTime);
		buf.writeFloat(windTime);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		stack = ByteBufUtils.readItemStack(buf);
		lastChangeTime = buf.readLong();
		windTime = buf.readFloat();
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateCoiler, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdateCoiler message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				TileEntityCoiler te = (TileEntityCoiler)Minecraft.getMinecraft().world.getTileEntity(message.pos);
				te.inventory.setStackInSlot(0, message.stack);
				te.lastChangeTime = message.lastChangeTime;
				te.windTime = message.windTime;
			});
			return null;
		}
	
	}


}