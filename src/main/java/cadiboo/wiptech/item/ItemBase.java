package cadiboo.wiptech.item;

import cadiboo.wiptech.util.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
}