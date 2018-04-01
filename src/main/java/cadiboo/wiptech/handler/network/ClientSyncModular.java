package cadiboo.wiptech.handler.network;

import cadiboo.wiptech.capability.IWeaponModular;
import cadiboo.wiptech.init.Capabilities;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public /**/abstract/**/ class ClientSyncModular implements IMessage{

	public ClientSyncModular() {
	}

	/*public boolean freeze;
	public int entityId;

	public ClientSyncModular(boolean freeze, int entityId) {
		this.freeze = freeze;
		this.entityId=entityId;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		freeze = buf.readBoolean();
		entityId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(freeze);
		buf.writeInt(entityId);
	}

	public static class ClientSyncModularHandler implements IMessageHandler<ClientSyncModular, IMessage>
	{
		@Override
		public IMessage onMessage(ClientSyncModular message, MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(() ->{
				Entity e = Minecraft.getMinecraft().player.world.getEntityByID(message.entityId);

				if(e instanceof EntityLivingBase)
				{
					EntityLivingBase el = (EntityLivingBase)e;

					if(el.hasCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null))
					{
						IWeaponModular data = el.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null);

						/*if(message.freeze)
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
							*\/
					}
				}
			});

			return null;
		}
	}*/
}