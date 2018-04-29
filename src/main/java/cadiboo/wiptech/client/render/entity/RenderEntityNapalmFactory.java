package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.util.Reference;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityNapalmFactory implements IRenderFactory<EntityNapalm> {
	@Override
	public Render<? super EntityNapalm> createRenderFor(RenderManager manager) {
		return new Render2D(manager, new ResourceLocation(Reference.ID, "textures/entities/napalm.png"), 1);
	}
}