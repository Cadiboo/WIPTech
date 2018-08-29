package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ModItem extends Item {

	public ModItem(String name) {
		this.setRegistryName(new ResourceLocation(ModReference.Version.getModId(), name));
		this.setUnlocalizedName(name);
		ModUtil.setCreativeTab(this);
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
