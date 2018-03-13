package cadiboo.wiptech.network;

import cadiboo.wiptech.block.crusher.TileEntityCrusher;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateCrusher implements IMessage {
	
	
	private BlockPos pos;
	private ItemStack stack;
	private long lastChangeTime;
	private float crushTime;

	public PacketUpdateCrusher(BlockPos pos, ItemStack stack, long lastChangeTime, float crushTime) {
		this.pos = pos;
		this.stack = stack;
		this.lastChangeTime = lastChangeTime;
		this.crushTime = crushTime;
	}
	
	public PacketUpdateCrusher(TileEntityCrusher te) {
		this(te.getPos(), te.inventory.getStackInSlot(0), te.lastChangeTime, te.crushTime);
	}
	
	public PacketUpdateCrusher() {
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeItemStack(buf, stack);
		buf.writeLong(lastChangeTime);
		buf.writeFloat(crushTime);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		stack = ByteBufUtils.readItemStack(buf);
		lastChangeTime = buf.readLong();
		crushTime = buf.readFloat();
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateCrusher, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdateCrusher message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				TileEntityCrusher te = (TileEntityCrusher)Minecraft.getMinecraft().world.getTileEntity(message.pos);
				te.inventory.setStackInSlot(0, message.stack);
				te.lastChangeTime = message.lastChangeTime;
				te.crushTime = message.crushTime;
			});
			return null;
		}
	
	}


}