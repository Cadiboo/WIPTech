package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSpade;

public class ItemModShovel extends ItemSpade implements IItemModMaterial {

    protected final ModMaterials material;

    public ItemModShovel(ModMaterials materialIn) {

	super(materialIn.getToolMaterial());
	ModUtil.setNameForMaterialItem(this, materialIn, "shovel");
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
