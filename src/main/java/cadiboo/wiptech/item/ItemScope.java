package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.AttachmentPoint;
import cadiboo.wiptech.util.ModEnums.ScopeType;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.item.Item;

/**
 * @author Cadiboo
 */
public class ItemScope extends Item implements IItemAttachment, IModItem {

	private final ScopeType type;

	public ItemScope(final String name, final ScopeType type) {
		ModUtil.setRegistryNames(this, type.getNameLowercase() + "_" + name);
		this.type = type;
	}

	public ScopeType getType() {
		return this.type;
	}

	@Override
	public AttachmentPoint getAttachmentPoint() {
		return AttachmentPoint.SCOPE;
	}

}
