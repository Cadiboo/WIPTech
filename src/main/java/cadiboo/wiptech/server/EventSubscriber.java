package cadiboo.wiptech.server;

import cadiboo.wiptech.config.Configuration;
import cadiboo.wiptech.handler.network.PacketHandler;
import cadiboo.wiptech.handler.network.PacketSyncConfig;
import cadiboo.wiptech.util.Reference;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.SERVER, modid = Reference.ID)
public class EventSubscriber {

	@SubscribeEvent
	public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
		event.player.sendMessage(new TextComponentString("[WeatherPlus] Client-Side features enabled"));
		System.out.println("[WeatherPlus] Player logged in: " + event.player.getName());

		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("TurbineProduction", Configuration.energy.TurbineProduction);
		nbt.setInteger("CrusherUsage", Configuration.energy.CrusherUsage);
		nbt.setInteger("CoilerUsage", Configuration.energy.CoilerUsage);
		nbt.setInteger("BaseWireStorage", Configuration.energy.BaseWireStorage);

		nbt.setBoolean("HurtEnderman", Configuration.projectile.HurtEnderman);
		nbt.setBoolean("HurtWither", Configuration.projectile.HurtWither);
		PacketHandler.NETWORK.sendTo(new PacketSyncConfig(), (EntityPlayerMP) event.player);
	}

}
