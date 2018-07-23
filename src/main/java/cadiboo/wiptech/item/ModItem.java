package cadiboo.wiptech.item;

import cadiboo.wiptech.creativetab.ModCreativeTabs;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ModItem extends Item {

	public ModItem(String name) {
		this.setRegistryName(new ResourceLocation(ModReference.ID, name));
		this.setUnlocalizedName(name);
		this.setCreativeTab(ModCreativeTabs.CREATIVE_TAB);
	}

}
