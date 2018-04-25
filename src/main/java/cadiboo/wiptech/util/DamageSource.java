package cadiboo.wiptech.util;

import javax.annotation.Nullable;

import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile113;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSourceIndirect;

public class DamageSource extends net.minecraft.util.DamageSource {

	public DamageSource(String damageTypeIn) {
		super(damageTypeIn);
	}

	net.minecraft.util.DamageSource	railgunDamage	= new net.minecraft.util.DamageSource("railgunprojectile").setDamageBypassesArmor().setDamageIsAbsolute();
	net.minecraft.util.DamageSource	coilgunDamage	= new net.minecraft.util.DamageSource("coilgunprojectile");
	net.minecraft.util.DamageSource	plasmaDamage	= new net.minecraft.util.DamageSource("plasma");

	public static net.minecraft.util.DamageSource causeRailgunProjectileDamage(EntityParamagneticProjectile projectile, @Nullable Entity indirectEntityIn) {
		return (new EntityDamageSourceIndirect("railgunprojectile", projectile, indirectEntityIn)).setProjectile();
	}

	public static net.minecraft.util.DamageSource causeCoilgunProjectileDamage(EntityParamagneticProjectile projectile, @Nullable Entity indirectEntityIn) {
		return (new EntityDamageSourceIndirect("coilgunprojectile", projectile, indirectEntityIn)).setProjectile();
	}

	public static net.minecraft.util.DamageSource causePlasmaProjectileDamage(EntityParamagneticProjectile projectile, @Nullable Entity indirectEntityIn) {
		return (new EntityDamageSourceIndirect("plasmaprojectile", projectile, indirectEntityIn)).setProjectile();
	}

	public static net.minecraft.util.DamageSource causeRailgunProjectileDamage113(EntityParamagneticProjectile113 projectile, @Nullable Entity indirectEntityIn) {
		return (new EntityDamageSourceIndirect("railgunprojectile", projectile, indirectEntityIn)).setProjectile();
	}

	public static net.minecraft.util.DamageSource causeCoilgunProjectileDamage113(EntityParamagneticProjectile113 projectile, @Nullable Entity indirectEntityIn) {
		return (new EntityDamageSourceIndirect("coilgunprojectile", projectile, indirectEntityIn)).setProjectile();
	}

	public static net.minecraft.util.DamageSource causePlasmaProjectileDamage113(EntityParamagneticProjectile113 projectile, @Nullable Entity indirectEntityIn) {
		return (new EntityDamageSourceIndirect("plasmaprojectile", projectile, indirectEntityIn)).setProjectile();
	}

}
