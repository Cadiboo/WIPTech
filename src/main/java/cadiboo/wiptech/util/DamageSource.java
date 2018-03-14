package cadiboo.wiptech.util;

import javax.annotation.Nullable;

import cadiboo.wiptech.entity.projectile.EntityRailgunProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EntityDamageSourceIndirect;

public class DamageSource extends net.minecraft.util.DamageSource {

	public DamageSource(String damageTypeIn) {
		super(damageTypeIn);
	}

	net.minecraft.util.DamageSource railgunDamage = new net.minecraft.util.DamageSource("railgunprojectile").setDamageBypassesArmor().setDamageIsAbsolute();

	public static net.minecraft.util.DamageSource causeRailgunProjectileDamage(EntityRailgunProjectile projectile, @Nullable Entity indirectEntityIn)
	{
		return (new EntityDamageSourceIndirect("railgunprojectile", projectile, indirectEntityIn)).setProjectile();
	}

}
