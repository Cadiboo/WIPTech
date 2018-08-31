package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCasedSlug extends Item implements IItemModMaterial {

	private final ModMaterials material;

	public ItemCasedSlug(final ModMaterials materialIn) {
		final ModResourceLocation registryName = new ModResourceLocation(ModReference.MOD_ID, "cased_" + materialIn.getNameLowercase() + "_slug");
		ModUtil.setRegistryNames(this, registryName);
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
