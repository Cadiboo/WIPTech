package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.SlugCasingParts;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemSlugCasing extends Item {

	private final SlugCasingParts part;

	public ItemSlugCasing(final String name, final SlugCasingParts part) {
		this.setRegistryName(new ResourceLocation(ModReference.MOD_ID, name));
		this.setUnlocalizedName(name);
		this.part = part;
	}

	public SlugCasingParts getPart() {
		return this.part;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
