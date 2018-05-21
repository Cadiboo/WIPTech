package cadiboo.wiptech.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AssembleRecipe {

	private Item		output;
	private int			time;
	private ItemStack[]	required;
	private ItemStack[]	optional;

	public AssembleRecipe(Item output, int time, ItemStack[] railgun_required, ItemStack[] railgun_optional) {
		this.output = output;
		this.time = time;
		this.required = railgun_required;
		this.optional = railgun_optional;
	}

	public Item getOutput() {
		return output;
	}

	public int getTime() {
		return time;
	}

	public ItemStack[] getRequiredComponents() {
		return required;
	}

	public ItemStack[] getOptionalComponents() {
		return optional;
	}

	public ItemStack[] getAllComponents() {
		ItemStack[] result = new ItemStack[required.length + optional.length];
		for (int i = 0; i < required.length; i++)
			result[i] = required[i];
		for (int i = required.length; i < required.length + optional.length; i++)
			result[i] = optional[i - required.length];
		return result;
	}

}
