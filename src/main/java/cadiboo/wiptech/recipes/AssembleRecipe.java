package cadiboo.wiptech.recipes;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class AssembleRecipe extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	private final ItemStack					recipeOutput;
	public final NonNullList<Ingredient>	recipeItems;
	private final String					group;
	private final boolean					isSimple;

	public AssembleRecipe(String group, ItemStack output, NonNullList<Ingredient> ingredients) {
		this.group = group;
		this.recipeOutput = output;
		this.recipeItems = ingredients;
		boolean simple = true;
		for (Ingredient i : ingredients)
			simple &= i.isSimple();
		this.isSimple = simple;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.recipeOutput;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.recipeItems;
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

		if (ingredientCount != this.recipeItems.size())
			return false;

		if (this.isSimple)
			return recipeItemHelper.canCraft(this, null);

		return net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.recipeItems) != null;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return this.recipeOutput.copy();
	}

	public static AssembleRecipe deserialize(JsonObject json) {
		String s = JsonUtils.getString(json, "group", "");
		NonNullList<Ingredient> nonnulllist = deserializeIngredients(JsonUtils.getJsonArray(json, "ingredients"));

		if (nonnulllist.isEmpty()) {
			throw new JsonParseException("No ingredients for shapeless recipe");
		} else if (nonnulllist.size() > 9) {
			throw new JsonParseException("Too many ingredients for shapeless recipe");
		} else {
			ItemStack itemstack = ShapedRecipes.deserializeItem(JsonUtils.getJsonObject(json, "result"), true);
			return new AssembleRecipe(s, itemstack, nonnulllist);
		}
	}

	private static NonNullList<Ingredient> deserializeIngredients(JsonArray array) {
		NonNullList<Ingredient> nonnulllist = NonNullList.<Ingredient>create();

		for (int i = 0; i < array.size(); ++i) {
			Ingredient ingredient = ShapedRecipes.deserializeIngredient(array.get(i));

			if (ingredient != Ingredient.EMPTY) {
				nonnulllist.add(ingredient);
			}
		}

		return nonnulllist;
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	@Override
	public boolean canFit(int width, int height) {
		return width * height >= this.recipeItems.size();
	}
}