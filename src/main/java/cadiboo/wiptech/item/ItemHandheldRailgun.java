package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * @author Cadiboo
 */
public class ItemHandheldRailgun extends ItemHandheldGun {

	public static final AttachmentPoints[] ATTACHMENT_POINTS = new AttachmentPoints[] {

			AttachmentPoints.CIRCUIT,

			AttachmentPoints.SCOPE,

			AttachmentPoints.SIDE_LEFT,

			AttachmentPoints.SIDE_RIGHT,

			AttachmentPoints.RAIL,

			AttachmentPoints.UNDER

	};

	public ItemHandheldRailgun(final String name) {
		super(name, ATTACHMENT_POINTS);
	}

	@Override
	public void shoot(final World world, final EntityPlayer player) {

		if (!(player instanceof EntityPlayer)) {
			return;
		}

		final float velocity = 1.25F;
		if (!player.world.isRemote) {
			final EntityNapalm entitynapalm = new EntityNapalm(player.world, player);
			entitynapalm.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity, 5.0F);
			player.world.spawnEntity(entitynapalm);
		}
		player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, (1.0F / ((itemRand.nextFloat() * 0.4F) + 1.2F)) + (velocity * 0.5F));

	}

}
