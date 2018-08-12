package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;

public class ItemModHoe extends ItemHoe implements IItemModMaterial {

    protected final ModMaterials material;

    public ItemModHoe(ModMaterials materialIn) {

	super(materialIn.getToolMaterial());
	ModUtil.setNameForMaterialItem(this, materialIn, "hoe");
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
