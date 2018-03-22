package cadiboo.wiptech.client.render.entity;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import cadiboo.wiptech.Reference;
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
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableCull();

		customRendering(entity, x, y, z, entityYaw, partialTicks);

		// arrow shake
		float renderShake = (float)entity.arrowShake - partialTicks;

		if (renderShake > 0.0F)
		{
			float angle = -MathHelper.sin(renderShake * 3.0F) * renderShake;
			GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
		}

		if(entity.getAmmoId()==9) {

			this.bindTexture(new ResourceLocation(Reference.ID, "textures/entities/plasma_field.png"));
			double scale = 0.25;
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE); //maybe change GL_ONE to src alpha OR 3 one minus src color
			drawQuad(0, 1, 0, 1, 0.25, 0.25, 0.25, 0.25);
			GlStateManager.disableBlend();

			this.bindTexture(new ResourceLocation(Reference.ID, "textures/entities/plasma_core.png"));
			drawQuad(0, 1, 0, 1, 0.25, 0.25, 0.25, 0.25);
			GlStateManager.popMatrix();

			return;
		}
		else
		{
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

					float multiplier = 1;
					float scale = 1F;

					switch(entity.getAmmoId()) {
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
					case 9: //never happens
						scale = 1F;
						break;
					}

					minU = midU-(width/(8F*multiplier));
					maxU = midU+(width/(8F*multiplier));
					minV = midV-(height/(8F*multiplier));
					maxV = midV+(height/(8F*multiplier));


					drawQuad(minU, maxU, minV, maxV, 1, 0.25, 0.25, scale);

				}
			}
		}


		GlStateManager.disableCull();
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
		GlStateManager.rotate(entity.rotationYaw, 0f, 1f, 0f);
		GlStateManager.rotate(-entity.rotationPitch, 1f, 0f, 0f);

		// adjust "stuck" depth
		if(entity.inGround) {
			GlStateManager.translate(0, 0, -entity.getStuckDepth());
		}

		customCustomRendering(entity, x, y, z, entityYaw, partialTicks);

		// rotate it so it faces forward
		//GL11.glRotatef(-90f, 0f, 1f, 0f);

		// rotate the projectile it so it faces upwards
		//GL11.glRotatef(-45, 0f, 0f, 1f);
	}

	private void drawQuad (float minU, float maxU, float minV, float maxV, double width, double height, double length, double scale) {

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		GlStateManager.scale(scale, scale, scale);
		
		this.bindTexture(Reference.DEBUG2_TEXTURE);
		minU = 0;
		maxU = 1;
		minV = 0;
		maxV = 1;

		//WEST
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		bufferbuilder.pos(-length,  height,  width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(-length,  height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-length, -height, -width).tex(minU, minV).endVertex();
		bufferbuilder.pos(-length, -height,  width).tex(maxU, minV).endVertex();

		tessellator.draw();

		//EAST
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		bufferbuilder.pos( length,  height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos( length,  height,  width).tex(minU, maxV).endVertex();
		bufferbuilder.pos( length, -height,  width).tex(minU, minV).endVertex();
		bufferbuilder.pos( length, -height, -width).tex(maxU, minV).endVertex();

		tessellator.draw();

		//DOWN
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		bufferbuilder.pos( length, -height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos( length, -height,  width).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-length, -height,  width).tex(minU, minV).endVertex();
		bufferbuilder.pos(-length, -height, -width).tex(maxU, minV).endVertex();

		tessellator.draw();

		//UP
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		bufferbuilder.pos(-length,  height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(-length,  height,  width).tex(minU, maxV).endVertex();
		bufferbuilder.pos( length,  height,  width).tex(minU, minV).endVertex();
		bufferbuilder.pos( length,  height, -width).tex(maxU, minV).endVertex();

		tessellator.draw();
		
		
		//DONE! PERFECT! DONT EDIT!
		//SOUTH //FRONT
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		bufferbuilder.pos( length,  height,  width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(-length,  height,  width).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-length, -height,  width).tex(minU, minV).endVertex();
		bufferbuilder.pos( length, -height,  width).tex(maxU, minV).endVertex();

		tessellator.draw();

		
		//NORTH //BACK
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		bufferbuilder.pos(-length,  height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos( length,  height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos( length, -height, -width).tex(minU, minV).endVertex();
		bufferbuilder.pos(-length, -height, -width).tex(maxU, minV).endVertex();

		tessellator.draw();
	}

}