package cadiboo.wiptech.client.render;

import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile2;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityFerromagneticProjectile2Factory
implements IRenderFactory<EntityFerromagneticProjectile2>
{
	public Render<? super EntityFerromagneticProjectile2> createRenderFor(RenderManager manager)
	{
		return new RenderEntityFerromagneticProjectile(manager);
	}
}
