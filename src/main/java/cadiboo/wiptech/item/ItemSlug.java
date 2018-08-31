package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSlug extends Item implements IItemModMaterial {
	protected final ModMaterials material;

	public ItemSlug(final ModMaterials materialIn) {
		ModUtil.setRegistryNames(this, materialIn, "slug");
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
