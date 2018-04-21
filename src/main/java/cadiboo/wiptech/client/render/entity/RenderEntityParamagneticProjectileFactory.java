package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityParamagneticProjectileFactory implements IRenderFactory<EntityParamagneticProjectile> {
	@Override
	public Render<? super EntityParamagneticProjectile> createRenderFor(RenderManager manager) {
		return new RenderEntityParamagneticProjectile(manager);
	}
}
