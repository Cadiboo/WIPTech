package cadiboo.wiptech.item;

import javax.annotation.Nonnull;

import cadiboo.wiptech.material.ModMaterial;

public interface IItemModMaterial extends IModItem {

	@Nonnull
	ModMaterial getModMaterial();

}
