package cadiboo.wiptech.handler.network;

import cadiboo.wiptech.tileentity.TileEntityCrusher;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.ItemStackHandler;


public class PacketUpdateCrusher implements IMessage {
  private BlockPos pos;
  private ItemStack stack0;
  private ItemStack stack1;
  private ItemStack stack2;
  private ItemStack stack3;
  private ItemStack stack4;
  private ItemStack stack5;
  private ItemStack stack6;
  private ItemStack stack7;
  private long lastChangeTime;
  private float crushTime;
  
  public PacketUpdateCrusher(BlockPos pos, ItemStack stack0, ItemStack stack1, ItemStack stack2, ItemStack stack3, ItemStack stack4, ItemStack stack5, ItemStack stack6, ItemStack stack7, long lastChangeTime, float crushTime)
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
    
    this.lastChangeTime = lastChangeTime;
    this.crushTime = crushTime;
  }
  
  public PacketUpdateCrusher(TileEntityCrusher te) {
    this(te.getPos(), te.inventory.getStackInSlot(0), te.inventory.getStackInSlot(1), te.inventory.getStackInSlot(2), te.inventory.getStackInSlot(3), te.inventory.getStackInSlot(4), te.inventory.getStackInSlot(5), te.inventory.getStackInSlot(6), te.inventory.getStackInSlot(7), te.lastChangeTime, te.crushTime);
  }
  

  public PacketUpdateCrusher() {}
  
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
    buf.writeLong(lastChangeTime);
    buf.writeFloat(crushTime);
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
    lastChangeTime = buf.readLong();
    crushTime = buf.readFloat();
  }
  
  public static class Handler implements IMessageHandler<PacketUpdateCrusher, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdateCrusher message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				TileEntityCrusher te = (TileEntityCrusher)Minecraft.getMinecraft().world.getTileEntity(message.pos);
				te.inventory.setStackInSlot(0, message.stack0);
				te.inventory.setStackInSlot(1, message.stack1);
				te.inventory.setStackInSlot(2, message.stack2);
				te.inventory.setStackInSlot(3, message.stack3);
				te.inventory.setStackInSlot(4, message.stack4);
				te.inventory.setStackInSlot(5, message.stack5);
				te.inventory.setStackInSlot(6, message.stack6);
				te.inventory.setStackInSlot(7, message.stack7);
				te.lastChangeTime = message.lastChangeTime;
				te.crushTime = message.crushTime;
			});
			return null;
		}
	
	}
  
}