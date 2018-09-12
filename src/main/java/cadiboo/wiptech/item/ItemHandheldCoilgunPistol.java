package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Cadiboo
 */
public class ItemHandheldCoilgunPistol extends ItemHandheldGun {

	public static final AttachmentPoints[] ATTACHMENT_POINTS = new AttachmentPoints[] {

			AttachmentPoints.CIRCUIT,

			AttachmentPoints.COIL,

			AttachmentPoints.SCOPE,

			AttachmentPoints.SILENCER

	};

	public ItemHandheldCoilgunPistol(final String name) {
		super(name, ATTACHMENT_POINTS);
	}

	@Override
	public void shoot(final World world, final EntityPlayer player) {

	}

}
