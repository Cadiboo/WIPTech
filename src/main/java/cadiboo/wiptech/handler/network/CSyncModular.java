package cadiboo.wiptech.handler.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import cadiboo.wiptech.capability.ModularData;
import cadiboo.wiptech.capability.ModularDataCapability;

public class CSyncModular implements IMessage{

	public CSyncModular() {
	}

	public boolean module;
	public int entityId;

	public CSyncModular(boolean module, int entityId) {
		this.module = module;
		this.entityId=entityId;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		module = buf.readBoolean();
		entityId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(module);
		buf.writeInt(entityId);
	}

	public static class CSyncModularHandler implements IMessageHandler<CSyncModular, IMessage>
	{
		@Override
		public IMessage onMessage(CSyncModular message, MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(() ->{
				Entity e = Minecraft.getMinecraft().player.world.getEntityByID(message.entityId);

				if(e instanceof EntityLivingBase)
				{
					EntityLivingBase el = (EntityLivingBase)e;

					if(el.hasCapability(ModularDataCapability.CAPABILITY, null))
					{
						ModularData data = ModularData.get(el);

						if(message.module)
						{
							data.startToFreeze();

							//jitter fix ?
							el.prevLimbSwingAmount = el.limbSwingAmount;
							el.prevCameraPitch = el.cameraPitch;
							el.prevPosX = el.posX;
							el.prevPosY = el.posY;
							el.prevPosZ = el.posZ;
							el.prevSwingProgress = el.swingProgressInt;
							el.prevRenderYawOffset = el.renderYawOffset;
							el.prevRotationPitch = el.rotationPitch;
							el.prevRotationYaw = el.rotationYaw;
							el.prevRotationYawHead = el.rotationYawHead;
						}
						else
							data.stopFreeze();
					}
				}
			});

			return null;
		}
	}
}