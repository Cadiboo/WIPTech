package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;

public class ItemModPickaxe extends ItemPickaxe implements IItemModMaterial {

	protected final ModMaterials material;

	public ItemModPickaxe(final ModMaterials materialIn) {
		super(materialIn.getToolMaterial());
		ModUtil.setRegistryNames(this, materialIn, "pickaxe");
		this.material = materialIn;
	}

	@Override
	public ModMaterials getModMaterial() {
		return this.material;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
