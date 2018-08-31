package cadiboo.wiptech.item;

import javax.annotation.Nonnull;

import cadiboo.wiptech.util.ModEnums.ModMaterials;

public interface IItemModMaterial extends IModItem {

	@Nonnull
	ModMaterials getModMaterial();

}
