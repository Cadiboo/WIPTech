package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Cadiboo
 */
public class ItemHandheldPlasmagun extends ItemHandheldGun {

	public static final AttachmentPoints[] ATTACHMENT_POINTS = new AttachmentPoints[] {

			AttachmentPoints.CIRCUIT,

			AttachmentPoints.COIL,

			AttachmentPoints.SCOPE,

			AttachmentPoints.SIDE_LEFT,

			AttachmentPoints.SIDE_RIGHT,

			AttachmentPoints.RAIL,

			AttachmentPoints.UNDER

	};

	public ItemHandheldPlasmagun(final String name) {
		super(name, ATTACHMENT_POINTS);
	}

	@Override
	public void shoot(final World world, final EntityPlayer player) {

	}

}
