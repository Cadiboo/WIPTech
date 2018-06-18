package cadiboo.wiptech.recipes;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class CrushRecipe extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	/** Is the ItemStack that composes the recipe. */
	private final Ingredient			recipeInput;
	/** Is a List of ItemStack that you get when you craft the recipe. */
	public final NonNullList<ItemStack>	recipeOutputs;
	private final String				group;
	private final boolean				isSimple;

	public CrushRecipe(String group, Ingredient input, NonNullList<ItemStack> outputs) {
		this.group = group;
		this.recipeInput = input;
		this.recipeOutputs = outputs;
		this.isSimple = input.isSimple();
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.<Ingredient>withSize(1, Ingredient.EMPTY);
	}

	public NonNullList<ItemStack> getRecipeOutputs() {
		return this.recipeOutputs;
	}

	public Ingredient getIngredient() {
		return this.recipeInput;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);

			nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
		}

		return nonnulllist;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int ingredientCount = 0;
		net.minecraft.client.util.RecipeItemHelper recipeItemHelper = new net.minecraft.client.util.RecipeItemHelper();
		List<ItemStack> inputs = Lists.newArrayList();

		for (int i = 0; i < inv.getHeight(); ++i) {
			for (int j = 0; j < inv.getWidth(); ++j) {
				ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

				if (!itemstack.isEmpty()) {
					++ingredientCount;
					if (this.isSimple)
						recipeItemHelper.accountStack(itemstack, 1);
					else
						inputs.add(itemstack);
				}
			}
		}

		if (ingredientCount != 1)
			return false;

		if (this.isSimple)
			return recipeItemHelper.canCraft(this, null);
		List tests = new ArrayList<Ingredient>();
		tests.add(recipeInput);

		return net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, tests) != null;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return ItemStack.EMPTY;
	}

	public NonNullList<ItemStack> getCraftingResults(InventoryCrafting inv) {
		return this.recipeOutputs;
	}

	private static NonNullList<ItemStack> deserializeStacks(JsonArray array) {
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>create();

		for (int i = 0; i < array.size(); ++i) {
			ItemStack stack = ShapedRecipes.deserializeItem((JsonObject) array.get(i), true);

			if (!stack.isEmpty()) {
				nonnulllist.add(stack);
			}
		}

		return nonnulllist;
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 1;
	}

	/**
	 * Example JSON recipe:<br>
	 * {<br>
	 * &emsp;"type": "wiptech:crushing",<br>
	 * &emsp;"ingredient": {<br>
	 * &emsp;&emsp;"item": "minecraft:sand",<br>
	 * &emsp;&emsp;"data": 0,<br>
	 * &emsp;&emsp;"count": 1<br>
	 * &emsp;},<br>
	 * &emsp;"results": [<br>
	 * &emsp;&emsp;&emsp;{<br>
	 * &emsp;&emsp;&emsp;"item": "wiptech:silicon",<br>
	 * &emsp;&emsp;&emsp;"data": 0,<br>
	 * &emsp;&emsp;&emsp;"count": 1<br>
	 * &emsp;&emsp;}<br>
	 * &emsp;],<br>
	 * &emsp;"experience": 0.1,<br>
	 * &emsp;"duration": 200<br>
	 * }
	 * 
	 * @author Cadiboo
	 *
	 */

	public static class Factory implements IRecipeFactory {
		@Override
		public IRecipe parse(JsonContext context, JsonObject json) {
			String domain = null;
			if (JsonUtils.hasField(json, "domain")) {
				domain = JsonUtils.getString(json, "domain", "");
			}
			final Ingredient ingredientInput = CraftingHelper.getIngredient(JsonUtils.getJsonObject(json, "ingredient"), context);
			// final JsonObject ingredient = JsonUtils.getJsonObject(json, "ingredients");
			final NonNullList<ItemStack> itemstackOutputs = deserializeStacks(json.getAsJsonArray("results"));

			return new CrushRecipe(JsonUtils.getString(json, "group", "wiptech:crushing"), ingredientInput, itemstackOutputs);
		}
	}

}