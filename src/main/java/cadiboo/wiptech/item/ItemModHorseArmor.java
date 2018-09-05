package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemModHorseArmor extends Item implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemModHorseArmor(final ModMaterials material) {
		super();
		this.setMaxStackSize(1);
		ModUtil.setRegistryNames(this, material, "horse_armor");
		this.material = material;
	}

	@Override
	public ModMaterials getModMaterial() {
		return this.material;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

	@Override
	public HorseArmorType getHorseArmorType(final ItemStack stack) {
		return this.getModMaterial().getHorseArmorType();
	}

}
