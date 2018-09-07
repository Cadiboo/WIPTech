package cadiboo.wiptech.capability.inventory;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class ModItemStackHandler extends ItemStackHandler {

	public ModItemStackHandler() {
		this(1);
	}

	public ModItemStackHandler(final int size) {
		super(size);
	}

	public ModItemStackHandler(final NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	public void dropItems(final World world, final double x, final double y, final double z) {
		for (int i = 0; i < this.stacks.size(); i++) {
			if (this.stacks.get(i).isEmpty()) {
				continue;
			}

			final EntityItem entityitem = new EntityItem(world, x, y, z, this.stacks.get(i));
			entityitem.setDefaultPickupDelay();
			world.spawnEntity(entityitem);

			this.stacks.set(i, ItemStack.EMPTY);
		}
	}

	public NonNullList<ItemStack> getStacks() {
		return this.stacks;
	}

}
