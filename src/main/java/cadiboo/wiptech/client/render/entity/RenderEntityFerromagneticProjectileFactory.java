package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityFerromagneticProjectileFactory implements IRenderFactory<EntityFerromagneticProjectile> {
	@Override
	public Render<? super EntityFerromagneticProjectile> createRenderFor(RenderManager manager) {
		return new RenderEntityFerromagneticProjectile(manager);
	}
}
