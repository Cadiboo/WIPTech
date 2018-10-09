package cadiboo.wiptech.item;

import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCasedSlug extends Item implements IItemModMaterial, IModItem {

	private final ModMaterial material;

	public ItemCasedSlug(final ModMaterial material) {
		final ModResourceLocation registryName = new ModResourceLocation(ModReference.MOD_ID, "cased_" + material.getNameLowercase() + "_slug");
		ModUtil.setRegistryNames(this, registryName);
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
