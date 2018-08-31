package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.SlugCasingParts;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemSlugCasing extends Item {

	private SlugCasingParts part;

	public ItemSlugCasing(String name, SlugCasingParts part) {
		this.setRegistryName(new ResourceLocation(ModReference.MOD_ID, name));
		this.setUnlocalizedName(name);
		this.part = part;
	}

	public SlugCasingParts getPart() {
		return part;
	}

}
