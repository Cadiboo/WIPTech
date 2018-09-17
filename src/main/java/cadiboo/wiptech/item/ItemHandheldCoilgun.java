package cadiboo.wiptech.item;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.attachments.AttachmentList;
import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Cadiboo
 */
public class ItemHandheldCoilgun extends ItemHandheldGun {

	public static final AttachmentPoints[] ATTACHMENT_POINTS = new AttachmentPoints[] {

			AttachmentPoints.CIRCUIT,

			AttachmentPoints.COIL,

			AttachmentPoints.SCOPE,

			AttachmentPoints.SIDE_LEFT,

			AttachmentPoints.SIDE_RIGHT,

			AttachmentPoints.UNDER

	};

	public ItemHandheldCoilgun(final String name) {
		super(name, ATTACHMENT_POINTS);
	}

	@Override
	public void shoot(final World world, final EntityPlayer player, final AttachmentList attachmentList) {

		WIPTech.info("shot coilgun");

	}

}
