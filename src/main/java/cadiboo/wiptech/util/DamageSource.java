package cadiboo.wiptech.util;

import javax.annotation.Nullable;

import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSourceIndirect;

public class DamageSource extends net.minecraft.util.DamageSource {

	public DamageSource(String damageTypeIn) {
		super(damageTypeIn);
	}

	net.minecraft.util.DamageSource railgunDamage = new net.minecraft.util.DamageSource("railgunprojectile")
			.setDamageBypassesArmor().setDamageIsAbsolute();
	net.minecraft.util.DamageSource coilgunDamage = new net.minecraft.util.DamageSource("coilgunprojectile");
	net.minecraft.util.DamageSource plasmaDamage = new net.minecraft.util.DamageSource("plasma");

	public static net.minecraft.util.DamageSource causeRailgunProjectileDamage(EntityFerromagneticProjectile projectile,
			@Nullable Entity indirectEntityIn) {
		return (new EntityDamageSourceIndirect("railgunprojectile", projectile, indirectEntityIn)).setProjectile();
	}

	public static net.minecraft.util.DamageSource causeCoilgunProjectileDamage(EntityFerromagneticProjectile projectile,
			@Nullable Entity indirectEntityIn) {
		return (new EntityDamageSourceIndirect("coilgunprojectile", projectile, indirectEntityIn)).setProjectile();
	}

	public static net.minecraft.util.DamageSource causePlasmaProjectileDamage(EntityFerromagneticProjectile projectile,
			@Nullable Entity indirectEntityIn) {
		return (new EntityDamageSourceIndirect("plasmaprojectile", projectile, indirectEntityIn)).setProjectile();
	}

}
