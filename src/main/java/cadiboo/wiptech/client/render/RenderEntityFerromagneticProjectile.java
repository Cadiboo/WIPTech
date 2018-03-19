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
	public void doRender(@Nonnull T entity, double x, double y, double z, float f0, float f1)
	{

		/*if(!itemStack.isEmpty()) {
		Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, ItemCameraTransforms.TransformType.NONE);
	}
	else {
		ItemStack dummy = ItemStack.EMPTY;
		Minecraft.getMinecraft().getRenderItem().renderItem(dummy, Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getMissingModel());
	}*/

		/*
	 else if (Block.getBlockFromItem(item) instanceof BlockShulkerBox)
        {
            TileEntityRendererDispatcher.instance.render(SHULKER_BOXES[BlockShulkerBox.getColorFromItem(item).getMetadata()], 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        }
		 */


		itemStack = entity.getAmmoStack();

		GlStateManager.pushMatrix();
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();




		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack);
		if(model!=null) {
			//bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
			int color = -1;
			for (EnumFacing enumfacing : EnumFacing.values())
			{
				List<BakedQuad> quads = model.getQuads((IBlockState)null, EnumFacing.UP, 0L);


				boolean flag = color == -1 && !itemStack.isEmpty();
				int i = 0;

				for (int j = quads.size(); i < j; ++i)
				{
					BakedQuad bakedquad = quads.get(i);
					/*int k = color;
					//TODO now make it only render part of the texture

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
						WIPTech.logger.info(bakedquad.getSprite());

						float minU = sprite.getMinU();
						float maxU = sprite.getMaxU();
						float minV = sprite.getMinV();
						float maxV = sprite.getMaxV();
						
						WIPTech.logger.info(minU);
						WIPTech.logger.info(maxU);
						WIPTech.logger.info(minV);
						WIPTech.logger.info(maxV);

						GlStateManager.disableCull();
						GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
						GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);

						//GlStateManager.scale(.25f, .25f, .25f);

						bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						bufferbuilder.pos(0, .0,-.25).tex(maxU, maxV).endVertex();
						bufferbuilder.pos(0, .0, .25).tex(minU, maxV).endVertex();
						bufferbuilder.pos(0, .5, .25).tex(minU, minV).endVertex();
						bufferbuilder.pos(0, .5,-.25).tex(maxU, minV).endVertex();
						tessellator.draw();
						
						bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						bufferbuilder.pos(.375,	.125,0).tex(8/32d, 5/32d).endVertex();
						bufferbuilder.pos(0,	.125,0).tex(0/32d, 5/32d).endVertex();
						bufferbuilder.pos(0,	.375,0).tex(0/32d, 0/32d).endVertex();
						bufferbuilder.pos(.375,	.375,0).tex(8/32d, 0/32d).endVertex();
						tessellator.draw();

					}
				}
			}
		}



		/*int color = -1;


		for (EnumFacing enumfacing : EnumFacing.values())
		{
			this.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, enumfacing, 0L), color, itemStack);
		}


		/*List<BakedQuad> quads = model.getQuads((IBlockState)null, EnumFacing.UP, 0L);
		//WIPTech.logger.info(model);
		System.out.println(model);
		//WIPTech.logger.info(quads);
		System.out.println(quads);

		BakedQuad bakedquad = quads.get(0);
		System.out.println(bakedquad);
		//WIPTech.logger.info(bakedquad);
		TextureAtlasSprite sprite = bakedquad.getSprite();
		float minU = sprite.getMinU();
		float maxU = sprite.getMaxU();
		float minV = sprite.getMinV();
		float maxV = sprite.getMaxV();
		 *\/
		for (EnumFacing enumfacing : EnumFacing.values())
		{
			this.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, enumfacing, 0L), color, itemStack);
		}

		//this.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, (EnumFacing)null, 0L), color, stack);
		//tessellator.draw();

		GlStateManager.disableCull();
		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);

		//GlStateManager.scale(.25f, .25f, .25f);

		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(0, .0,-.25).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(0, .0, .25).tex(minU, maxV).endVertex();
		bufferbuilder.pos(0, .5, .25).tex(minU, minV).endVertex();
		bufferbuilder.pos(0, .5,-.25).tex(maxU, minV).endVertex();
		tessellator.draw();

		 */

		/*bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(.375,	.125,0).tex(8/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.125,0).tex(0/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.375,0).tex(0/32d, 0/32d).endVertex();
		bufferbuilder.pos(.375,	.375,0).tex(8/32d, 0/32d).endVertex();
		tessellator.draw();

		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(.375,	.25,-.25).tex(8/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.25,-.25).tex(0/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.25, .25).tex(0/32d, 0/32d).endVertex();
		bufferbuilder.pos(.375,	.25, .25).tex(8/32d, 0/32d).endVertex();
		tessellator.draw();
		 */

		/*
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(0, .0,-.25).tex(5/32d, 10/32d).endVertex();
		bufferbuilder.pos(0, .0, .25).tex(0/32d, 10/32d).endVertex();
		bufferbuilder.pos(0, .5, .25).tex(0/32d, 5/32d).endVertex();
		bufferbuilder.pos(0, .5,-.25).tex(5/32d, 5/32d).endVertex();
		tessellator.draw();

		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(.375,	.125,0).tex(8/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.125,0).tex(0/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.375,0).tex(0/32d, 0/32d).endVertex();
		bufferbuilder.pos(.375,	.375,0).tex(8/32d, 0/32d).endVertex();
		tessellator.draw();

		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(.375,	.25,-.25).tex(8/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.25,-.25).tex(0/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.25, .25).tex(0/32d, 0/32d).endVertex();
		bufferbuilder.pos(.375,	.25, .25).tex(8/32d, 0/32d).endVertex();
		tessellator.draw();
		 */

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

	private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack)
	{
		boolean flag = color == -1 && !stack.isEmpty();
		int i = 0;

		for (int j = quads.size(); i < j; ++i)
		{
			BakedQuad bakedquad = quads.get(i);
			int k = color;
			//wow i got everything right
			//TODO now make it only render part of the texture

			if (flag && bakedquad.hasTintIndex())
			{
				k = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable)
				{
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);

			WIPTech.logger.info(bakedquad.getSprite());
			//PLEASE WØRK
		}
	}

	/*
	public static void putBakedQuad(IVertexConsumer consumer, BakedQuad quad)
    {
        consumer.setTexture(quad.getSprite());
        consumer.setQuadOrientation(quad.getFace());
        if(quad.hasTintIndex())
        {
            consumer.setQuadTint(quad.getTintIndex());
        }
        consumer.setApplyDiffuseLighting(quad.shouldApplyDiffuseLighting());
        float[] data = new float[4];
        VertexFormat formatFrom = consumer.getVertexFormat();
        VertexFormat formatTo = quad.getFormat();
        int countFrom = formatFrom.getElementCount();
        int countTo = formatTo.getElementCount();
        int[] eMap = mapFormats(formatFrom, formatTo);
        for(int v = 0; v < 4; v++)
        {
            for(int e = 0; e < countFrom; e++)
            {
                if(eMap[e] != countTo)
                {
                    unpack(quad.getVertexData(), data, formatTo, v, eMap[e]);
                    consumer.put(e, data);
                }
                else
                {
                    consumer.put(e);
                }
            }
        }
    }
	 */
}