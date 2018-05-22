package cadiboo.wiptech.init;

import java.util.ArrayList;

import com.google.common.annotations.VisibleForTesting;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Capacitors;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Rails;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Scopes;
import cadiboo.wiptech.recipes.AssembleRecipe;
import cadiboo.wiptech.tileentity.TileEntityCoiler;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Recipes {
	private static final ArrayList<ArrayList>		crushRecipes	= new ArrayList<ArrayList>();
	private static final ArrayList<ArrayList>		hammerRecipes	= new ArrayList<ArrayList>();
	private static final ArrayList<ArrayList>		coilRecipes		= new ArrayList<ArrayList>();
	private static final ArrayList<AssembleRecipe>	assembleRecipes	= new ArrayList<AssembleRecipe>();

	private static final Block AIR = net.minecraft.init.Blocks.AIR;

	public static void registerRecipes() {
		addCrushRecipes();
		addHammerRecipes();
		addCoilRecipes();
		addAssembleRecipes();
		WIPTech.logger.info("Registered Processing Recipes");

		GameRegistry.addSmelting(Blocks.COPPER_ORE, new ItemStack(Blocks.COPPER_INGOT, 1), 0.0F);
		GameRegistry.addSmelting(Blocks.TIN_ORE, new ItemStack(Blocks.TIN_INGOT, 1), 0.0F);
		GameRegistry.addSmelting(Blocks.ALUMINIUM_ORE, new ItemStack(Blocks.ALUMINIUM_INGOT, 1), 0.0F);
		GameRegistry.addSmelting(Blocks.SILVER_ORE, new ItemStack(Blocks.SILVER_INGOT, 1), 0.0F);
		GameRegistry.addSmelting(Blocks.TUNGSTEN_ORE, new ItemStack(Items.TUNGSTEN_INGOT, 1), 0.0F);
		GameRegistry.addSmelting(Blocks.OSMIUM_ORE, new ItemStack(Items.OSMIUM_INGOT, 1), 0.0F);
		WIPTech.logger.info("Registered Ore Smelting");

		GameRegistry.addSmelting(Items.ALUMINA, new ItemStack(Blocks.ALUMINIUM_INGOT, 2), 0.0F);
		GameRegistry.addSmelting(Items.GALLIUM, new ItemStack(Items.GALLIUM_INGOT, 2), 0.0F);
		GameRegistry.addSmelting(Items.IRON_OXIDE, new ItemStack(Items.IRON_INGOT), 0.0F);
		GameRegistry.addSmelting(Items.SILICA, new ItemStack(Items.SILICON, 2), 0.0F);
		GameRegistry.addSmelting(Items.TITANIA, new ItemStack(Items.TITANIUM_INGOT, 2), 0.0F);
		WIPTech.logger.info("Registered Item Smelting");
	}

	@VisibleForTesting
	public static void addAssembleRecipes() {

		// for (int rail = 0; rail < Rails.values().length; rail++)
		// for (int capacitor = 0; capacitor < Capacitors.values().length; capacitor++)
		// for (int circuit = 0; circuit < Circuits.values().length; circuit++)
		// for (int scope = 0; scope < Scopes.values().length; scope++) {
		// ItemStack[] railgun_required = {
		//
		// new ItemStack(getItem(Rails.byID(rail).getName(), "_rail")),
		//
		// new ItemStack(getItem(Capacitors.byID(capacitor).getName(), "_capacitor")),
		//
		// new ItemStack(getItem(Circuits.byID(circuit).getName(), "_circuit")),
		//
		// };
		// ItemStack[] railgun_optional = { new
		// ItemStack(getItem(Scopes.byID(scope).getName(), "_scope")), };
		//
		// AssembleRecipe recipe = new AssembleRecipe(Items.RAILGUN, 10,
		// railgun_required, railgun_optional);
		// assembleRecipes.add(recipe);
		//
		// // if (Reference.DEBUG_ENABLED)
		// // WIPTech.info("[" + rail + capacitor + circuit + scope + "] Added Assemble
		// // Recipe: " + recipe);
		// }

		Class[] railgun_required = { Rails.class, Capacitors.class, Circuits.class };
		Class[] railgun_optional = { Scopes.class, };

		AssembleRecipe recipe = new AssembleRecipe(Items.RAILGUN, 10, railgun_required, railgun_optional);
		assembleRecipes.add(recipe);

		// Class[] coilgun_required = { Coils.class, Capacitors.class, Circuits.class };
		// Class[] coilgun_optional = { Scopes.class };
		//
		// assembleRecipes.add(new AssembleRecipe(Items.COILGUN, 10, coilgun_required,
		// coilgun_optional));
		//
		// Class[] plasmagun_required = { Coils.class, Rails.class, Capacitors.class,
		// Circuits.class };
		// Class[] plasmagun_optional = { Scopes.class };
		//
		// assembleRecipes.add(new AssembleRecipe(Items.PLASMA_GUN, 10,
		// plasmagun_required, plasmagun_optional));
		//
		// Class[] plasmatool_required = {};
		// Class[] plasmatool_optional = {};
		//
		// assembleRecipes.add(new AssembleRecipe(Items.PLASMA_TOOL, 10,
		// plasmatool_required, plasmatool_optional));
		//
		// Class[] taser_required = {};
		// Class[] taser_optional = {};
		//
		// assembleRecipes.add(new AssembleRecipe(Items.TASER, 10, taser_required,
		// taser_optional));

	}

	public static AssembleRecipe getAssembleRecipeFor(Item item) {
		for (AssembleRecipe recipe : assembleRecipes) {
			if (recipe.getOutput() == item)
				return recipe;
		}
		return null;
	}

	private static void addHammerRecipes() {

		// addHammerRecipe(20, new ItemStack(Blocks.COPPER_INGOT), new
		// ItemStack(Items.COPPER_RAIL));
		// addHammerRecipe(20, new ItemStack(Blocks.TIN_INGOT), new
		// ItemStack(Items.TIN_RAIL));
		// addHammerRecipe(20, new ItemStack(Blocks.ALUMINIUM_INGOT), new
		// ItemStack(Items.ALUMINIUM_RAIL));
		// addHammerRecipe(20, new ItemStack(Blocks.SILVER_INGOT), new
		// ItemStack(Items.SILVER_RAIL));
		// addHammerRecipe(20, new ItemStack(Blocks.IRON_INGOT), new
		// ItemStack(Items.IRON_RAIL));
		// addHammerRecipe(20, new ItemStack(Blocks.GOLD_INGOT), new
		// ItemStack(Items.GOLD_RAIL));

		addHammerRecipe(20, new ItemStack(Blocks.COPPER_NUGGET), new ItemStack(Blocks.COPPER_WIRE));
		addHammerRecipe(20, new ItemStack(Blocks.TIN_NUGGET), new ItemStack(Blocks.TIN_WIRE));
		addHammerRecipe(20, new ItemStack(Blocks.ALUMINIUM_NUGGET), new ItemStack(Blocks.ALUMINIUM_WIRE));
		addHammerRecipe(20, new ItemStack(Blocks.SILVER_NUGGET), new ItemStack(Blocks.SILVER_WIRE));
		addHammerRecipe(20, new ItemStack(Blocks.IRON_NUGGET), new ItemStack(Blocks.IRON_WIRE));
		addHammerRecipe(20, new ItemStack(Blocks.GOLD_NUGGET), new ItemStack(Blocks.GOLD_WIRE));

		addHammerRecipe(20, new ItemStack(Items.IRON_INGOT), new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, 0));
		addHammerRecipe(20, new ItemStack(Items.OSMIUM_INGOT), new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, 1));
		addHammerRecipe(20, new ItemStack(Items.TUNGSTEN_INGOT), new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, 2));
		for (int i = 0; i < 5 + 1; i++) {
			addHammerRecipe(20, new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, i), new ItemStack(Items.PARAMAGNETIC_PROJECILE, 3, i + 3));
		}
	}

	private static void addCoilRecipes() {
		// addCoilRecipe(20, new ItemStack(Blocks.GOLD_ENAMEL, 16), new
		// ItemStack(Blocks.GOLD_SPOOL, 1));
		//
		// addCoilRecipe(20, new ItemStack(Blocks.GOLD_SPOOL), new
		// ItemStack(Items.GOLD_COIL, 4));
		//
		// addCoilRecipe(20, new ItemStack(Blocks.COPPER_ENAMEL, 16), new
		// ItemStack(Blocks.COPPER_SPOOL, 1));
		//
		// addCoilRecipe(20, new ItemStack(Blocks.COPPER_SPOOL), new
		// ItemStack(Items.COPPER_COIL, 4));
	}

	private static void addCrushRecipes() {
		addCrushRecipe(100, new ItemStack(Blocks.BAUXITE_ORE), new ItemStack(Items.ALUMINA), new ItemStack(Items.GALLIUM), new ItemStack(Items.IRON_OXIDE), new ItemStack(Items.SILICA), new ItemStack(Items.TITANIA),
				new ItemStack(net.minecraft.init.Blocks.GRAVEL));

		addCrushRecipe(20, new ItemStack(net.minecraft.init.Blocks.SAND), new ItemStack(Items.SILICON));
	}

	public static ArrayList getCrushResult(ItemStack input) {
		for (int i = 0; i < crushRecipes.size(); i++) {
			if (ItemStack.areItemsEqualIgnoreDurability((ItemStack) crushRecipes.get(i).get(0), input)) {
				return crushRecipes.get(i);
			}
		}
		return null;
	}

	public static ArrayList getHammerResult(ItemStack input) {
		for (int i = 0; i < hammerRecipes.size(); i++) {
			if (ItemStack.areItemsEqualIgnoreDurability((ItemStack) hammerRecipes.get(i).get(0), input)) {
				return hammerRecipes.get(i);
			}
		}
		return null;
	}

	public static ArrayList getCoilResult(ItemStack input) {
		for (int i = 0; i < coilRecipes.size(); i++) {
			if (ItemStack.areItemsEqualIgnoreDurability((ItemStack) coilRecipes.get(i).get(0), input)) {
				return coilRecipes.get(i);
			}
		}
		return null;
	}

	public static ArrayList<ItemStack> getCrushResults(int slot) {
		if ((slot > TileEntityCrusher.getSlots() - 1) || (slot < 0)) {
			return null;
		}
		ArrayList<ItemStack> crushResults = new ArrayList();
		for (ArrayList recipe : crushRecipes) {
			crushResults.add((ItemStack) recipe.get(slot));
		}
		return crushResults;
	}

	public static ArrayList<ItemStack> getHammerResults(int slot) {
		if ((slot > TileEntityCrusher.getSlots() - 1) || (slot < 0)) {
			return null;
		}
		ArrayList<ItemStack> hammerResults = new ArrayList();
		for (ArrayList recipe : hammerRecipes) {
			hammerResults.add((ItemStack) recipe.get(slot));
		}
		return hammerResults;
	}

	public static ArrayList<ItemStack> getCoilResult(int slot) {
		if ((slot > TileEntityCoiler.getSlots() - 1) || (slot < 0)) {
			return null;
		}
		ArrayList<ItemStack> coilResults = new ArrayList();
		for (ArrayList recipe : coilRecipes) {
			coilResults.add((ItemStack) recipe.get(slot));
		}
		return coilResults;
	}

	public static void addCrushRecipe(int time, ItemStack input, ItemStack... outputs) {
		ArrayList recipe = new ArrayList();
		recipe.add(input);
		recipe.add(time);
		for (int i = 0; i < outputs.length; i++)
			recipe.add(outputs[i]);
		crushRecipes.add(recipe);
	}

	public static void addHammerRecipe(int time, ItemStack input, ItemStack... outputs) {
		ArrayList recipe = new ArrayList();
		recipe.add(input);
		recipe.add(time);
		for (int i = 0; i < outputs.length; i++)
			recipe.add(outputs[i]);
		hammerRecipes.add(recipe);
	}

	public static void addCoilRecipe(int time, ItemStack input, ItemStack... outputs) {
		ArrayList recipe = new ArrayList();
		recipe.add(input);
		recipe.add(time);
		for (int i = 0; i < outputs.length; i++)
			recipe.add(outputs[i]);
		coilRecipes.add(recipe);
	}

}
