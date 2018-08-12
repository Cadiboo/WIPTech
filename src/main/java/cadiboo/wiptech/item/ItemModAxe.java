package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;

public class ItemModAxe extends ItemAxe implements IItemModMaterial {

    protected final ModMaterials material;

    public ItemModAxe(ModMaterials materialIn) {

	super(materialIn.getToolMaterial(), materialIn.getToolMaterial().getAttackDamage(),
		materialIn.getToolMaterial().getEfficiency());
	ModUtil.setNameForMaterialItem(this, materialIn, "axe");
	ModUtil.setCreativeTab(this);
	this.material = materialIn;

    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
	return ModUtil.getCreativeTabs(this);
    }

    @Override
    public ModMaterials getModMaterial() {
	return material;
    }

}
