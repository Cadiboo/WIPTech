package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.SlugCasingPart;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSlugCasing extends Item implements IModItem {

	private final SlugCasingPart part;

	public ItemSlugCasing(final String name, final SlugCasingPart part) {
		ModUtil.setRegistryNames(this, name);
		this.part = part;
	}

	public SlugCasingPart getPart() {
		return this.part;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
