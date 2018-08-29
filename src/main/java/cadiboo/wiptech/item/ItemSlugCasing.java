package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.SlugCasingTypes;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemSlugCasing extends Item {

	private SlugCasingTypes type;

	public ItemSlugCasing(String name, SlugCasingTypes type) {
		this.setRegistryName(new ResourceLocation(ModReference.Version.getModId(), name + "_" + type.getNameLowercase()));
		this.setUnlocalizedName(name + "_" + type.getNameLowercase());
		this.type = type;
	}

	public SlugCasingTypes getType() {
		return type;
	}

}
