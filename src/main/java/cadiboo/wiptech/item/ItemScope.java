package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import cadiboo.wiptech.util.ModEnums.ScopeTypes;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.item.Item;

/**
 * @author Cadiboo
 */
public class ItemScope extends Item implements IItemAttachment, IModItem {

	private final ScopeTypes type;

	public ItemScope(final String name, final ScopeTypes type) {
		ModUtil.setRegistryNames(this, type.getNameLowercase() + "_" + name);
		this.type = type;
	}

	public ScopeTypes getType() {
		return this.type;
	}

	@Override
	public AttachmentPoints getAttachmentPoint() {
		return AttachmentPoints.SCOPE;
	}

}
