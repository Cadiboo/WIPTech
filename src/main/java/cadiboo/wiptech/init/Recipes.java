package cadiboo.wiptech.init;

import java.util.ArrayList;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.tileentity.TileEntityCoiler;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class Recipes
{
	private static ArrayList<ArrayList> crushRecipes = new ArrayList();
	private static ArrayList<ArrayList> hammerRecipes = new ArrayList();
	private static ArrayList<ArrayList> coilRecipes = new ArrayList();

	public static void createRecipes()
	{
		addCrushRecipe(
				new ItemStack(Blocks.BAUXITE_ORE), 
				new ItemStack(Items.ALUMINA), 
				new ItemStack(Items.GALLIUM), 
				new ItemStack(Items.IRON_OXIDE), 
				new ItemStack(Items.SILICA), 
				new ItemStack(Items.TITANIA), 
				new ItemStack(net.minecraft.init.Blocks.GRAVEL), 
				100);

		addCrushRecipe(
				new ItemStack(net.minecraft.init.Blocks.SAND), 
				new ItemStack(Items.SILICON), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);

		addHammerRecipe(
				new ItemStack(Items.COPPER_INGOT), 
				new ItemStack(Blocks.COPPER_RAIL), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);

		addHammerRecipe(
				new ItemStack(Items.COPPER_NUGGET), 
				new ItemStack(Blocks.COPPER_WIRE), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);

		addHammerRecipe(
				new ItemStack((Item)Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_ingot"))), 
				new ItemStack(Blocks.GOLD_RAIL), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);

		addHammerRecipe(
				new ItemStack((Item)Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_nugget"))), 
				new ItemStack(Blocks.GOLD_WIRE), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);

		addHammerRecipe(
				new ItemStack((Item)Item.REGISTRY.getObject(new ResourceLocation("minecraft", "iron_ingot"))), 
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 0), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);
		addHammerRecipe(
				new ItemStack(Items.OSMIUM_INGOT), 
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 1), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);
		addHammerRecipe(
				new ItemStack(Items.TUNGSTEN_INGOT), 
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 2), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);
		addHammerRecipe(
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 0),
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 3, 3), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);
		addHammerRecipe(
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 1),
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 3, 4), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);
		addHammerRecipe(
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 2),
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 3, 5), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);
		addHammerRecipe(
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 3),
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 3, 6), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);
		addHammerRecipe(
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 4),
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 3, 7), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);
		addHammerRecipe(
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, 5),
				new ItemStack(Items.FERROMAGNETIC_PROJECILE, 3, 8), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				new ItemStack(net.minecraft.init.Blocks.AIR), 
				20);

		addCoilRecipe(
				new ItemStack(Blocks.GOLD_ENAMEL, 16), 
				new ItemStack(Blocks.GOLD_SPOOL, 1), 
				20);

		addCoilRecipe(
				new ItemStack(Blocks.GOLD_SPOOL), 
				new ItemStack(Items.GOLD_COIL, 4), 
				20);

		addCoilRecipe(
				new ItemStack(Blocks.COPPER_ENAMEL, 16), 
				new ItemStack(Blocks.COPPER_SPOOL, 1), 
				20);

		addCoilRecipe(
				new ItemStack(Blocks.COPPER_SPOOL), 
				new ItemStack(Items.COPPER_COIL, 4), 
				20);
	}

	public static ArrayList getCrushResult(ItemStack input)
	{
		for (ArrayList recipe : crushRecipes) {
			ItemStack test = ((ItemStack)recipe.get(0)).copy();
			test.setCount(input.getCount());
			if(ItemStack.areItemsEqual(test, input)) {
				return recipe;
			}
		}
		return null;
	}

	public static ArrayList getHammerResult(ItemStack input)
	{
		//if(input != ItemStack.EMPTY) {
		for (ArrayList recipe : hammerRecipes) {
			ItemStack test = ((ItemStack)recipe.get(0)).copy();
			test.setCount(input.getCount());
			if(ItemStack.areItemsEqual(test, input)) {
				/*WIPTech.logger.info("TEST==INPUT");
					WIPTech.logger.info(test);
					WIPTech.logger.info(input);
				 */
				return recipe;
			}
			else
			{
				/*WIPTech.logger.info("TEST!=INPUT");
					WIPTech.logger.info(test);
					WIPTech.logger.info(input);*/
			}
		}
		//}
		return null;
	}

	public static ArrayList getCoilResult(ItemStack input)
	{
		for (ArrayList recipe : crushRecipes) {
			ItemStack test = ((ItemStack)recipe.get(0)).copy();
			test.setCount(input.getCount());
			if(ItemStack.areItemsEqual(test, input)) {
				return recipe;
			}
		}
		return null;
	}

	public static ArrayList<ItemStack> getCrushResults(int slot)
	{
		if ((slot > TileEntityCrusher.getSlots()-1) || (slot < 0)) {
			return null;
		}
		ArrayList<ItemStack> crushResults = new ArrayList();
		for (ArrayList recipe : crushRecipes) {
			crushResults.add((ItemStack)recipe.get(slot));
		}
		return crushResults;
	}

	public static ArrayList<ItemStack> getHammerResults(int slot)
	{
		if ((slot > TileEntityCrusher.getSlots()-1) || (slot < 0)) {
			return null;
		}
		ArrayList<ItemStack> hammerResults = new ArrayList();
		for (ArrayList recipe : hammerRecipes) {
			hammerResults.add((ItemStack)recipe.get(slot));
		}
		return hammerResults;
	}

	public static ArrayList<ItemStack> getCoilResult(int slot)
	{
		if ((slot > TileEntityCoiler.getSlots()-1) || (slot < 0)) {
			return null;
		}
		ArrayList<ItemStack> coilResults = new ArrayList();
		for (ArrayList recipe : coilRecipes) {
			coilResults.add((ItemStack)recipe.get(slot));
		}
		return coilResults;
	}

	public static void addCrushRecipe(ItemStack input, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4, ItemStack output5, ItemStack output6, int time)
	{
		ArrayList recipe = new ArrayList();
		recipe.add(input);
		recipe.add(output1);
		recipe.add(output2);
		recipe.add(output3);
		recipe.add(output4);
		recipe.add(output5);
		recipe.add(output6);
		recipe.add(Integer.valueOf(time));
		crushRecipes.add(recipe);
	}

	public static void addHammerRecipe(ItemStack input, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4, ItemStack output5, ItemStack output6, int time)
	{
		ArrayList recipe = new ArrayList();
		recipe.add(input);
		recipe.add(output1);
		recipe.add(output2);
		recipe.add(output3);
		recipe.add(output4);
		recipe.add(output5);
		recipe.add(output6);
		recipe.add(Integer.valueOf(time));
		hammerRecipes.add(recipe);
	}

	public static void addCoilRecipe(ItemStack input, ItemStack output, int time)
	{
		ArrayList recipe = new ArrayList();
		recipe.add(input);
		recipe.add(output);
		recipe.add(Integer.valueOf(time));
		coilRecipes.add(recipe);
	}
}
