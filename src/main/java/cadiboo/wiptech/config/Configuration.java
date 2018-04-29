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

		public Projectile(final boolean HurtEnderman, final boolean HurtWither) {
			this.HurtEnderman = HurtEnderman;
			this.HurtWither = HurtWither;
		}

		@Comment("If projectiles will hurt Endermen")
		public boolean	HurtEnderman;
		@Comment("If projectiles will hurt Withers even if they are Armored")
		public boolean	HurtWither	= false;
	}

	@Comment("Energy Settings")
	public static final Energy energy = new Energy(1, 10, 10, 100000);

	public static class Energy {

		public Energy(final int TurbineProduction, final int CrusherUsage, final int CoilerUsage, final int BaseWireStorage) {
			this.TurbineProduction = TurbineProduction;
			this.CrusherUsage = CrusherUsage;
			this.CoilerUsage = CoilerUsage;
			this.BaseWireStorage = BaseWireStorage;
		}

		@Comment("How many " + Reference.ENERGY_UNIT + "the Turbine produces each tick (multiplied by its Y height)")
		public int	TurbineProduction;
		@Comment("How many " + Reference.ENERGY_UNIT + "the Crusher uses each tick it is active")
		public int	CrusherUsage;
		@Comment("How many " + Reference.ENERGY_UNIT + "the Coiler uses each tick it is active")
		public int	CoilerUsage;
		@Comment("The Storage Capacity of each Wire multiplied by its conductive power")
		public int	BaseWireStorage;
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
