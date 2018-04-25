package cadiboo.wiptech.item;

import java.util.List;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.util.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBase extends Item {

	public ItemBase(String name) {
		super();
		this.setRegistryName(Reference.ID, name);
		this.setUnlocalizedName(name);
	}

	// Beacon Payment
	private ItemBase beaconPayment;

	public ItemBase setBeaconPayment() {
		return beaconPayment = this;
	}

	@Override
	public boolean isBeaconPayment(ItemStack stack) {
		return this == beaconPayment;
	}

	// Ingot
	private ItemBase isIngot;

	public ItemBase setIngot() {
		return isIngot = this;
	}

	public final boolean isIngot() {
		return this == isIngot;
	}

	// Nugget
	private ItemBase isNugget;

	public ItemBase setNugget() {
		this.setBeaconPayment();
		return isNugget = this;
	}

	public final boolean isNugget() {
		return this == isNugget;
	}

	// Coil
	private Item isCoil;

	public Item setCoil() {
		this.setBeaconPayment();
		return isCoil = this;
	}

	public final boolean isCoil() {
		return this == isCoil;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String itemTooltip = WIPTech.proxy.localize(stack.getUnlocalizedName() + ".tooltip", new Object[0]);
		if (!itemTooltip.equalsIgnoreCase(stack.getUnlocalizedName() + ".tooltip"))
			tooltip.add(itemTooltip);
	}

}