package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.entity.projectile.EntityRailgunSlug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

public class EntityRailgunSlugRenderer extends Render<EntityRailgunSlug> {

	public EntityRailgunSlugRenderer(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityRailgunSlug entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.pushMatrix();

		GlStateManager.translate((float) x, (float) y + 0.5, (float) z);

		GlStateManager.translate(0, -0.56f, 0);

//		WIPTech.info(entity.getName());

		ItemStack stack = new ItemStack(entity.getMaterial().getBlock());

		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, entity.getEntityWorld(), null);
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

		GlStateManager.popMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityRailgunSlug entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
