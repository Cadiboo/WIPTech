package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.item.Item;

/**
 * @author Cadiboo
 */
public class ItemShotgun extends Item implements IItemAttachment, IModItem {

	public ItemShotgun(final String name) {
		ModUtil.setRegistryNames(this, name);
	}

	@Override
	public AttachmentPoints getAttachmentPoint() {
		return AttachmentPoints.UNDER;
	}

}
