package cadiboo.wiptech.network;

import cadiboo.wiptech.block.coiler.TileEntityCoiler;
import cadiboo.wiptech.block.crusher.TileEntityCrusher;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.ItemStackHandler;

public class PacketUpdateCoiler implements IMessage {
	private BlockPos pos;
	private ItemStack stack0;
	private ItemStack stack1;
	private ItemStack stack2;
	private ItemStack stack3;
	private ItemStack stack4;
	private ItemStack stack5;
	private ItemStack stack6;
	private ItemStack stack7;
	private ItemStack stack8;
	private ItemStack stack9;
	private long lastChangeTime;
	private float windTime;

	public PacketUpdateCoiler(BlockPos pos, ItemStack stack0, ItemStack stack1, ItemStack stack2, ItemStack stack3, ItemStack stack4, ItemStack stack5, ItemStack stack6, ItemStack stack7, ItemStack stack8, ItemStack stack9, long lastChangeTime, float windTime)
	{
		this.pos = pos;
		this.stack0 = stack0;
		this.stack1 = stack1;
		this.stack2 = stack2;
		this.stack3 = stack3;
		this.stack4 = stack4;
		this.stack5 = stack5;
		this.stack6 = stack6;
		this.stack7 = stack7;
		this.stack8 = stack8;
		this.stack9 = stack9;
		this.lastChangeTime = lastChangeTime;
		this.windTime = windTime;
	}

	public PacketUpdateCoiler(TileEntityCoiler te) {
		this(te.getPos(), te.inventory.getStackInSlot(0), te.inventory.getStackInSlot(1), te.inventory.getStackInSlot(2), te.inventory.getStackInSlot(3), te.inventory.getStackInSlot(4), te.inventory.getStackInSlot(5), te.inventory.getStackInSlot(6), te.inventory.getStackInSlot(7), te.inventory.getStackInSlot(8), te.inventory.getStackInSlot(9), te.lastChangeTime, te.windTime);
	}


	public PacketUpdateCoiler() {}

	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeItemStack(buf, stack0);
		ByteBufUtils.writeItemStack(buf, stack1);
		ByteBufUtils.writeItemStack(buf, stack2);
		ByteBufUtils.writeItemStack(buf, stack3);
		ByteBufUtils.writeItemStack(buf, stack4);
		ByteBufUtils.writeItemStack(buf, stack5);
		ByteBufUtils.writeItemStack(buf, stack6);
		ByteBufUtils.writeItemStack(buf, stack7);
		ByteBufUtils.writeItemStack(buf, stack8);
		ByteBufUtils.writeItemStack(buf, stack9);
		buf.writeLong(lastChangeTime);
		buf.writeFloat(windTime);
	}

	public void fromBytes(ByteBuf buf)
	{
		pos = BlockPos.fromLong(buf.readLong());
		stack0 = ByteBufUtils.readItemStack(buf);
		stack1 = ByteBufUtils.readItemStack(buf);
		stack2 = ByteBufUtils.readItemStack(buf);
		stack3 = ByteBufUtils.readItemStack(buf);
		stack4 = ByteBufUtils.readItemStack(buf);
		stack5 = ByteBufUtils.readItemStack(buf);
		stack6 = ByteBufUtils.readItemStack(buf);
		stack7 = ByteBufUtils.readItemStack(buf);
		stack8 = ByteBufUtils.readItemStack(buf);
		stack9 = ByteBufUtils.readItemStack(buf);
		lastChangeTime = buf.readLong();
		windTime = buf.readFloat();
	}

	public static class Handler implements IMessageHandler<PacketUpdateCoiler, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdateCoiler message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				TileEntityCoiler te = (TileEntityCoiler)Minecraft.getMinecraft().world.getTileEntity(message.pos);
				te.inventory.setStackInSlot(0, message.stack0);
				te.inventory.setStackInSlot(1, message.stack1);
				te.inventory.setStackInSlot(2, message.stack2);
				te.inventory.setStackInSlot(3, message.stack3);
				te.inventory.setStackInSlot(4, message.stack4);
				te.inventory.setStackInSlot(5, message.stack5);
				te.inventory.setStackInSlot(6, message.stack6);
				te.inventory.setStackInSlot(7, message.stack7);
				te.lastChangeTime = message.lastChangeTime;
				te.windTime = message.windTime;
			});
			return null;
		}

	}
}