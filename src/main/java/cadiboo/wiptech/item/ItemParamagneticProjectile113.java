package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile113;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectiles;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemParamagneticProjectile113 extends ItemBase {

	private ParamagneticProjectiles type;

	public ItemParamagneticProjectile113(String name, ParamagneticProjectiles type) {
		super(name);
		this.type = type;
	}

	public EntityParamagneticProjectile113 createProjectile(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		EntityParamagneticProjectile113 projectile = new EntityParamagneticProjectile113(worldIn, shooter);
		projectile.setType(this.type);
		return projectile;
	}

	public float getVelocity() {
		return this.type.getVelocity();
	}

}
