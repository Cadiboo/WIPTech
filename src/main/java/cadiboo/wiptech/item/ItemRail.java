package cadiboo.wiptech.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cadiboo.wiptech.creativetab.ModCreativeTabs;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.creativetab.CreativeTabs;

public class ItemRail extends ModItem implements IItemModMaterial {

    protected final ModMaterials material;

    public ItemRail(ModMaterials materialIn) {
	super(materialIn.getNameLowercase() + "_rail");
	this.material = materialIn;
    }

    @Override
    public final ModMaterials getModMaterial() {
	return this.material;
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
	List<CreativeTabs> tabs = new ArrayList<CreativeTabs>(Arrays.asList(super.getCreativeTabs()));
	tabs.add(ModCreativeTabs.CREATIVE_TAB);
	return tabs.toArray(new CreativeTabs[0]);
    }

}
