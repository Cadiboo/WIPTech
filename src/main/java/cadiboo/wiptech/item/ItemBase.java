package cadiboo.wiptech.item;

import cadiboo.wiptech.util.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBase extends Item {

	public ItemBase(String name) {
		this.setRegistryName(new ResourceLocation(Reference.ID, name));
		this.setUnlocalizedName(name);
	}

}
