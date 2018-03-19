package cadiboo.wiptech.client.render;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import cadiboo.wiptech.init.Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;


//COPPIED FROM IMMERSIVE ENGINEERING
// https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/client/render/EntityRenderRevolvershot.java
/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */



public class RenderEntityFerromagneticProjectile<T extends EntityFerromagneticProjectile> extends Render<T> {

	protected RenderEntityFerromagneticProjectile(RenderManager renderManager) {
		super(renderManager);
	}

	private ItemStack itemStack = new ItemStack(Items.FERROMAGNETIC_PROJECILE);

	@Override
	public void doRender(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		itemStack = entity.getAmmoStack();

		GlStateManager.pushMatrix();
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.translate(x, y, z);

		customRendering(entity, x, y, z, entityYaw, partialTicks);

		// arrow shake
		float renderShake = (float)entity.arrowShake - partialTicks;

		if (renderShake > 0.0F)
		{
			float angle = -MathHelper.sin(renderShake * 3.0F) * renderShake;
			GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
		}
		GlStateManager.enableRescaleNormal();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();




		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack);
		if(model!=null) {
			int color = -1;
			List<BakedQuad> quads = model.getQuads((IBlockState)null, (EnumFacing)null, 0L);

			//TODO if(quads.size > 0) do everything else {

			boolean flag = color == -1 && !itemStack.isEmpty();
			int i = 0;

			BakedQuad bakedquad = quads.get(0);

			/*int k = color;
					//TODO what does this do?

					if (flag && bakedquad.hasTintIndex())
					{
						k = Minecraft.getMinecraft().getItemColors().colorMultiplier(itemStack, bakedquad.getTintIndex());

						if (EntityRenderer.anaglyphEnable)
						{
							k = TextureUtil.anaglyphColor(k);
						}

						k = k | -16777216;
					}

					net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(bufferbuilder, bakedquad, k);
			 */
			TextureAtlasSprite sprite = bakedquad.getSprite();
			if(sprite!=null) {

				float minU = sprite.getMinU();
				float maxU = sprite.getMaxU();
				float minV = sprite.getMinV();
				float maxV = sprite.getMaxV();

				float width = maxU-minU;
				float height = maxV-minV;

				float midU = minU+(width/2);
				float midV = minV+(height/2);

				WIPTech.logger.info(minU);
				WIPTech.logger.info(maxU);
				WIPTech.logger.info(minV);
				WIPTech.logger.info(maxV);

				WIPTech.logger.info(width);
				WIPTech.logger.info(height);

				WIPTech.logger.info(midU);
				WIPTech.logger.info(midV);

				GlStateManager.disableCull();
				//GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
				//GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);

				//GlStateManager.scale(.25f, .25f, .25f);

				double x0 = -1;
				double x1 = 1;
				double y0 = 0.25;
				double y1 = 0.75;
				double z0 = -1;
				double z1 = 1;
				
				bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				bufferbuilder.pos(0, y0, z0).tex(maxU, maxV).endVertex();
				bufferbuilder.pos(0, y0, z1).tex(minU, maxV).endVertex();
				bufferbuilder.pos(0, y1, z1).tex(minU, minV).endVertex();
				bufferbuilder.pos(0, y1, z0).tex(maxU, minV).endVertex();
				tessellator.draw();
				//TODO Might need to be -0.5 & 0.5 instead of .25 & .75
				bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				bufferbuilder.pos(x0, y0, 0).tex(maxU, maxV).endVertex();
				bufferbuilder.pos(x1, y0, 0).tex(minU, maxV).endVertex();
				bufferbuilder.pos(x1, y1, 0).tex(minU, minV).endVertex();
				bufferbuilder.pos(x0, y1, 0).tex(maxU, minV).endVertex();
				tessellator.draw();
				
				/*bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				bufferbuilder.pos(0, y0, z0).tex(maxU, maxV).endVertex();
				bufferbuilder.pos(0, y0, z1).tex(minU, maxV).endVertex();
				bufferbuilder.pos(0, y1, z1).tex(minU, minV).endVertex();
				bufferbuilder.pos(0, y1, z0).tex(maxU, minV).endVertex();
				tessellator.draw();*/

				/*
				buffer.pos(x1, y0, z0).color(r, g, b, a).endVertex();
				buffer.pos(x1, y0, z1).color(r, g, b, a).endVertex();
				buffer.pos(x0, y0, z1).color(r, g, b, a).endVertex();
				buffer.pos(x0, y0, z0).color(r, g, b, a).endVertex();
				 */

				/*bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				bufferbuilder.pos(.375,	.125,0).tex(8/32d, 5/32d).endVertex();
				bufferbuilder.pos(0,	.125,0).tex(0/32d, 5/32d).endVertex();
				bufferbuilder.pos(0,	.375,0).tex(0/32d, 0/32d).endVertex();
				bufferbuilder.pos(.375,	.375,0).tex(8/32d, 0/32d).endVertex();
				tessellator.draw();
				 */
			}
		}

		GlStateManager.enableCull();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	/** If you just want to rotate it or something but the overall "have it heading towards the target" should stay the same */
	protected void customCustomRendering(T entity, double x, double y, double z, float entityYaw, float partialTicks) {}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull T entity) {
		return TextureMap.LOCATION_MISSING_TEXTURE;
	}

	public void customRendering(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

		// flip it, flop it, pop it, pull it, push it, rotate it, translate it, TECHNOLOGY

		// rotate it into the direction we threw it
		GL11.glRotatef(entity.rotationYaw, 0f, 1f, 0f);
		GL11.glRotatef(-entity.rotationPitch, 1f, 0f, 0f);

		// adjust "stuck" depth
		if(entity.inGround) {
			GL11.glTranslated(0, 0, -entity.getStuckDepth());
		}

		customCustomRendering(entity, x, y, z, entityYaw, partialTicks);

		// rotate it so it faces forward
		GL11.glRotatef(-90f, 0f, 1f, 0f);

		// rotate the projectile it so it faces upwards
		GL11.glRotatef(-45, 0f, 0f, 1f);
	}

}