package cadiboo.wiptech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cadiboo.wiptech.handler.GuiHandler;
import cadiboo.wiptech.handler.network.PacketHandler;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.init.Recipes;
import cadiboo.wiptech.util.IProxy;
import cadiboo.wiptech.util.Reference;
import cadiboo.wiptech.world.WorldGen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS)
public class WIPTech {

	@Instance(Reference.ID)
	public static WIPTech instance;

	@SidedProxy(serverSide = Reference.SERVER_PROXY_CLASS, clientSide = Reference.CLIENT_PROXY_CLASS)
	public static IProxy proxy;

	public static final Logger logger = LogManager.getLogger(Reference.ID);

	// HALF DONE STOP ARROW-LIKE BOUNCE OFF - Partly done, they now either go
	// through the mob or teleport into the ground a block below it... //DONE maybe
	// reenable it
	// TODO find out ...I forgot what I was writing
	// TODO Find out if dammage is set to Projectiles correctly
	// TODO STOP SPELLING DAMAGE WITH TWO Ms
	// TODO does /giving myself a Plasma still throw an error?
	// DONE Implement my Modular capability
	// TODO Maybe implement a custom renderer for it? so that you can see what
	// augments you have on it
	// HALF DONE Def make it display the modules/augments in the ItemStack's Tooltip
	// HALF DONE make Plasma Cannon!!!
	// TODO and drill
	// DOING get some sleep
	// TODO redo knockback
	// HALF DONE redo Projectile spawning
	// HALF DONE capabilities
	// TODO make EntityRailgun rideable
	// TODO redo itemStack proj consumption coilgun
	// TODO RIGHT CLICK ANVIL
	// TODO PLACE BLOCKS
	// TODO STOP GUI
	// TODO Overheat
	// DOING capacitors
	// TODO FE
	// DOING ^
	// TODO Knockback
	// TODO damage nerf
	// TODO check Capabilities modules NBT
	// DONE WHy are projectiles bouncing off bosses? IDK but they aren't anymore
	// DONE copy how ender gateways are rendered - used Dragon Death layer rendering
	// TODO WHY ISNT ENERGY SYNCED!!!

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		WIPTech.proxy.logLogicalSide();
		WIPTech.proxy.addToCreativeTab();

		new PacketHandler();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		Capabilities.registerCapabilities();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
		Recipes.registerRecipes();
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		// Mod compatibility, or anything which depends on other mods’ init phases being
		// finished.
	}

}