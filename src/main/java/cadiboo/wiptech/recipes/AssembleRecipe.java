package cadiboo.wiptech.recipes;

import net.minecraft.item.Item;

public class AssembleRecipe {

	private Item	output;
	private int		time;
	private Class[]	required;
	private Class[]	optional;

	public AssembleRecipe(Item output, int time, Class[] required, Class[] optional) {
		this.output = output;
		this.time = time;
		this.required = required;
		this.optional = optional;
	}

	public Item getOutput() {
		return output;
	}

	public int getTime() {
		return time;
	}

	public Class[] getRequiredModules() {
		return required;
	}

	public Class[] getOptionalModules() {
		return optional;
	}

	public Class[] getAllModules() {
		Class[] result = new Class[required.length + optional.length];
		for (int i = 0; i < required.length; i++)
			result[i] = required[i];
		for (int i = required.length; i < required.length + optional.length; i++)
			result[i] = optional[i - required.length];
		return result;
	}

}
