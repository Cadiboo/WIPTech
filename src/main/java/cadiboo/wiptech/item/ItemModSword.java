package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;

public class ItemModSword extends ItemSword implements IItemModMaterial {

    protected final ModMaterials material;

    public ItemModSword(ModMaterials materialIn) {
	super(materialIn.getToolMaterial());
	ModUtil.setNameForMaterialItem(this, materialIn, "sword");
	ModUtil.setCreativeTab(this);
	this.material = materialIn;

    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
	return ModUtil.getCreativeTabs(this);
    }

    @Override
    public final ModMaterials getModMaterial() {
	return material;
    }

}
