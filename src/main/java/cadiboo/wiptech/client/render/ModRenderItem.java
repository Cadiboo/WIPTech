package cadiboo.wiptech.client.render;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public final class ModRenderItem {

	public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	public static void renderModel(final IBakedModel model, final ItemStack stack) {
		renderModel(model, -1, stack);
	}

	public static void renderModel(final IBakedModel model, final int color) {
		renderModel(model, color, ItemStack.EMPTY);
	}

	public static void renderModel(final IBakedModel model, final int color, final ItemStack stack) {
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

		for (final EnumFacing enumfacing : EnumFacing.values()) {
			renderQuads(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), color, stack);
		}

		renderQuads(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color, stack);
		tessellator.draw();
	}

	public static void renderItem(final ItemStack stack, final IBakedModel model) {
		if (!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);

			if (model.isBuiltInRenderer()) {
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableRescaleNormal();
				stack.getItem().getTileEntityItemStackRenderer().renderByItem(stack);
			} else {
				renderModel(model, stack);

				if (stack.hasEffect()) {
					renderEffect(model);
				}
			}

			GlStateManager.popMatrix();
		}
	}

	public static void renderEffect(final IBakedModel model) {
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
		Minecraft.getMinecraft().getTextureManager().bindTexture(RES_ITEM_GLINT);
		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		final float f = (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
		GlStateManager.translate(f, 0.0F, 0.0F);
		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
		renderModel(model, -8372020);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		final float f1 = (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
		renderModel(model, -8372020);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	public static void putQuadNormal(final BufferBuilder renderer, final BakedQuad quad) {
		final Vec3i vec3i = quad.getFace().getDirectionVec();
		renderer.putNormal(vec3i.getX(), vec3i.getY(), vec3i.getZ());
	}

	public static void renderQuad(final BufferBuilder renderer, final BakedQuad quad, final int color) {
		renderer.addVertexData(quad.getVertexData());
		renderer.putColor4(color);
		putQuadNormal(renderer, quad);
	}

	public static void renderQuads(final BufferBuilder renderer, final List<BakedQuad> quads, final int color, final ItemStack stack) {
		final boolean flag = (color == -1) && !stack.isEmpty();
		int i = 0;

		for (final int j = quads.size(); i < j; ++i) {
			final BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex()) {
				k = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

	public static boolean shouldRenderItemIn3D(final ItemStack stack) {
		final IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
		return ibakedmodel == null ? false : ibakedmodel.isGui3d();
	}

	public static void renderItem(final ItemStack stack, final ItemCameraTransforms.TransformType cameraTransformType) {
		if (!stack.isEmpty()) {
			final IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null);
			renderItemModel(stack, ibakedmodel, cameraTransformType, false);
		}
	}

	public static IBakedModel getItemModelWithOverrides(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entitylivingbaseIn) {
		final IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
		return ibakedmodel.getOverrides().handleItemState(ibakedmodel, stack, worldIn, entitylivingbaseIn);
	}

	public static void renderItem(final ItemStack stack, final EntityLivingBase entitylivingbaseIn, final ItemCameraTransforms.TransformType transform, final boolean leftHanded) {
		if (!stack.isEmpty() && (entitylivingbaseIn != null)) {
			final IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, entitylivingbaseIn.world, entitylivingbaseIn);
			renderItemModel(stack, ibakedmodel, transform, leftHanded);
		}
	}

	public static void renderItemModel(final ItemStack stack, IBakedModel bakedmodel, final ItemCameraTransforms.TransformType transform, final boolean leftHanded) {
		if (!stack.isEmpty()) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.pushMatrix();
			// TODO: check if negative scale is a thing
			bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, transform, leftHanded);

			renderItem(stack, bakedmodel);
			GlStateManager.cullFace(GlStateManager.CullFace.BACK);
			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		}
	}

	/**
	 * Return true if only one scale is negative
	 */
	public static boolean isThereOneNegativeScale(final ItemTransformVec3f itemTranformVec) {
		return (itemTranformVec.scale.x < 0.0F) ^ (itemTranformVec.scale.y < 0.0F) ^ (itemTranformVec.scale.z < 0.0F);
	}

	public static void renderItemIntoGUI(final ItemStack stack, final int x, final int y) {
		renderItemModelIntoGUI(stack, x, y, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
	}

	public static void renderItemModelIntoGUI(final ItemStack stack, final int x, final int y, IBakedModel bakedmodel) {
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		setupGuiTransform(x, y, bakedmodel.isGui3d());
		bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
		renderItem(stack, bakedmodel);
		GlStateManager.disableAlpha();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}

	public static void setupGuiTransform(final int xPosition, final int yPosition, final boolean isGui3d) {
		GlStateManager.translate(xPosition, yPosition, 100.0F + Minecraft.getMinecraft().getRenderItem().zLevel);
		GlStateManager.translate(8.0F, 8.0F, 0.0F);
		GlStateManager.scale(1.0F, -1.0F, 1.0F);
		GlStateManager.scale(16.0F, 16.0F, 16.0F);

		if (isGui3d) {
			GlStateManager.enableLighting();
		} else {
			GlStateManager.disableLighting();
		}
	}

	public static void renderItemAndEffectIntoGUI(final ItemStack stack, final int xPosition, final int yPosition) {
		renderItemAndEffectIntoGUI(Minecraft.getMinecraft().player, stack, xPosition, yPosition);
	}

	public static void renderItemAndEffectIntoGUI(@Nullable final EntityLivingBase entityLiving, final ItemStack stack, final int xPosition, final int yPosition) {
		if (!stack.isEmpty()) {
			Minecraft.getMinecraft().getRenderItem().zLevel += 50.0F;

			try {
				renderItemModelIntoGUI(stack, xPosition, yPosition, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, null, entityLiving));
			} catch (final Throwable throwable) {
				final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
				final CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
				crashreportcategory.addDetail("Item Type", () -> String.valueOf(stack.getItem()));
				crashreportcategory.addDetail("Registry Name", () -> String.valueOf(stack.getItem().getRegistryName()));
				crashreportcategory.addDetail("Item Aux", () -> String.valueOf(stack.getMetadata()));
				crashreportcategory.addDetail("Item NBT", () -> String.valueOf(stack.getTagCompound()));
				crashreportcategory.addDetail("Item Foil", () -> String.valueOf(stack.hasEffect()));
				throw new ReportedException(crashreport);
			}

			Minecraft.getMinecraft().getRenderItem().zLevel -= 50.0F;
		}
	}

	public static void renderItemOverlays(final FontRenderer fr, final ItemStack stack, final int xPosition, final int yPosition) {
		renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, (String) null);
	}

	/**
	 * Renders the stack size and/or damage bar for the given ItemStack.
	 */
	public static void renderItemOverlayIntoGUI(final FontRenderer fr, final ItemStack stack, final int xPosition, final int yPosition, @Nullable final String text) {
		if (!stack.isEmpty()) {
			if ((stack.getCount() != 1) || (text != null)) {
				final String s = text == null ? String.valueOf(stack.getCount()) : text;
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableBlend();
				fr.drawStringWithShadow(s, (xPosition + 19) - 2 - fr.getStringWidth(s), yPosition + 6 + 3, 16777215);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
				// Fixes opaque cooldown overlay a bit lower
				// TODO: check if enabled blending still screws things up down the line.
				GlStateManager.enableBlend();
			}

			if (stack.getItem().showDurabilityBar(stack)) {
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableTexture2D();
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				final Tessellator tessellator = Tessellator.getInstance();
				final BufferBuilder bufferbuilder = tessellator.getBuffer();
				final double health = stack.getItem().getDurabilityForDisplay(stack);
				final int rgbfordisplay = stack.getItem().getRGBDurabilityForDisplay(stack);
				final int i = Math.round(13.0F - ((float) health * 13.0F));
				final int j = rgbfordisplay;
				draw(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
				draw(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, (j >> 16) & 255, (j >> 8) & 255, j & 255, 255);
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.enableTexture2D();
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}

			final EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
			final float f3 = entityplayersp == null ? 0.0F : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());

			if (f3 > 0.0F) {
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableTexture2D();
				final Tessellator tessellator1 = Tessellator.getInstance();
				final BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
				draw(bufferbuilder1, xPosition, yPosition + MathHelper.floor(16.0F * (1.0F - f3)), 16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
				GlStateManager.enableTexture2D();
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}
		}
	}

	/**
	 * Draw with the WorldRenderer
	 */
	public static void draw(final BufferBuilder renderer, final int x, final int y, final int width, final int height, final int red, final int green, final int blue, final int alpha) {
		renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x + 0, y + 0, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + 0, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + width, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + width, y + 0, 0.0D).color(red, green, blue, alpha).endVertex();
		Tessellator.getInstance().draw();
	}

}
