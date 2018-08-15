package cadiboo.wiptech.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class ModItemStackHandler extends ItemStackHandler {

	public ModItemStackHandler() {
		this(1);
	}

	public ModItemStackHandler(int size) {
		stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	public ModItemStackHandler(NonNullList<ItemStack> stacks) {
		this.stacks = stacks;
	}

}
