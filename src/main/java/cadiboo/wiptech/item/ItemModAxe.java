package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;

public class ItemModAxe extends ItemAxe {

	public ItemModAxe(ModMaterials materialIn) {

		super(materialIn.getToolMaterial(), materialIn.getToolMaterial().getAttackDamage(), materialIn.getToolMaterial().getEfficiency());
		ModUtil.setNameForMaterialItem(this, materialIn, "axe");
		ModUtil.setCreativeTab(this);

	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
