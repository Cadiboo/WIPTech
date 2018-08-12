package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;

public class ItemModPickaxe extends ItemPickaxe implements IItemModMaterial {

    protected final ModMaterials material;

    public ItemModPickaxe(ModMaterials materialIn) {

	super(materialIn.getToolMaterial());
	ModUtil.setNameForMaterialItem(this, materialIn, "pickaxe");
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
