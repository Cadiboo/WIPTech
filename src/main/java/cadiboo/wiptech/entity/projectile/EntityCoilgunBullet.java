package cadiboo.wiptech.entity.projectile;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.entity.IModEntity;
import cadiboo.wiptech.util.ModDamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityCoilgunBullet extends EntityHighSpeedProjectile implements IModEntity {

	public EntityCoilgunBullet(final World worldIn) {
		super(worldIn);
	}

	public EntityCoilgunBullet(final World world, final EntityPlayer player) {
		super(world, player);
		this.thrower = player;
		this.ignoreEntity = player;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
	protected void onImpact(final RayTraceResult result) {
		if (this.world.isRemote) {
			return;
		}
		WIPTech.info("EntityCoilgunBullet#onImpact");
		if (result == null) {
			return;
		}
		if (result.entityHit != null) {
//				result.entityHit.setDead(); //this completely breaks the game bahaha
			result.entityHit.attackEntityFrom(ModDamageSource.causeCoilgunBulletDamage(this, this.getThrower()), 10);
			WIPTech.info(result.entityHit);
		}

		this.setDead();

	}

}
