package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;

public class ItemModAxe extends ItemAxe implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemModAxe(final ModMaterials material) {
		super(material.getToolMaterial(), material.getToolMaterial().getAttackDamage(), material.getToolMaterial().getEfficiency());
		ModUtil.setRegistryNames(this, material, "axe");
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

}
