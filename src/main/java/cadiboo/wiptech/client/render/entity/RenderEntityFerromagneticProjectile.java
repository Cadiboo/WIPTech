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


//Based on Immersive Engineering
//https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/client/render/EntityRenderRevolvershot.java

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

		//Direction that its heading
		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);

		// flip it, flop it, pop it, pull it, push it, rotate it, translate it, TECHNOLOGY
		// rotate it into the direction we threw it
		//GlStateManager.rotate(entity.rotationYaw, 0f, 1f, 0f);
		//GlStateManager.rotate(-entity.rotationPitch, 1f, 0f, 0f);

		// arrow shake
		float renderShake = (float)entity.arrowShake - partialTicks;

		if (renderShake > 0.0F)
		{
			float angle = -MathHelper.sin(renderShake * 3.0F) * renderShake;
			GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
		}

		if(entity.getAmmoId()==9) {
			double scale = 0.25;

			this.bindTexture(new ResourceLocation(Reference.ID, "textures/entities/plasma_core.png"));
			drawQuad(0, 1, 0, 1, scale, scale, scale, scale);

			this.bindTexture(new ResourceLocation(Reference.ID, "textures/entities/plasma_field.png"));
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE); //maybe change GL_ONE to src alpha OR 3 one minus src color
			drawQuad(0, 1, 0, 1, scale, scale, scale, scale);
			GlStateManager.disableBlend();

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
					//float midU = sprite.getInterpolatedU(width/2) //TODO look at this

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

					if(entity.overheat)
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

	private void drawQuad (float minU, float maxU, float minV, float maxV, double width, double height, double length, double scale) {

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		GlStateManager.scale(scale, scale, scale);

		/*this.bindTexture(Reference.DEBUG2_TEXTURE);
		minU = 0;
		maxU = 1;
		minV = 0;
		maxV = 1;*/
		double hlfU = minU + (maxU-minU)/2;
		double hlfV = minV + (maxV-minV)/2;

		double centre = 0d;

		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		//UP
		bufferbuilder.pos(-length,  height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(-length,  height,  width).tex(maxU, minV).endVertex();
		bufferbuilder.pos( length,  height,  width).tex(minU, minV).endVertex();
		bufferbuilder.pos( length,  height, -width).tex(minU, maxV).endVertex();

		//DOWN
		bufferbuilder.pos(-length, -height,  width).tex(minU, minV).endVertex();
		bufferbuilder.pos(-length, -height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos( length, -height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos( length, -height,  width).tex(maxU, minV).endVertex();

		//LEFT
		bufferbuilder.pos( length, -height,  width).tex(maxU, minV).endVertex();
		bufferbuilder.pos( length, -height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos( length,  height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos( length,  height,  width).tex(minU, minV).endVertex();

		//RIGHT
		bufferbuilder.pos(-length, -height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-length, -height,  width).tex(minU, minV).endVertex();
		bufferbuilder.pos(-length,  height,  width).tex(maxU, minV).endVertex();
		bufferbuilder.pos(-length,  height, -width).tex(maxU, maxV).endVertex();

		//BACK BOTTOM
		bufferbuilder.pos(-length, -height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-length,  centre, -width).tex(minU, hlfV).endVertex();
		bufferbuilder.pos( length,  centre, -width).tex(maxU, hlfV).endVertex();
		bufferbuilder.pos( length, -height, -width).tex(maxU, maxV).endVertex();

		//BACK TOP
		bufferbuilder.pos(-length,  centre, -width).tex(maxU, hlfV).endVertex();
		bufferbuilder.pos(-length,  height, -width).tex(maxU, maxV).endVertex();
		bufferbuilder.pos( length,  height, -width).tex(minU, maxV).endVertex();
		bufferbuilder.pos( length,  centre, -width).tex(minU, hlfV).endVertex();

		//FRONT BOTTOM
		bufferbuilder.pos( length, -height,  width).tex(maxU, minV).endVertex();
		bufferbuilder.pos( length,  centre,  width).tex(maxU, hlfV).endVertex();
		bufferbuilder.pos(-length,  centre,  width).tex(minU, hlfV).endVertex();
		bufferbuilder.pos(-length, -height,  width).tex(minU, minV).endVertex();

		//FRONT TOP
		bufferbuilder.pos( length,  centre,  width).tex(minU, hlfV).endVertex();
		bufferbuilder.pos( length,  height,  width).tex(minU, minV).endVertex();
		bufferbuilder.pos(-length,  height,  width).tex(maxU, minV).endVertex();
		bufferbuilder.pos(-length,  centre,  width).tex(maxU, hlfV).endVertex();

		tessellator.draw();
	}

}