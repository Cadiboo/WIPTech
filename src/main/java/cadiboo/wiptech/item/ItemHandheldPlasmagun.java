package cadiboo.wiptech.item;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.attachments.AttachmentList;
import cadiboo.wiptech.util.ModEnums.AttachmentPoint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Cadiboo
 */
public class ItemHandheldPlasmagun extends ItemHandheldGun {

	public static final AttachmentPoint[] ATTACHMENT_POINTS = new AttachmentPoint[] {

			AttachmentPoint.CIRCUIT,

			AttachmentPoint.COIL,

			AttachmentPoint.SCOPE,

			AttachmentPoint.SIDE_LEFT,

			AttachmentPoint.SIDE_RIGHT,

			AttachmentPoint.RAIL,

			AttachmentPoint.UNDER

	};

	public ItemHandheldPlasmagun(final String name) {
		super(name, ATTACHMENT_POINTS);
	}

	@Override
	public void shoot(final World world, final EntityPlayer player, final AttachmentList attachmentList) {

		WIPTech.info("shot plasma");

	}

}
