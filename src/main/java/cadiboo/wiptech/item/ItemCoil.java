package cadiboo.wiptech.item;

import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.util.ModEnums.AttachmentPoint;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCoil extends Item implements IItemModMaterial, IModItem, IItemAttachment {

	private final ModMaterial material;

	public ItemCoil(final ModMaterial material) {
		ModUtil.setRegistryNames(this, material, "coil");
		this.material = material;
	}

	@Override
	public final ModMaterial getModMaterial() {
		return this.material;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

	@Override
	public AttachmentPoint getAttachmentPoint() {
		return AttachmentPoint.COIL;
	}

}
