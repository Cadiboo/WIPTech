package cadiboo.wiptech.item;

import cadiboo.wiptech.creativetab.ModCreativeTabs;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.ResourceLocation;

public class ItemModPickaxe extends ItemPickaxe {

	public ItemModPickaxe(ModMaterials materialIn) {

		super(materialIn.getToolMaterial());
		ResourceLocation name = new ResourceLocation(materialIn.getResouceLocationDomain(), materialIn.getNameLowercase() + "_pickaxe");
		this.setRegistryName(name);
		this.setUnlocalizedName(name.getResourcePath());
		this.setCreativeTab(ModCreativeTabs.CREATIVE_TAB);

	}

}
