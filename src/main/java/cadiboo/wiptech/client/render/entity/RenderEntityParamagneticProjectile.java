package cadiboo.wiptech.client.render.entity;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

//Based on Immersive Engineering
//https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/client/render/EntityRenderRevolvershot.java

public class RenderEntityParamagneticProjectile<T extends EntityParamagneticProjectile> extends Render<T> {

	protected RenderEntityParamagneticProjectile(RenderManager renderManager) {
		super(renderManager);
	}

	private ItemStack ammoStack = new ItemStack(Items.PARAMAGNETIC_PROJECILE);
	private boolean overheated = false;
	// Heading, Pitch, Roll
	// Heading (being a rotation around an "up"-axis), pitch (being a rotation
	// around a "right"-axis), and roll (being a rotation about a "forward"-axis)

	@Override
	public void doRender(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		ammoStack = entity.getAmmoStack();
		overheated = entity.isOverheated();

		GlStateManager.pushMatrix();
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.translate(x, y + 0.25, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableCull();

		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0,
				1, 0);
		GlStateManager.rotate(
				-(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks), 1, 0,
				0);

		// projectile shake
		float renderShake = entity.projectileShake - partialTicks;

		if (renderShake > 0.0F) {
			float angle = -MathHelper.sin(renderShake * 3.0F) * renderShake;
			GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
		}

		if (entity.isPlasma()) {
			GlStateManager.scale(0.5, 0.5, 0.5);

			this.bindTexture(new ResourceLocation(Reference.ID, "textures/entities/plasma_core.png"));
			// GlStateManager.depthMask(true);
			drawQuad(0, 1, 0, 1, 0.125, 0.125, 0.125, 1);
			// GlStateManager.depthMask(false);

			this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			RenderHelper.disableStandardItemLighting();

			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(7425);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			GlStateManager.disableAlpha();
			GlStateManager.enableCull();
			GlStateManager.depthMask(false);
			// GlStateManager.translate(0.0F, -1.0F, -2.0F);

			GlStateManager.color(0.6F, 0.5F, 1);

			drawQuad(0, 1, 0, 1, 0.25, 0.25, 0.25, 1);

			GlStateManager.depthMask(true);
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.shadeModel(7424);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();
			RenderHelper.enableStandardItemLighting();

			GlStateManager.popMatrix();

			return;

		} else {
			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(ammoStack);
			if (model != null) {
				// List<BakedQuad> quads = model.getQuads(null, null, 0L);
				// BakedQuad bakedquad = quads.get(0);

				TextureAtlasSprite sprite = model.getQuads(null, null, 0L).get(0).getSprite();

				// TextureAtlasSprite sprite = bakedquad.getSprite();
				if (sprite != null) {

					float minU = sprite.getMinU();
					float maxU = sprite.getMaxU();
					float minV = sprite.getMinV();
					float maxV = sprite.getMaxV();
					// float midU = sprite.getInterpolatedU(width/2) //TODO look at this

					float width = maxU - minU;
					float height = maxV - minV;

					float midU = minU + (width / 2);
					float midV = minV + (height / 2);

					float multiplier = 1;
					float scale = 1F;

					switch (entity.getAmmoId()) {
					default:
						scale = 1F;
						multiplier = 1;
						break;
					case 3:
					case 4:
					case 5:
						multiplier = 2;
						scale = 0.5F;
						break;
					case 6:
					case 7:
					case 8:
						multiplier = 2;
						scale = 0.25F;
						break;
					case 9: // never happens
						scale = 1F;
						break;
					}

					minU = midU - (width / (8F * multiplier));
					maxU = midU + (width / (8F * multiplier));
					minV = midV - (height / (8F * multiplier));
					maxV = midV + (height / (8F * multiplier));

					if (overheated)
						GlStateManager.color(1F, 0, 0);

					drawQuad(minU, maxU, minV, maxV, 1, 0.25, 0.25, scale);

				}
			}
		}

		GlStateManager.disableCull();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull T entity) {
		return TextureMap.LOCATION_MISSING_TEXTURE;
	}

	private void drawQuad(float minU, float maxU, float minV, float maxV, double width, double height, double length,
			double scale) {

		GlStateManager.scale(scale, scale, scale);

		double hlfU = minU + (maxU - minU) / 2;
		double hlfV = minV + (maxV - minV) / 2;

		double centre = 0d;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		// UP
		bufferbuilder.pos(-length, height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(-length, height, width).tex(maxU, minV).endVertex();
		bufferbuilder.pos(length, height, width).tex(minU, minV).endVertex();
		bufferbuilder.pos(length, height, -width).tex(minU, maxV).endVertex();

		// DOWN
		bufferbuilder.pos(-length, -height, width).tex(minU, minV).endVertex();
		bufferbuilder.pos(-length, -height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos(length, -height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(length, -height, width).tex(maxU, minV).endVertex();

		// LEFT
		bufferbuilder.pos(length, -height, width).tex(maxU, minV).endVertex();
		bufferbuilder.pos(length, -height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(length, height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos(length, height, width).tex(minU, minV).endVertex();

		// RIGHT
		bufferbuilder.pos(-length, -height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-length, -height, width).tex(minU, minV).endVertex();
		bufferbuilder.pos(-length, height, width).tex(maxU, minV).endVertex();
		bufferbuilder.pos(-length, height, -width).tex(maxU, maxV).endVertex();

		// BACK BOTTOM
		bufferbuilder.pos(-length, -height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-length, centre, -width).tex(minU, hlfV).endVertex();
		bufferbuilder.pos(length, centre, -width).tex(maxU, hlfV).endVertex();
		bufferbuilder.pos(length, -height, -width).tex(maxU, maxV).endVertex();

		// BACK TOP
		bufferbuilder.pos(-length, centre, -width).tex(maxU, hlfV).endVertex();
		bufferbuilder.pos(-length, height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(length, height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos(length, centre, -width).tex(minU, hlfV).endVertex();

		// FRONT BOTTOM
		bufferbuilder.pos(length, -height, width).tex(maxU, minV).endVertex();
		bufferbuilder.pos(length, centre, width).tex(maxU, hlfV).endVertex();
		bufferbuilder.pos(-length, centre, width).tex(minU, hlfV).endVertex();
		bufferbuilder.pos(-length, -height, width).tex(minU, minV).endVertex();

		// FRONT TOP
		bufferbuilder.pos(length, centre, width).tex(minU, hlfV).endVertex();
		bufferbuilder.pos(length, height, width).tex(minU, minV).endVertex();
		bufferbuilder.pos(-length, height, width).tex(maxU, minV).endVertex();
		bufferbuilder.pos(-length, centre, width).tex(maxU, hlfV).endVertex();

		tessellator.draw();

		GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
	}

}