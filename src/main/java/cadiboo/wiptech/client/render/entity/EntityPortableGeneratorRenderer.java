package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.client.model.ModelsCache;
import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityPortableGeneratorRenderer extends Render<EntityPortableGenerator> {

	public EntityPortableGeneratorRenderer(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(final EntityPortableGenerator entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(-entityYaw, 0, 1, 0);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.disableLighting();

		try {
			GlStateManager.translate(0, 0.5, 0);

			try {
				GlStateManager.pushMatrix();
				ClientUtil.renderModel(ModelsCache.INSTANCE.getBakedModel(new ModResourceLocation(ModReference.MOD_ID, "entity/portable_generator_body")));
				GlStateManager.popMatrix();
			} catch (Exception e) {
				WIPTech.error("Error rendering portable generator body");
				e.printStackTrace();
			}

			try {
				GlStateManager.pushMatrix();
				if (entity.getHandleHolder() == null) {
					GlStateManager.rotate(180, 1, 0, 0);
					GlStateManager.rotate(180, 0, 1, 0);
					GlStateManager.translate(0, -0.5, 0);
				}
				ClientUtil.renderModel(ModelsCache.INSTANCE.getBakedModel(new ModResourceLocation(ModReference.MOD_ID, "entity/portable_generator_handle")));
				GlStateManager.popMatrix();
			} catch (Exception e) {
				WIPTech.error("Error rendering portable generator handle");
				e.printStackTrace();
			}

			try {
				for (int wheelIndex = 0; wheelIndex < 4; wheelIndex++) {
					GlStateManager.pushMatrix();

					final float xOffset = 5.5f / 16f;
					final float yOffset = -5.5f / 16f;
					final float zOffset = 5.5f / 16f;

					GlStateManager.translate(xOffset * (wheelIndex % 2 == 0 ? 1 : -1), yOffset, zOffset * (wheelIndex / 2 == 0 ? 1 : -1));
					GlStateManager.rotate((wheelIndex % 2 == 0 ? 1 : -1) * 90, 0, 1, 0);
					GlStateManager.rotate((wheelIndex % 2 == 0 ? 1 : -1) * -(float) ((entity.prevPosX - entity.posX) + (entity.prevPosZ - entity.posZ) * 1000), 0, 0, 1);

					ClientUtil.renderModel(ModelsCache.INSTANCE.getBakedModel(new ModResourceLocation(ModReference.MOD_ID, "entity/portable_generator_wheel")));
					GlStateManager.popMatrix();
				}
			} catch (Exception e) {
				WIPTech.error("Error rendering portable generator wheels");
				e.printStackTrace();
			}

		} catch (Exception e) {
			WIPTech.error("Error rendering portable generator parts");
			e.printStackTrace();
		}

		GlStateManager.enableLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPortableGenerator entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
