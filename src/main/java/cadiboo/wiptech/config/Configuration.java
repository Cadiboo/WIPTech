package cadiboo.wiptech.config;

import cadiboo.wiptech.util.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@net.minecraftforge.common.config.Config(modid = Reference.ID)
@net.minecraftforge.common.config.Config.LangKey(Reference.ID + ".config.title")
public class Configuration {

	@Comment("Projectile Settings")
	public static final Projectile projectile = new Projectile(false, false);

	public static class Projectile {

		public Projectile(final boolean HurtEnderman, /* final boolean HurtDragon, */ final boolean HurtWither) {
			this.HurtEnderman = HurtEnderman;
			// this.HurtDragon = HurtDragon;
			this.HurtWither = HurtWither;
		}

		@Comment("If projectiles will hurt Endermen")
		public boolean HurtEnderman;
		// @Comment("If projectiles will hurt Dragons even if they are Invulnerable")
		// public boolean HurtDragon = false;
		@Comment("If projectiles will hurt Withers even if they are Armored")
		public boolean HurtWither = false;
	}

	@Mod.EventBusSubscriber(modid = Reference.ID)
	private static class EventHandler {

		/**
		 * Inject the new values and save to the config file when the config has been
		 * changed from the GUI.
		 *
		 * @param event
		 *            The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.ID)) {
				ConfigManager.sync(Reference.ID, Config.Type.INSTANCE);
			}
		}
	}

}
