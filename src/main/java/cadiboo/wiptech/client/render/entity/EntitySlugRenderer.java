package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.entity.projectile.EntitySlug;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EntitySlugRenderer extends Render<EntitySlug> {

	public EntitySlugRenderer(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntitySlug entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.pushMatrix();

		GlStateManager.translate((float) x, (float) y + 0.5, (float) z);

		GlStateManager.rotate(entity.rotationYaw, 0, 1, 0);
		GlStateManager.rotate(entity.rotationPitch, 1, 0, 0);

		GlStateManager.translate(0, -0.56f, 0);

		ClientUtil.renderStack(new ItemStack(entity.getMaterial().getSlugItem()), entity.world);

		GlStateManager.popMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySlug entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
