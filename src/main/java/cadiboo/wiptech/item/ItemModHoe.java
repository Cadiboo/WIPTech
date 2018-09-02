package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;

public class ItemModHoe extends ItemHoe implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemModHoe(final ModMaterials material) {
		super(material.getToolMaterial());
		ModUtil.setRegistryNames(this, material, "hoe");
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
