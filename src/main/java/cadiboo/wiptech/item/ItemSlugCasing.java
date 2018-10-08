package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.SlugCasingPart;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemSlugCasing extends Item implements IModItem {

	private final SlugCasingPart part;

	public ItemSlugCasing(final String name, final SlugCasingPart part) {
		this.setRegistryName(new ResourceLocation(ModReference.MOD_ID, name));
		this.setUnlocalizedName(name);
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
