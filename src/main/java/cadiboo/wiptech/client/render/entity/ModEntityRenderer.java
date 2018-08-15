package cadiboo.wiptech.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class ModEntityRenderer<ME extends Entity> extends Render<ME> {

	protected ModEntityRenderer(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(ME entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
