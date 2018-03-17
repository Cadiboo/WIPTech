package cadiboo.wiptech.entity.render;

import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityFerromagneticProjectileFactory
  implements IRenderFactory<EntityFerromagneticProjectile>
{
  public Render<? super EntityFerromagneticProjectile> createRenderFor(RenderManager manager)
  {
    return new RenderEntityFerromagneticProjectile(manager);
  }
}
