package cadiboo.wiptech.init;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;
import cadiboo.wiptech.item.ItemAlumina;
import cadiboo.wiptech.item.ItemAluminiumCoil;
import cadiboo.wiptech.item.ItemAluminiumIngot;
import cadiboo.wiptech.item.ItemAluminiumNugget;
import cadiboo.wiptech.item.ItemBase;
import cadiboo.wiptech.item.ItemCapacitor;
import cadiboo.wiptech.item.ItemCoilgun;
import cadiboo.wiptech.item.ItemCopperCoil;
import cadiboo.wiptech.item.ItemCopperIngot;
import cadiboo.wiptech.item.ItemCopperNugget;
import cadiboo.wiptech.item.ItemCopperStrand;
import cadiboo.wiptech.item.ItemCrusherBit;
import cadiboo.wiptech.item.ItemCrusherBitHolder;
import cadiboo.wiptech.item.ItemFlamethrower;
import cadiboo.wiptech.item.ItemGallium;
import cadiboo.wiptech.item.ItemGalliumIngot;
import cadiboo.wiptech.item.ItemGalliumWafer;
import cadiboo.wiptech.item.ItemGoldCoil;
import cadiboo.wiptech.item.ItemHammer;
import cadiboo.wiptech.item.ItemIronOxide;
import cadiboo.wiptech.item.ItemNapalm;
import cadiboo.wiptech.item.ItemOsmiumIngot;
import cadiboo.wiptech.item.ItemOsmiumNugget;
import cadiboo.wiptech.item.ItemParamagneticProjectile;
import cadiboo.wiptech.item.ItemPlasmagun;
import cadiboo.wiptech.item.ItemRail;
import cadiboo.wiptech.item.ItemRailgun;
import cadiboo.wiptech.item.ItemSilica;
import cadiboo.wiptech.item.ItemSilicon;
import cadiboo.wiptech.item.ItemSiliconWafer;
import cadiboo.wiptech.item.ItemSilverCoil;
import cadiboo.wiptech.item.ItemSilverIngot;
import cadiboo.wiptech.item.ItemSilverNugget;
import cadiboo.wiptech.item.ItemTestLauncher;
import cadiboo.wiptech.item.ItemTestModularWeapon;
import cadiboo.wiptech.item.ItemTinCoil;
import cadiboo.wiptech.item.ItemTinIngot;
import cadiboo.wiptech.item.ItemTinNugget;
import cadiboo.wiptech.item.ItemTitania;
import cadiboo.wiptech.item.ItemTitaniumIngot;
import cadiboo.wiptech.item.ItemTitaniumNugget;
import cadiboo.wiptech.item.ItemTungstenIngot;
import cadiboo.wiptech.item.ItemTungstenNugget;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class Items {

	public static final Item	GOLD_NUGGET	= Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_nugget"));
	public static final Item	GOLD_INGOT	= Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_ingot"));
	public static final Item	IRON_NUGGET	= Item.REGISTRY.getObject(new ResourceLocation("minecraft", "iron_nugget"));
	public static final Item	IRON_INGOT	= Item.REGISTRY.getObject(new ResourceLocation("minecraft", "iron_ingot"));

	// 1.13

	public static final ItemRail	COPPER_RAIL		= new ItemRail("copper_rail", ConductiveMetals.COPPER);
	public static final ItemRail	TIN_RAIL		= new ItemRail("tin_rail", ConductiveMetals.TIN);
	public static final ItemRail	ALUMINIUM_RAIL	= new ItemRail("aluminium_rail", ConductiveMetals.ALUMINIUM);
	public static final ItemRail	SILVER_RAIL		= new ItemRail("silver_rail", ConductiveMetals.SILVER);
	public static final ItemRail	IRON_RAIL		= new ItemRail("iron_rail", ConductiveMetals.IRON);
	public static final ItemRail	GOLD_RAIL		= new ItemRail("gold_rail", ConductiveMetals.GOLD);

	// 1.12
	public static final ItemCopperIngot		COPPER_INGOT	= new ItemCopperIngot("copper_ingot");
	public static final ItemCopperNugget	COPPER_NUGGET	= new ItemCopperNugget("copper_nugget");
	public static final ItemCopperStrand	COPPER_STRAND	= new ItemCopperStrand("copper_strand");
	public static final ItemCopperCoil		COPPER_COIL		= new ItemCopperCoil("copper_coil");

	public static final ItemSilverIngot		SILVER_INGOT	= new ItemSilverIngot("silver_ingot");
	public static final ItemSilverNugget	SILVER_NUGGET	= new ItemSilverNugget("silver_nugget");
	public static final ItemSilverCoil		SILVER_COIL		= new ItemSilverCoil("silver_coil");

	public static final ItemTinIngot	TIN_INGOT	= new ItemTinIngot("tin_ingot");
	public static final ItemTinNugget	TIN_NUGGET	= new ItemTinNugget("tin_nugget");
	public static final ItemTinCoil		TIN_COIL	= new ItemTinCoil("tin_coil");

	public static final ItemAluminiumIngot	ALUMINIUM_INGOT		= new ItemAluminiumIngot("aluminium_ingot");
	public static final ItemAluminiumNugget	ALUMINIUM_NUGGET	= new ItemAluminiumNugget("aluminium_nugget");
	public static final ItemAluminiumCoil	ALUMINIUM_COIL		= new ItemAluminiumCoil("aluminium_coil");

	public static final ItemGoldCoil				GOLD_COIL				= new ItemGoldCoil("gold_coil");
	public static final ItemTungstenIngot			TUNGSTEN_INGOT			= new ItemTungstenIngot("tungsten_ingot");
	public static final ItemTungstenNugget			TUNGSTEN_NUGGET			= new ItemTungstenNugget("tungsten_nugget");
	public static final ItemOsmiumIngot				OSMIUM_INGOT			= new ItemOsmiumIngot("osmium_ingot");
	public static final ItemOsmiumNugget			OSMIUM_NUGGET			= new ItemOsmiumNugget("osmium_nugget");
	public static final ItemParamagneticProjectile	PARAMAGNETIC_PROJECILE	= new ItemParamagneticProjectile("paramagnetic_projectile");
	public static final ItemCrusherBit				CRUSHER_BIT				= new ItemCrusherBit("crusher_bit");
	public static final ItemCrusherBitHolder		CRUSHER_BIT_HOLDER		= new ItemCrusherBitHolder("crusher_bit_holder");
	public static final ItemGallium					GALLIUM					= new ItemGallium("gallium");
	public static final ItemGalliumIngot			GALLIUM_INGOT			= new ItemGalliumIngot("gallium_ingot");
	public static final ItemGalliumWafer			GALLIUM_WAFER			= new ItemGalliumWafer("gallium_wafer");
	public static final ItemAlumina					ALUMINA					= new ItemAlumina("alumina");
	public static final ItemIronOxide				IRON_OXIDE				= new ItemIronOxide("iron_oxide");
	public static final ItemSilica					SILICA					= new ItemSilica("silica");
	public static final ItemSilicon					SILICON					= new ItemSilicon("silicon");
	public static final ItemSiliconWafer			SILICON_WAFER			= new ItemSiliconWafer("silicon_wafer");
	public static final ItemTitania					TITANIA					= new ItemTitania("titania");
	public static final ItemTitaniumIngot			TITANIUM_INGOT			= new ItemTitaniumIngot("titanium_ingot");
	public static final ItemTitaniumNugget			TITANIUM_NUGGET			= new ItemTitaniumNugget("titanium_nugget");
	public static final ItemFlamethrower			FLAMETHROWER			= new ItemFlamethrower("flamethrower");
	public static final ItemNapalm					NAPALM					= new ItemNapalm("napalm");
	public static final ItemHammer					HAMMER					= new ItemHammer("hammer");
	public static final ItemRailgun					RAILGUN					= new ItemRailgun("railgun");
	public static final ItemCoilgun					COILGUN					= new ItemCoilgun("coilgun");
	public static final ItemPlasmagun				PLASMA_GUN				= new ItemPlasmagun("plasmagun");

	public static final ItemCapacitor CAPACITOR = new ItemCapacitor("capacitor");

	public static final ItemTestLauncher		TEST_LAUNCHER		= new ItemTestLauncher("test_launcher");
	public static final ItemTestModularWeapon	TEST_MODULAR_WEAPON	= new ItemTestModularWeapon("test_modular_weapon");

	public static final Item[] ITEMS = {

			HAMMER,

			COPPER_RAIL, TIN_RAIL, ALUMINIUM_RAIL, SILVER_RAIL, IRON_RAIL, GOLD_RAIL,

			// COPPER_INGOT, COPPER_NUGGET, COPPER_STRAND, COPPER_COIL,

			// SILVER_INGOT, SILVER_NUGGET, SILVER_COIL,
			//
			// TIN_INGOT, TIN_NUGGET, TIN_COIL,
			//
			// GOLD_COIL,
			//
			// ALUMINIUM_INGOT, ALUMINIUM_NUGGET, ALUMINIUM_COIL,

			TUNGSTEN_INGOT, TUNGSTEN_NUGGET, OSMIUM_INGOT, OSMIUM_NUGGET, PARAMAGNETIC_PROJECILE,

			CRUSHER_BIT, CRUSHER_BIT_HOLDER,

			GALLIUM, GALLIUM_INGOT, GALLIUM_WAFER, ALUMINA, IRON_OXIDE, SILICA, SILICON, SILICON_WAFER, TITANIA, TITANIUM_INGOT, TITANIUM_NUGGET,

			FLAMETHROWER, NAPALM, RAILGUN, COILGUN, PLASMA_GUN,

			CAPACITOR,

			TEST_LAUNCHER, TEST_MODULAR_WEAPON,

	};

	private static List<Item>	Ingots			= new ArrayList();
	private static Boolean		IngotsIterated	= false;

	public static List<Item> getIngots() {
		if (!IngotsIterated.booleanValue()) {
			Item[] arrayOfItem;
			int j = (arrayOfItem = ITEMS).length;
			for (int i = 0; i < j; i++) {
				Item item = arrayOfItem[i];
				if (!Ingots.contains(item)) {
					if (((item instanceof ItemBase)) && (((ItemBase) item).isIngot())) {
						Ingots.add(item);
					}
				} else {
					IngotsIterated = Boolean.valueOf(true);
				}
			}
		}
		return Ingots;
	}

	private static List<Item>	Nuggets			= new ArrayList();
	private static Boolean		NuggetsIterated	= false;

	public static List<Item> getNuggets() {
		if (!NuggetsIterated.booleanValue()) {
			Item[] arrayOfItem;
			int j = (arrayOfItem = ITEMS).length;
			for (int i = 0; i < j; i++) {
				Item item = arrayOfItem[i];
				if (!Nuggets.contains(item)) {
					if (((item instanceof ItemBase)) && (((ItemBase) item).isNugget())) {
						Nuggets.add(item);
					}
				} else {
					NuggetsIterated = Boolean.valueOf(true);
				}
			}
		}
		return Nuggets;
	}

	private static List<Item>	IngredientItems			= new ArrayList();
	private static Boolean		IngredientItemsIterated	= Boolean.valueOf(false);

	public static List<Item> getIngredientItems() {
		if (!IngredientItemsIterated)
			for (int i = 0; i < ITEMS.length; i++) {
				if (IngredientItems.contains(ITEMS[i])) {
					IngredientItemsIterated = true;
					break;
				}
				if (!(ITEMS[i] instanceof ItemBase))
					break;
				ItemBase item = (ItemBase) ITEMS[i];
				if (item.isNugget() || item.isIngot())
					IngredientItems.add(item);
			}
		return IngredientItems;
	}

}
