package cadiboo.wiptech.util;

import cadiboo.wiptech.entity.projectile.EntityCoilgunBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class ModDamageSource {

	public static final DamageSource ELECTRICITY = (new DamageSource("electricity")).setDamageIsAbsolute().setDifficultyScaled();

	public static DamageSource causeCoilgunBulletDamage(final EntityCoilgunBullet coilgunBullet, final EntityLivingBase shooter) {
		return (new EntityDamageSourceIndirect("coilgun_bullet", coilgunBullet, shooter)).setDamageIsAbsolute().setDifficultyScaled().setProjectile();
	}

//	public static DamageSource causeRailgunBulletDamage(final EntityRailgunBullet railgunBullet, final EntityLivingBase shooter) {
//		return (new EntityDamageSourceIndirect("railgun_bullet", railgunBullet, shooter)).setDamageIsAbsolute().setDifficultyScaled().setProjectile();
//	}
//
//	public static DamageSource causePlasmagunBulletDamage(final EntityPlasmagunBullet plasmagunBullet, final EntityLivingBase shooter) {
//		return (new EntityDamageSourceIndirect("plasmagun_bullet", plasmagunBullet, shooter)).setDamageIsAbsolute().setDifficultyScaled().setProjectile().setFireDamage();
//	}

}
