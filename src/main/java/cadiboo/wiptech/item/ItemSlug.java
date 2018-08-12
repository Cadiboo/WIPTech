package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.ModMaterials;

public class ItemSlug extends ModItem implements IItemModMaterial {
    protected final ModMaterials material;

    public ItemSlug(ModMaterials materialIn) {
	super(materialIn.getNameLowercase() + "_slug");
	this.material = materialIn;
    }

    @Override
    public ModMaterials getModMaterial() {
	return material;
    }

}
