package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;

public class ItemModHoe extends ItemHoe implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemModHoe(final ModMaterials materialIn) {
		super(materialIn.getToolMaterial());
		ModUtil.setRegistryNames(this, materialIn, "hoe");
		this.material = materialIn;
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
