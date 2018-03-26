package cadiboo.wiptech.init;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.block.BlockBase;
import cadiboo.wiptech.item.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Items
{
	public static final ItemCopperIngot COPPER_INGOT = new ItemCopperIngot("copper_ingot");
	public static final ItemCopperNugget COPPER_NUGGET = new ItemCopperNugget("copper_nugget");
	public static final ItemCopperStrand COPPER_STRAND = new ItemCopperStrand("copper_strand");
	public static final ItemCopperCoil COPPER_COIL = new ItemCopperCoil("copper_coil");

	public static final ItemSilverIngot SILVER_INGOT = new ItemSilverIngot("silver_ingot");
	public static final ItemSilverNugget SILVER_NUGGET = new ItemSilverNugget("silver_nugget");
	public static final ItemSilverCoil SILVER_COIL = new ItemSilverCoil("silver_coil");

	public static final ItemTinIngot TIN_INGOT = new ItemTinIngot("tin_ingot");
	public static final ItemTinNugget TIN_NUGGET = new ItemTinNugget("tin_nugget");
	public static final ItemTinCoil TIN_COIL = new ItemTinCoil("tin_coil");

	public static final ItemAluminiumIngot ALUMINIUM_INGOT = new ItemAluminiumIngot("aluminium_ingot");
	public static final ItemAluminiumNugget ALUMINIUM_NUGGET = new ItemAluminiumNugget("aluminium_nugget");
	public static final ItemAluminiumCoil ALUMINIUM_COIL = new ItemAluminiumCoil("aluminium_coil");

	public static final ItemGoldCoil GOLD_COIL = new ItemGoldCoil("gold_coil");
	public static final ItemTungstenIngot TUNGSTEN_INGOT = new ItemTungstenIngot("tungsten_ingot");
	public static final ItemTungstenNugget TUNGSTEN_NUGGET = new ItemTungstenNugget("tungsten_nugget");
	public static final ItemOsmiumIngot OSMIUM_INGOT = new ItemOsmiumIngot("osmium_ingot");
	public static final ItemOsmiumNugget OSMIUM_NUGGET = new ItemOsmiumNugget("osmium_nugget");
	public static final ItemFerromagneticProjectile FERROMAGNETIC_PROJECILE = new ItemFerromagneticProjectile("ferromagnetic_projectile");
	public static final ItemCrusherBit CRUSHER_BIT = new ItemCrusherBit("crusher_bit");
	public static final ItemCrusherBitHolder CRUSHER_BIT_HOLDER = new ItemCrusherBitHolder("crusher_bit_holder");
	public static final ItemGallium GALLIUM = new ItemGallium("gallium");
	public static final ItemGalliumIngot GALLIUM_INGOT = new ItemGalliumIngot("gallium_ingot");
	public static final ItemGalliumWafer GALLIUM_WAFER = new ItemGalliumWafer("gallium_wafer");
	public static final ItemAlumina ALUMINA = new ItemAlumina("alumina");
	public static final ItemIronOxide IRON_OXIDE = new ItemIronOxide("iron_oxide");
	public static final ItemSilica SILICA = new ItemSilica("silica");
	public static final ItemSilicon SILICON = new ItemSilicon("silicon");
	public static final ItemSiliconWafer SILICON_WAFER = new ItemSiliconWafer("silicon_wafer");
	public static final ItemTitania TITANIA = new ItemTitania("titania");
	public static final ItemTitaniumIngot TITANIUM_INGOT = new ItemTitaniumIngot("titanium_ingot");
	public static final ItemTitaniumNugget TITANIUM_NUGGET = new ItemTitaniumNugget("titanium_nugget");
	public static final ItemFlamethrower FLAMETHROWER = new ItemFlamethrower("flamethrower");
	public static final ItemNapalm NAPALM = new ItemNapalm("napalm");
	public static final ItemHammer HAMMER = new ItemHammer("hammer");
	public static final ItemRailgun RAILGUN = new ItemRailgun("railgun");
	public static final ItemCoilgun COILGUN = new ItemCoilgun("coilgun");
	public static final ItemPlasmagun PLASMA_GUN = new ItemPlasmagun("plasmagun");
	
	public static final ItemCapacitor CAPACITOR = new ItemCapacitor("capacitor");

	public static final ItemTestLauncher TEST_LAUNCHER = new ItemTestLauncher("test_launcher");
	public static final ItemTestModularWeapon TEST_MODULAR_WEAPON = new ItemTestModularWeapon("test_modular_weapon");


	public static final Item[] ITEMS = {
			HAMMER, 
			COPPER_INGOT, 
			COPPER_NUGGET, 
			COPPER_STRAND, 
			COPPER_COIL, 
			
			SILVER_INGOT, 
			SILVER_NUGGET, 
			SILVER_COIL, 
			
			TIN_INGOT, 
			TIN_NUGGET, 
			TIN_COIL, 
			
			GOLD_COIL,
			
			ALUMINIUM_INGOT, 
			ALUMINIUM_NUGGET,
			ALUMINIUM_COIL, 
			
			TUNGSTEN_INGOT, 
			TUNGSTEN_NUGGET, 
			OSMIUM_INGOT, 
			OSMIUM_NUGGET, 
			FERROMAGNETIC_PROJECILE, 
			CRUSHER_BIT, 
			CRUSHER_BIT_HOLDER, 
			GALLIUM, 
			GALLIUM_INGOT, 
			GALLIUM_WAFER, 
			ALUMINA, 
			IRON_OXIDE, 
			SILICA, 
			SILICON, 
			SILICON_WAFER, 
			TITANIA, 
			TITANIUM_INGOT,
			TITANIUM_NUGGET,
			FLAMETHROWER, 
			NAPALM,
			RAILGUN,
			COILGUN,
			PLASMA_GUN,
			
			CAPACITOR,

			TEST_LAUNCHER,
			TEST_MODULAR_WEAPON,

	};
	
	
	
	private static List<Item> Ingots = new ArrayList();
	private static Boolean IngotsIterated = false;

	public static List<Item> getIngots()
	{
		if (!IngotsIterated.booleanValue())
		{
			Item[] arrayOfItem;
			int j = (arrayOfItem = ITEMS).length;
			for (int i = 0; i < j; i++)
			{
				Item item = arrayOfItem[i];
				if (!Ingots.contains(item))
				{
					if (((item instanceof ItemBase)) && 
							(((ItemBase)item).isIngot()))
					{
						Ingots.add(item);
					}
				}
				else {
					IngotsIterated = Boolean.valueOf(true);
				}
			}
		}
		return Ingots;
	}
	
	private static List<Item> Nuggets = new ArrayList();
	private static Boolean NuggetsIterated = false;

	public static List<Item> getNuggets()
	{
		if (!NuggetsIterated.booleanValue())
		{
			Item[] arrayOfItem;
			int j = (arrayOfItem = ITEMS).length;
			for (int i = 0; i < j; i++)
			{
				Item item = arrayOfItem[i];
				if (!Nuggets.contains(item))
				{
					if (((item instanceof ItemBase)) && 
							(((ItemBase)item).isNugget()))
					{
						Nuggets.add(item);
					}
				}
				else {
					NuggetsIterated = Boolean.valueOf(true);
				}
			}
		}
		return Nuggets;
	}
	
	
	
}
