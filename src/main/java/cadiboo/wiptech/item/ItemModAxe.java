package cadiboo.wiptech.item;

import cadiboo.wiptech.creativetab.ModCreativeTabs;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.ResourceLocation;

public class ItemModAxe extends ItemAxe {

	public ItemModAxe(ModMaterials materialIn) {

		super(materialIn.getToolMaterial(), materialIn.getToolMaterial().getAttackDamage(), materialIn.getToolMaterial().getEfficiency());
		ResourceLocation name = new ResourceLocation(materialIn.getResouceLocationDomain(), materialIn.getNameLowercase() + "_axe");
		this.setRegistryName(name);
		this.setUnlocalizedName(name.getResourcePath());
		this.setCreativeTab(ModCreativeTabs.CREATIVE_TAB);

	}

}
