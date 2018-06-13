package cadiboo.wiptech.recipes;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
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
	public final NonNullList<Ingredient>	requiredRecipeItems;
	public final NonNullList<Ingredient>	optionalRecipeItems;
	public final NonNullList<Ingredient>	allRecipeItems;
	private final String					group;
	private final boolean					isSimple;

	public AssembleRecipe(String group, ItemStack output, NonNullList<Ingredient> requiredIngredients, NonNullList<Ingredient> optionalIngredients) {
		this.group = group;
		this.recipeOutput = output;
		this.requiredRecipeItems = requiredIngredients;
		this.optionalRecipeItems = optionalIngredients;

		NonNullList<Ingredient> allIngredients = requiredIngredients;
		allIngredients.addAll(optionalIngredients);

		this.allRecipeItems = allIngredients;

		boolean simple = true;
		for (Ingredient i : requiredIngredients)
			simple &= i.isSimple();
		for (Ingredient i : optionalIngredients)
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
		return this.allRecipeItems;
	}

	public NonNullList<Ingredient> getRequiredIngredients() {
		return this.requiredRecipeItems;
	}

	public NonNullList<Ingredient> getOptionalIngredients() {
		return this.optionalRecipeItems;
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

		if (ingredientCount < this.requiredRecipeItems.size())
			return false;

		if (this.isSimple)
			return recipeItemHelper.canCraft(this, null);

		return net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.allRecipeItems) != null;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return this.recipeOutput.copy();
	}

	public static AssembleRecipe deserialize(JsonObject json) {
		String group = JsonUtils.getString(json, "group", "");
		NonNullList<Ingredient> required = deserializeIngredients(JsonUtils.getJsonArray(json, "required_ingredients"));
		NonNullList<Ingredient> optional = deserializeIngredients(JsonUtils.getJsonArray(json, "optional_ingredients"));

		if (required.isEmpty() && optional.isEmpty()) {
			throw new JsonParseException("No ingredients for assemble recipe");
		} else if (required.size() > TileEntityAssemblyTable.SLOTS.length) {
			throw new JsonParseException("Too many ingredients for assemble recipe");
		} else {
			ItemStack itemstack = ShapedRecipes.deserializeItem(JsonUtils.getJsonObject(json, "result"), true);
			return new AssembleRecipe(group, itemstack, required, optional);
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
		return width * height >= this.requiredRecipeItems.size();
	}
}