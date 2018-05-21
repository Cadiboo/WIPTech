package cadiboo.wiptech.init;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectiles;
import cadiboo.wiptech.handler.EnumHandler.ToolTypes;
import cadiboo.wiptech.item.ItemArmor;
import cadiboo.wiptech.item.ItemBase;
import cadiboo.wiptech.item.ItemCapacitor;
import cadiboo.wiptech.item.ItemCoil;
import cadiboo.wiptech.item.ItemCoilgun113;
import cadiboo.wiptech.item.ItemFlamethrower;
import cadiboo.wiptech.item.ItemGallium;
import cadiboo.wiptech.item.ItemGalliumIngot;
import cadiboo.wiptech.item.ItemHammer;
import cadiboo.wiptech.item.ItemMissileLauncher;
import cadiboo.wiptech.item.ItemParamagneticProjectile;
import cadiboo.wiptech.item.ItemParamagneticProjectile113;
import cadiboo.wiptech.item.ItemPlasmaTool;
import cadiboo.wiptech.item.ItemPlasmagun113;
import cadiboo.wiptech.item.ItemRail;
import cadiboo.wiptech.item.ItemRailgun113;
import cadiboo.wiptech.item.ItemSilicon;
import cadiboo.wiptech.item.ItemSiliconWafer;
import cadiboo.wiptech.item.ItemStandaloneGun;
import cadiboo.wiptech.item.ItemTaser;
import cadiboo.wiptech.item.ItemTestLauncher;
import cadiboo.wiptech.item.ItemTestModularWeapon;
import cadiboo.wiptech.item.ItemTool;
import net.minecraft.inventory.EntityEquipmentSlot;
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

	public static final ItemCoil	COPPER_COIL		= new ItemCoil("copper_coil", ConductiveMetals.COPPER);
	public static final ItemCoil	TIN_COIL		= new ItemCoil("tin_coil", ConductiveMetals.TIN);
	public static final ItemCoil	ALUMINIUM_COIL	= new ItemCoil("aluminium_coil", ConductiveMetals.ALUMINIUM);
	public static final ItemCoil	SILVER_COIL		= new ItemCoil("silver_coil", ConductiveMetals.SILVER);
	public static final ItemCoil	IRON_COIL		= new ItemCoil("iron_coil", ConductiveMetals.IRON);
	public static final ItemCoil	GOLD_COIL		= new ItemCoil("gold_coil", ConductiveMetals.GOLD);

	public static final ItemBase	TUNGSTEN_INGOT	= new ItemBase("tungsten_ingot").setBeaconPayment();
	public static final ItemBase	OSMIUM_INGOT	= new ItemBase("osmium_ingot").setBeaconPayment();
	public static final ItemBase	TITANIUM_INGOT	= new ItemBase("titanium_ingot").setBeaconPayment();

	public static final ItemBase	TUNGSTEN_NUGGET	= new ItemBase("tungsten_nugget").setBeaconPayment();
	public static final ItemBase	OSMIUM_NUGGET	= new ItemBase("osmium_nugget").setBeaconPayment();
	public static final ItemBase	TITANIUM_NUGGET	= new ItemBase("titanium_nugget").setBeaconPayment();

	public static final ItemTool	COPPER_SHOVEL		= new ItemTool("copper_shovel", ItemTool.COPPER, ItemTool.SPADE_EFFECTIVE_ON, ToolTypes.SHOVEL);
	public static final ItemTool	TIN_SHOVEL			= new ItemTool("tin_shovel", ItemTool.TIN, ItemTool.SPADE_EFFECTIVE_ON, ToolTypes.SHOVEL);
	public static final ItemTool	ALUMINIUM_SHOVEL	= new ItemTool("aluminium_shovel", ItemTool.ALUMINIUM, ItemTool.SPADE_EFFECTIVE_ON, ToolTypes.SHOVEL);
	public static final ItemTool	SILVER_SHOVEL		= new ItemTool("silver_shovel", ItemTool.SILVER, ItemTool.SPADE_EFFECTIVE_ON, ToolTypes.SHOVEL);
	public static final ItemTool	TUNGSTEN_SHOVEL		= new ItemTool("tungsten_shovel", ItemTool.TUNGSTEN, ItemTool.SPADE_EFFECTIVE_ON, ToolTypes.SHOVEL);
	public static final ItemTool	OSMIUM_SHOVEL		= new ItemTool("osmium_shovel", ItemTool.OSMIUM, ItemTool.SPADE_EFFECTIVE_ON, ToolTypes.SHOVEL);
	public static final ItemTool	TITANIUM_SHOVEL		= new ItemTool("titanium_shovel", ItemTool.TITANIUM, ItemTool.SPADE_EFFECTIVE_ON, ToolTypes.SHOVEL);

	public static final ItemTool	COPPER_PICKAXE		= new ItemTool("copper_pickaxe", ItemTool.COPPER, ItemTool.PICKAXE_EFFECTIVE_ON, ToolTypes.PICKAXE);
	public static final ItemTool	TIN_PICKAXE			= new ItemTool("tin_pickaxe", ItemTool.TIN, ItemTool.PICKAXE_EFFECTIVE_ON, ToolTypes.PICKAXE);
	public static final ItemTool	ALUMINIUM_PICKAXE	= new ItemTool("aluminium_pickaxe", ItemTool.ALUMINIUM, ItemTool.PICKAXE_EFFECTIVE_ON, ToolTypes.PICKAXE);
	public static final ItemTool	SILVER_PICKAXE		= new ItemTool("silver_pickaxe", ItemTool.SILVER, ItemTool.PICKAXE_EFFECTIVE_ON, ToolTypes.PICKAXE);
	public static final ItemTool	TUNGSTEN_PICKAXE	= new ItemTool("tungsten_pickaxe", ItemTool.TUNGSTEN, ItemTool.PICKAXE_EFFECTIVE_ON, ToolTypes.PICKAXE);
	public static final ItemTool	OSMIUM_PICKAXE		= new ItemTool("osmium_pickaxe", ItemTool.OSMIUM, ItemTool.PICKAXE_EFFECTIVE_ON, ToolTypes.PICKAXE);
	public static final ItemTool	TITANIUM_PICKAXE	= new ItemTool("titanium_pickaxe", ItemTool.TITANIUM, ItemTool.PICKAXE_EFFECTIVE_ON, ToolTypes.PICKAXE);

	public static final ItemTool	COPPER_SWORD	= new ItemTool("copper_sword", ItemTool.COPPER, ItemTool.SWORD_EFFECTIVE_ON, ToolTypes.SWORD);
	public static final ItemTool	TIN_SWORD		= new ItemTool("tin_sword", ItemTool.TIN, ItemTool.SWORD_EFFECTIVE_ON, ToolTypes.SWORD);
	public static final ItemTool	ALUMINIUM_SWORD	= new ItemTool("aluminium_sword", ItemTool.ALUMINIUM, ItemTool.SWORD_EFFECTIVE_ON, ToolTypes.SWORD);
	public static final ItemTool	SILVER_SWORD	= new ItemTool("silver_sword", ItemTool.SILVER, ItemTool.SWORD_EFFECTIVE_ON, ToolTypes.SWORD);
	public static final ItemTool	TUNGSTEN_SWORD	= new ItemTool("tungsten_sword", ItemTool.TUNGSTEN, ItemTool.SWORD_EFFECTIVE_ON, ToolTypes.SWORD);
	public static final ItemTool	OSMIUM_SWORD	= new ItemTool("osmium_sword", ItemTool.OSMIUM, ItemTool.SWORD_EFFECTIVE_ON, ToolTypes.SWORD);
	public static final ItemTool	TITANIUM_SWORD	= new ItemTool("titanium_sword", ItemTool.TITANIUM, ItemTool.SWORD_EFFECTIVE_ON, ToolTypes.SWORD);

	public static final ItemTool	COPPER_HOE		= new ItemTool("copper_hoe", ItemTool.COPPER, ItemTool.HOE_EFFECTIVE_ON, ToolTypes.HOE);
	public static final ItemTool	TIN_HOE			= new ItemTool("tin_hoe", ItemTool.TIN, ItemTool.HOE_EFFECTIVE_ON, ToolTypes.HOE);
	public static final ItemTool	ALUMINIUM_HOE	= new ItemTool("aluminium_hoe", ItemTool.ALUMINIUM, ItemTool.HOE_EFFECTIVE_ON, ToolTypes.HOE);
	public static final ItemTool	SILVER_HOE		= new ItemTool("silver_hoe", ItemTool.SILVER, ItemTool.HOE_EFFECTIVE_ON, ToolTypes.HOE);
	public static final ItemTool	TUNGSTEN_HOE	= new ItemTool("tungsten_hoe", ItemTool.TUNGSTEN, ItemTool.HOE_EFFECTIVE_ON, ToolTypes.HOE);
	public static final ItemTool	OSMIUM_HOE		= new ItemTool("osmium_hoe", ItemTool.OSMIUM, ItemTool.HOE_EFFECTIVE_ON, ToolTypes.HOE);
	public static final ItemTool	TITANIUM_HOE	= new ItemTool("titanium_hoe", ItemTool.TITANIUM, ItemTool.HOE_EFFECTIVE_ON, ToolTypes.HOE);

	public static final ItemTool	COPPER_AXE		= new ItemTool("copper_axe", ItemTool.COPPER, ItemTool.AXE_EFFECTIVE_ON, ToolTypes.AXE);
	public static final ItemTool	TIN_AXE			= new ItemTool("tin_axe", ItemTool.TIN, ItemTool.AXE_EFFECTIVE_ON, ToolTypes.AXE);
	public static final ItemTool	ALUMINIUM_AXE	= new ItemTool("aluminium_axe", ItemTool.ALUMINIUM, ItemTool.AXE_EFFECTIVE_ON, ToolTypes.AXE);
	public static final ItemTool	SILVER_AXE		= new ItemTool("silver_axe", ItemTool.SILVER, ItemTool.AXE_EFFECTIVE_ON, ToolTypes.AXE);
	public static final ItemTool	TUNGSTEN_AXE	= new ItemTool("tungsten_axe", ItemTool.TUNGSTEN, ItemTool.AXE_EFFECTIVE_ON, ToolTypes.AXE);
	public static final ItemTool	OSMIUM_AXE		= new ItemTool("osmium_axe", ItemTool.OSMIUM, ItemTool.AXE_EFFECTIVE_ON, ToolTypes.AXE);
	public static final ItemTool	TITANIUM_AXE	= new ItemTool("titanium_axe", ItemTool.TITANIUM, ItemTool.AXE_EFFECTIVE_ON, ToolTypes.AXE);

	public static final ItemArmor	COPPER_HELMET		= new ItemArmor("copper_helmet", ItemArmor.COPPER, 2, EntityEquipmentSlot.HEAD);
	public static final ItemArmor	TIN_HELMET			= new ItemArmor("tin_helmet", ItemArmor.TIN, 2, EntityEquipmentSlot.HEAD);
	public static final ItemArmor	ALUMINIUM_HELMET	= new ItemArmor("aluminium_helmet", ItemArmor.ALUMINIUM, 2, EntityEquipmentSlot.HEAD);
	public static final ItemArmor	SILVER_HELMET		= new ItemArmor("silver_helmet", ItemArmor.SILVER, 2, EntityEquipmentSlot.HEAD);
	public static final ItemArmor	TUNGSTEN_HELMET		= new ItemArmor("tungsten_helmet", ItemArmor.TUNGSTEN, 2, EntityEquipmentSlot.HEAD);
	public static final ItemArmor	OSMIUM_HELMET		= new ItemArmor("osmium_helmet", ItemArmor.OSMIUM, 2, EntityEquipmentSlot.HEAD);
	public static final ItemArmor	TITANIUM_HELMET		= new ItemArmor("titanium_helmet", ItemArmor.TITANIUM, 2, EntityEquipmentSlot.HEAD);

	public static final ItemArmor	COPPER_CHESTPLATE		= new ItemArmor("copper_chestplate", ItemArmor.COPPER, 2, EntityEquipmentSlot.CHEST);
	public static final ItemArmor	TIN_CHESTPLATE			= new ItemArmor("tin_chestplate", ItemArmor.TIN, 2, EntityEquipmentSlot.CHEST);
	public static final ItemArmor	ALUMINIUM_CHESTPLATE	= new ItemArmor("aluminium_chestplate", ItemArmor.ALUMINIUM, 2, EntityEquipmentSlot.CHEST);
	public static final ItemArmor	SILVER_CHESTPLATE		= new ItemArmor("silver_chestplate", ItemArmor.SILVER, 2, EntityEquipmentSlot.CHEST);
	public static final ItemArmor	TUNGSTEN_CHESTPLATE		= new ItemArmor("tungsten_chestplate", ItemArmor.TUNGSTEN, 2, EntityEquipmentSlot.CHEST);
	public static final ItemArmor	OSMIUM_CHESTPLATE		= new ItemArmor("osmium_chestplate", ItemArmor.OSMIUM, 2, EntityEquipmentSlot.CHEST);
	public static final ItemArmor	TITANIUM_CHESTPLATE		= new ItemArmor("titanium_chestplate", ItemArmor.TITANIUM, 2, EntityEquipmentSlot.CHEST);

	public static final ItemArmor	COPPER_LEGGINGS		= new ItemArmor("copper_leggings", ItemArmor.COPPER, 2, EntityEquipmentSlot.LEGS);
	public static final ItemArmor	TIN_LEGGINGS		= new ItemArmor("tin_leggings", ItemArmor.TIN, 2, EntityEquipmentSlot.LEGS);
	public static final ItemArmor	ALUMINIUM_LEGGINGS	= new ItemArmor("aluminium_leggings", ItemArmor.ALUMINIUM, 2, EntityEquipmentSlot.LEGS);
	public static final ItemArmor	SILVER_LEGGINGS		= new ItemArmor("silver_leggings", ItemArmor.SILVER, 2, EntityEquipmentSlot.LEGS);
	public static final ItemArmor	TUNGSTEN_LEGGINGS	= new ItemArmor("tungsten_leggings", ItemArmor.TUNGSTEN, 2, EntityEquipmentSlot.LEGS);
	public static final ItemArmor	OSMIUM_LEGGINGS		= new ItemArmor("osmium_leggings", ItemArmor.OSMIUM, 2, EntityEquipmentSlot.LEGS);
	public static final ItemArmor	TITANIUM_LEGGINGS	= new ItemArmor("titanium_leggings", ItemArmor.TITANIUM, 2, EntityEquipmentSlot.LEGS);

	public static final ItemArmor	COPPER_BOOTS	= new ItemArmor("copper_boots", ItemArmor.COPPER, 2, EntityEquipmentSlot.FEET);
	public static final ItemArmor	TIN_BOOTS		= new ItemArmor("tin_boots", ItemArmor.TIN, 2, EntityEquipmentSlot.FEET);
	public static final ItemArmor	ALUMINIUM_BOOTS	= new ItemArmor("aluminium_boots", ItemArmor.ALUMINIUM, 2, EntityEquipmentSlot.FEET);
	public static final ItemArmor	SILVER_BOOTS	= new ItemArmor("silver_boots", ItemArmor.SILVER, 2, EntityEquipmentSlot.FEET);
	public static final ItemArmor	TUNGSTEN_BOOTS	= new ItemArmor("tungsten_boots", ItemArmor.TUNGSTEN, 2, EntityEquipmentSlot.FEET);
	public static final ItemArmor	OSMIUM_BOOTS	= new ItemArmor("osmium_boots", ItemArmor.OSMIUM, 2, EntityEquipmentSlot.FEET);
	public static final ItemArmor	TITANIUM_BOOTS	= new ItemArmor("titanium_boots", ItemArmor.TITANIUM, 2, EntityEquipmentSlot.FEET);

	public static final ItemBase	CRUSHER_BIT			= new ItemBase("crusher_bit");
	public static final ItemBase	CRUSHER_BIT_HOLDER	= new ItemBase("crusher_bit_holder");

	public static final ItemBase	GALLIUM_WAFER	= new ItemBase("gallium_wafer");
	public static final ItemBase	ALUMINA			= new ItemBase("alumina");
	public static final ItemBase	IRON_OXIDE		= new ItemBase("iron_oxide");
	public static final ItemBase	SILICA			= new ItemBase("silica");
	public static final ItemBase	TITANIA			= new ItemBase("titania");

	public static final ItemRailgun113 RAILGUN = new ItemRailgun113("railgun");
	// public static final ItemTestEnergyGun TEST_ENERGY_GUN = new
	// ItemTestEnergyGun("test_energy_gun");
	public static final ItemStandaloneGun	STANDALONE_GUN	= new ItemStandaloneGun("standalone_gun");
	public static final ItemPlasmagun113	PLASMA_GUN		= new ItemPlasmagun113("plasmagun");
	public static final ItemCoilgun113		COILGUN			= new ItemCoilgun113("coilgun");

	public static final ItemMissileLauncher MISSILE_LAUNCHER = new ItemMissileLauncher("missile_launcher");

	public static final ItemBase GUN_BODY = new ItemBase("gun_body");

	// semi

	public static final ItemParamagneticProjectile113	IRON_SMALL		= new ItemParamagneticProjectile113("iron_small", ParamagneticProjectiles.IRON_SMALL);
	public static final ItemParamagneticProjectile113	OSMIUM_SMALL	= new ItemParamagneticProjectile113("osmium_small", ParamagneticProjectiles.OSMIUM_SMALL);
	public static final ItemParamagneticProjectile113	TUNGSTEN_SMALL	= new ItemParamagneticProjectile113("tungsten_small", ParamagneticProjectiles.TUNGSTEN_SMALL);

	public static final ItemParamagneticProjectile113	IRON_MEDIUM		= new ItemParamagneticProjectile113("iron_medium", ParamagneticProjectiles.IRON_MEDIUM);
	public static final ItemParamagneticProjectile113	OSMIUM_MEDIUM	= new ItemParamagneticProjectile113("osmium_medium", ParamagneticProjectiles.OSMIUM_MEDIUM);
	public static final ItemParamagneticProjectile113	TUNGSTEN_MEDIUM	= new ItemParamagneticProjectile113("tungsten_medium", ParamagneticProjectiles.TUNGSTEN_MEDIUM);

	public static final ItemParamagneticProjectile113	IRON_LARGE		= new ItemParamagneticProjectile113("iron_large", ParamagneticProjectiles.IRON_LARGE);
	public static final ItemParamagneticProjectile113	OSMIUM_LARGE	= new ItemParamagneticProjectile113("osmium_large", ParamagneticProjectiles.OSMIUM_LARGE);
	public static final ItemParamagneticProjectile113	TUNGSTEN_LARGE	= new ItemParamagneticProjectile113("tungsten_large", ParamagneticProjectiles.TUNGSTEN_LARGE);

	// 1.12

	public static final ItemParamagneticProjectile	PARAMAGNETIC_PROJECILE	= new ItemParamagneticProjectile("paramagnetic_projectile");
	public static final ItemGallium					GALLIUM					= new ItemGallium("gallium");
	public static final ItemGalliumIngot			GALLIUM_INGOT			= new ItemGalliumIngot("gallium_ingot");
	public static final ItemSilicon					SILICON					= new ItemSilicon("silicon");
	public static final ItemSiliconWafer			SILICON_WAFER			= new ItemSiliconWafer("silicon_wafer");
	public static final ItemFlamethrower			FLAMETHROWER			= new ItemFlamethrower("flamethrower");
	public static final ItemHammer					HAMMER					= new ItemHammer("hammer");
	// public static final ItemRailgun RAILGUN = new ItemRailgun("railgun");
	// public static final ItemCoilgun COILGUN = new ItemCoilgun("coilgun");
	// public static final ItemPlasmagun PLASMA_GUN = new
	// ItemPlasmagun("plasmagun");

	public static final ItemCapacitor CAPACITOR = new ItemCapacitor("capacitor");

	public static final ItemTestLauncher		TEST_LAUNCHER		= new ItemTestLauncher("test_launcher");
	public static final ItemTestModularWeapon	TEST_MODULAR_WEAPON	= new ItemTestModularWeapon("test_modular_weapon");

	public static final ItemTaser		TASER		= new ItemTaser("taser");
	public static final ItemPlasmaTool	PLASMA_TOOL	= new ItemPlasmaTool("plasma_tool");

	public static final Item[] ITEMS = {

			COPPER_RAIL, TIN_RAIL, ALUMINIUM_RAIL, SILVER_RAIL, IRON_RAIL, GOLD_RAIL,

			COPPER_COIL, TIN_COIL, ALUMINIUM_COIL, SILVER_COIL, IRON_COIL, GOLD_COIL,

			TUNGSTEN_INGOT, OSMIUM_INGOT, TITANIUM_INGOT,

			TUNGSTEN_NUGGET, OSMIUM_NUGGET, TITANIUM_NUGGET,

			// Tools

			HAMMER,

			COPPER_SHOVEL, TIN_SHOVEL, ALUMINIUM_SHOVEL, SILVER_SHOVEL, TUNGSTEN_SHOVEL, OSMIUM_SHOVEL, TITANIUM_SHOVEL,

			COPPER_PICKAXE, TIN_PICKAXE, ALUMINIUM_PICKAXE, SILVER_PICKAXE, TUNGSTEN_PICKAXE, OSMIUM_PICKAXE, TITANIUM_PICKAXE,

			COPPER_AXE, TIN_AXE, ALUMINIUM_AXE, SILVER_AXE, TUNGSTEN_AXE, OSMIUM_AXE, TITANIUM_AXE,

			COPPER_HOE, TIN_HOE, ALUMINIUM_HOE, SILVER_HOE, TUNGSTEN_HOE, OSMIUM_HOE, TITANIUM_HOE,

			COPPER_SWORD, TIN_SWORD, ALUMINIUM_SWORD, SILVER_SWORD, TUNGSTEN_SWORD, OSMIUM_SWORD, TITANIUM_SWORD,

			// Armor

			COPPER_HELMET, TIN_HELMET, ALUMINIUM_HELMET, SILVER_HELMET, TUNGSTEN_HELMET, OSMIUM_HELMET, TITANIUM_HELMET,

			COPPER_CHESTPLATE, TIN_CHESTPLATE, ALUMINIUM_CHESTPLATE, SILVER_CHESTPLATE, TUNGSTEN_CHESTPLATE, OSMIUM_CHESTPLATE, TITANIUM_CHESTPLATE,

			COPPER_LEGGINGS, TIN_LEGGINGS, ALUMINIUM_LEGGINGS, SILVER_LEGGINGS, TUNGSTEN_LEGGINGS, OSMIUM_LEGGINGS, TITANIUM_LEGGINGS,

			COPPER_BOOTS, TIN_BOOTS, ALUMINIUM_BOOTS, SILVER_BOOTS, TUNGSTEN_BOOTS, OSMIUM_BOOTS, TITANIUM_BOOTS,

			RAILGUN,

			// TEST_ENERGY_GUN,

			STANDALONE_GUN,

			GUN_BODY, MISSILE_LAUNCHER,

			//

			IRON_SMALL, OSMIUM_SMALL, TUNGSTEN_SMALL,

			IRON_MEDIUM, OSMIUM_MEDIUM, TUNGSTEN_MEDIUM,

			IRON_LARGE, OSMIUM_LARGE, TUNGSTEN_LARGE,

			//

			PARAMAGNETIC_PROJECILE,

			CRUSHER_BIT, CRUSHER_BIT_HOLDER,

			GALLIUM, GALLIUM_INGOT, GALLIUM_WAFER, ALUMINA, IRON_OXIDE, SILICA, SILICON, SILICON_WAFER, TITANIA,

			FLAMETHROWER, COILGUN, PLASMA_GUN,

			CAPACITOR,

			TEST_LAUNCHER, TEST_MODULAR_WEAPON,

			TASER, PLASMA_TOOL

	};

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
				if (item.getRegistryName().getResourcePath().contains("_nugget") || item.getRegistryName().getResourcePath().contains("_ingot"))
					IngredientItems.add(item);
			}
		return IngredientItems;
	}

}
