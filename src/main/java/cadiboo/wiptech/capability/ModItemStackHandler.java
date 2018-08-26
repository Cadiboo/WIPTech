package cadiboo.wiptech.capability;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
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

	public void dropItems(World world, double x, double y, double z) {
		for (int i = 0; i < stacks.size(); i++) {
			if (stacks.get(i).isEmpty())
				return;

			EntityItem entityitem = new EntityItem(world, x, y, z, stacks.get(i));
			entityitem.setDefaultPickupDelay();

			stacks.set(i, ItemStack.EMPTY);
		}
	}

	public NonNullList<ItemStack> getStacks() {
		return stacks;
	}

}
