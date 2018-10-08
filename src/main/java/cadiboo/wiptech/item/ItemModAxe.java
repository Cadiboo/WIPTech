package cadiboo.wiptech.item;

import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;

public class ItemModAxe extends ItemAxe implements IItemModMaterial {

	protected final ModMaterial material;

	public ItemModAxe(final ModMaterial material) {
		super(material.getToolMaterial(), material.getToolMaterial().getAttackDamage(), material.getToolMaterial().getEfficiency());
		ModUtil.setRegistryNames(this, material, "axe");
		this.material = material;
	}

	@Override
	public ModMaterial getModMaterial() {
		return this.material;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
