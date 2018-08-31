package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;

public class ItemModSword extends ItemSword implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemModSword(final ModMaterials materialIn) {
		super(materialIn.getToolMaterial());
		ModUtil.setRegistryNames(this, materialIn, "sword");
		this.material = materialIn;
	}

	@Override
	public final ModMaterials getModMaterial() {
		return this.material;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
