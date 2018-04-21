package cadiboo.wiptech.init;

import java.util.ArrayList;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.tileentity.TileEntityCoiler;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Recipes {
	private static ArrayList<ArrayList> crushRecipes = new ArrayList();
	private static ArrayList<ArrayList> hammerRecipes = new ArrayList();
	private static ArrayList<ArrayList> coilRecipes = new ArrayList();

	private static final Block AIR = net.minecraft.init.Blocks.AIR;

	public static void registerRecipes() {
		addCrushRecipes();
		addHammerRecipes();
		addCoilRecipes();
		WIPTech.logger.info("Registered Processing Recipes");

		GameRegistry.addSmelting(Blocks.COPPER_ORE, new ItemStack(Items.COPPER_INGOT, 1), 0.0F);
		GameRegistry.addSmelting(Blocks.TIN_ORE, new ItemStack(Items.TIN_INGOT, 1), 0.0F);
		GameRegistry.addSmelting(Blocks.ALUMINIUM_ORE, new ItemStack(Items.ALUMINIUM_INGOT, 1), 0.0F);
		GameRegistry.addSmelting(Blocks.SILVER_ORE, new ItemStack(Items.SILVER_INGOT, 1), 0.0F);
		GameRegistry.addSmelting(Blocks.TUNGSTEN_ORE, new ItemStack(Items.TUNGSTEN_INGOT, 1), 0.0F);
		GameRegistry.addSmelting(Blocks.OSMIUM_ORE, new ItemStack(Items.OSMIUM_INGOT, 1), 0.0F);
		WIPTech.logger.info("Registered Ore Smelting");

		GameRegistry.addSmelting(Items.ALUMINA, new ItemStack(Items.ALUMINIUM_INGOT, 2), 0.0F);
		GameRegistry.addSmelting(Items.GALLIUM, new ItemStack(Items.GALLIUM_INGOT, 2), 0.0F);
		GameRegistry.addSmelting(Items.IRON_OXIDE, new ItemStack(net.minecraft.init.Items.IRON_INGOT, 2), 0.0F);
		GameRegistry.addSmelting(Items.SILICA, new ItemStack(Items.SILICON, 2), 0.0F);
		GameRegistry.addSmelting(Items.TITANIA, new ItemStack(Items.TITANIUM_INGOT, 2), 0.0F);
		WIPTech.logger.info("Registered Item Smelting");
	}

	private static void addHammerRecipes() {

		addHammerRecipe(20, new ItemStack(Items.COPPER_NUGGET), new ItemStack(Blocks.COPPER_WIRE));
		addHammerRecipe(20, new ItemStack(Items.COPPER_INGOT), new ItemStack(Blocks.COPPER_RAIL));

		addHammerRecipe(20, new ItemStack(Items.GOLD_NUGGET), new ItemStack(Blocks.GOLD_WIRE));
		addHammerRecipe(20, new ItemStack(Items.GOLD_INGOT), new ItemStack(Blocks.GOLD_RAIL));

		addHammerRecipe(20, new ItemStack(Items.IRON_INGOT), new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, 0));
		addHammerRecipe(20, new ItemStack(Items.OSMIUM_INGOT), new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, 1));
		addHammerRecipe(20, new ItemStack(Items.TUNGSTEN_INGOT), new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, 2));
		for (int i = 0; i < 5 + 1; i++) {
			addHammerRecipe(20, new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, i),
					new ItemStack(Items.PARAMAGNETIC_PROJECILE, 3, i + 3));
		}
	}

	private static void addCoilRecipes() {
		addCoilRecipe(20, new ItemStack(Blocks.GOLD_ENAMEL, 16), new ItemStack(Blocks.GOLD_SPOOL, 1));

		addCoilRecipe(20, new ItemStack(Blocks.GOLD_SPOOL), new ItemStack(Items.GOLD_COIL, 4));

		addCoilRecipe(20, new ItemStack(Blocks.COPPER_ENAMEL, 16), new ItemStack(Blocks.COPPER_SPOOL, 1));

		addCoilRecipe(20, new ItemStack(Blocks.COPPER_SPOOL), new ItemStack(Items.COPPER_COIL, 4));
	}

	private static void addCrushRecipes() {
		addCrushRecipe(100, new ItemStack(Blocks.BAUXITE_ORE), new ItemStack(Items.ALUMINA),
				new ItemStack(Items.GALLIUM), new ItemStack(Items.IRON_OXIDE), new ItemStack(Items.SILICA),
				new ItemStack(Items.TITANIA), new ItemStack(net.minecraft.init.Blocks.GRAVEL));

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
