package cadiboo.wiptech.item;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.attachments.AttachmentList;
import cadiboo.wiptech.entity.projectile.EntityCoilgunBullet;
import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
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

		final float velocity = 20;
		if (!player.world.isRemote) {
			final EntityCoilgunBullet entityCoilgunBullet = new EntityCoilgunBullet(player.world, player);
			entityCoilgunBullet.shoot(player, player.rotationPitch, player.rotationYaw, 0, velocity, 0);
			player.world.spawnEntity(entityCoilgunBullet);
		}
		player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, (1.0F / ((itemRand.nextFloat() * 0.4F) + 1.2F)) + (velocity * 0.5F));

	}

}
