package cadiboo.wiptech.client.render;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import cadiboo.wiptech.init.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.color.ItemColors;


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
		itemStack = entity.getAmmoStack();

		GlStateManager.pushMatrix();
		//this.bindEntityTexture(entity);
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

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

		ItemStack stack = itemStack;
		int color = -1;
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack);

		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values())
		{
			this.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, enumfacing, 0L), color, stack);
		}

		this.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, (EnumFacing)null, 0L), color, stack);
		tessellator.draw();

		GlStateManager.disableCull();
		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);

		//GlStateManager.scale(.25f, .25f, .25f);

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
		}
	}
}