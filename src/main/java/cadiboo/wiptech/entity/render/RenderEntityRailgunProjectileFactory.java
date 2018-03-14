package cadiboo.wiptech.entity.render;

import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntityRailgunProjectile;
import cadiboo.wiptech.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityRailgunProjectileFactory
  implements IRenderFactory<EntityRailgunProjectile>
{
  public Render<? super EntityRailgunProjectile> createRenderFor(RenderManager manager)
  {
    return new RenderEntityRailgunProjectile(manager);
  }
}
