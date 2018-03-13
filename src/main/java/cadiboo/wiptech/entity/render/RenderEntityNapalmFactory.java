package cadiboo.wiptech.entity.render;

import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityNapalmFactory
  implements IRenderFactory<EntityNapalm>
{
  public Render<? super EntityNapalm> createRenderFor(RenderManager manager)
  {
    return new RenderBasic(manager, Items.NAPALM, Minecraft.getMinecraft().getRenderItem(), 0.25F);
  }
}
