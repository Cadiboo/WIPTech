package cadiboo.wiptech.item;

import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCasedSlug extends Item implements IItemModMaterial, IModItem {

	private final ModMaterial material;

	public ItemCasedSlug(final ModMaterial material) {
		ModUtil.setRegistryNames(this, material, "cased_slug");
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
