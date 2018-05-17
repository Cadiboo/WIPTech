package cadiboo.wiptech.util;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.energy.IEnergyStorage;

public class Utils {

	public static float getEnergyFraction(IEnergyStorage storage) {
		if (storage == null)
			return 0;
		return getEnergyFraction(storage.getEnergyStored(), storage.getMaxEnergyStored());
	}

	public static float getEnergyFraction(int energy, int max) {
		return (float) energy / (float) max;
	}

	public static int getEnergyPercentage(IEnergyStorage storage) {
		return Math.round(getEnergyFraction(storage) * 100f);
	}

	public static int getEnergyPercentage(int energy, int max) {
		return Math.round(getEnergyFraction(energy, max) * 100f);
	}

	public static Block getBlockFromPos(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getBlock();
	}

	public static IBlockState getStateFromPos(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos);
	}

	public static double randomBetween(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

	public static TextureAtlasSprite getSpriteFromItemStack(ItemStack stack) {
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
		if (model == null)
			return null;
		List<BakedQuad> quads = model.getQuads(null, null, 0L);
		if (quads == null || quads.size() <= 0)
			return null;
		TextureAtlasSprite sprite = quads.get(0).getSprite();
		if (sprite == null)
			return null;
		return sprite;
	}

	public static void drawCuboid(float minU, float maxU, float minV, float maxV, double x_size, double y_size, double z_size, double scale) {

		GlStateManager.scale(scale, scale, scale);

		double hlfU = minU + (maxU - minU) / 2;
		double hlfV = minV + (maxV - minV) / 2;

		double centre = 0d;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		// UP
		bufferbuilder.pos(-x_size, y_size, -z_size).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(-x_size, y_size, z_size).tex(maxU, minV).endVertex();
		bufferbuilder.pos(x_size, y_size, z_size).tex(minU, minV).endVertex();
		bufferbuilder.pos(x_size, y_size, -z_size).tex(minU, maxV).endVertex();

		// DOWN
		bufferbuilder.pos(-x_size, -y_size, z_size).tex(minU, minV).endVertex();
		bufferbuilder.pos(-x_size, -y_size, -z_size).tex(minU, maxV).endVertex();
		bufferbuilder.pos(x_size, -y_size, -z_size).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(x_size, -y_size, z_size).tex(maxU, minV).endVertex();

		// LEFT
		bufferbuilder.pos(x_size, -y_size, z_size).tex(maxU, minV).endVertex();
		bufferbuilder.pos(x_size, -y_size, -z_size).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(x_size, y_size, -z_size).tex(minU, maxV).endVertex();
		bufferbuilder.pos(x_size, y_size, z_size).tex(minU, minV).endVertex();

		// RIGHT
		bufferbuilder.pos(-x_size, -y_size, -z_size).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-x_size, -y_size, z_size).tex(minU, minV).endVertex();
		bufferbuilder.pos(-x_size, y_size, z_size).tex(maxU, minV).endVertex();
		bufferbuilder.pos(-x_size, y_size, -z_size).tex(maxU, maxV).endVertex();

		// BACK
		bufferbuilder.pos(-x_size, -y_size, -z_size).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-x_size, y_size, -z_size).tex(minU, minV).endVertex();
		bufferbuilder.pos(x_size, y_size, -z_size).tex(maxU, minV).endVertex();
		bufferbuilder.pos(x_size, -y_size, -z_size).tex(maxU, maxV).endVertex();

		// FRONT
		bufferbuilder.pos(x_size, -y_size, z_size).tex(maxU, minV).endVertex();
		bufferbuilder.pos(x_size, y_size, z_size).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(-x_size, y_size, z_size).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-x_size, -y_size, z_size).tex(minU, minV).endVertex();

		tessellator.draw();

		GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
	}

	public static void drawSeamlessCuboid(float minU, float maxU, float minV, float maxV, double z_size, double y_size, double x_size, double scale) {

		GlStateManager.scale(scale, scale, scale);

		double hlfU = minU + (maxU - minU) / 2;
		double hlfV = minV + (maxV - minV) / 2;

		double centre = 0d;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		// UP
		bufferbuilder.pos(-x_size, y_size, -z_size).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(-x_size, y_size, z_size).tex(maxU, minV).endVertex();
		bufferbuilder.pos(x_size, y_size, z_size).tex(minU, minV).endVertex();
		bufferbuilder.pos(x_size, y_size, -z_size).tex(minU, maxV).endVertex();

		// DOWN
		bufferbuilder.pos(-x_size, -y_size, z_size).tex(minU, minV).endVertex();
		bufferbuilder.pos(-x_size, -y_size, -z_size).tex(minU, maxV).endVertex();
		bufferbuilder.pos(x_size, -y_size, -z_size).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(x_size, -y_size, z_size).tex(maxU, minV).endVertex();

		// LEFT
		bufferbuilder.pos(x_size, -y_size, z_size).tex(maxU, minV).endVertex();
		bufferbuilder.pos(x_size, -y_size, -z_size).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(x_size, y_size, -z_size).tex(minU, maxV).endVertex();
		bufferbuilder.pos(x_size, y_size, z_size).tex(minU, minV).endVertex();

		// RIGHT
		bufferbuilder.pos(-x_size, -y_size, -z_size).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-x_size, -y_size, z_size).tex(minU, minV).endVertex();
		bufferbuilder.pos(-x_size, y_size, z_size).tex(maxU, minV).endVertex();
		bufferbuilder.pos(-x_size, y_size, -z_size).tex(maxU, maxV).endVertex();

		// BACK BOTTOM
		bufferbuilder.pos(-x_size, -y_size, -z_size).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-x_size, centre, -z_size).tex(minU, hlfV).endVertex();
		bufferbuilder.pos(x_size, centre, -z_size).tex(maxU, hlfV).endVertex();
		bufferbuilder.pos(x_size, -y_size, -z_size).tex(maxU, maxV).endVertex();

		// BACK TOP
		bufferbuilder.pos(-x_size, centre, -z_size).tex(maxU, hlfV).endVertex();
		bufferbuilder.pos(-x_size, y_size, -z_size).tex(maxU, maxV).endVertex();
		bufferbuilder.pos(x_size, y_size, -z_size).tex(minU, maxV).endVertex();
		bufferbuilder.pos(x_size, centre, -z_size).tex(minU, hlfV).endVertex();

		// FRONT BOTTOM
		bufferbuilder.pos(x_size, -y_size, z_size).tex(maxU, minV).endVertex();
		bufferbuilder.pos(x_size, centre, z_size).tex(maxU, hlfV).endVertex();
		bufferbuilder.pos(-x_size, centre, z_size).tex(minU, hlfV).endVertex();
		bufferbuilder.pos(-x_size, -y_size, z_size).tex(minU, minV).endVertex();

		// FRONT TOP
		bufferbuilder.pos(x_size, centre, z_size).tex(minU, hlfV).endVertex();
		bufferbuilder.pos(x_size, y_size, z_size).tex(minU, minV).endVertex();
		bufferbuilder.pos(-x_size, y_size, z_size).tex(maxU, minV).endVertex();
		bufferbuilder.pos(-x_size, centre, z_size).tex(maxU, hlfV).endVertex();

		tessellator.draw();

		GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
	}

	public static void drawQuad(float minU, float maxU, float minV, float maxV, double x_size, double y_size, double scale) {
		GlStateManager.scale(scale, scale, scale);

		double hlfU = minU + (maxU - minU) / 2;
		double hlfV = minV + (maxV - minV) / 2;

		double centre = 0d;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		// BACK
		bufferbuilder.pos(-x_size, -y_size, -1).tex(minU, maxV).endVertex();
		bufferbuilder.pos(-x_size, y_size, -1).tex(minU, minV).endVertex();
		bufferbuilder.pos(x_size, y_size, -1).tex(maxU, minV).endVertex();
		bufferbuilder.pos(x_size, -y_size, -1).tex(maxU, maxV).endVertex();

		tessellator.draw();

		GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
	}

	public static void drawLightning(final int number, final int NumberOfBranches, final int NumberOfPossibleSubBranches, final double scale) {
		GlStateManager.depthMask(true);
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		final double scale16 = scale / 16;

		double[] translateXArray = new double[8];
		double[] translateZArray = new double[8];
		double tempX = 0.0D;
		double tempZ = 0.0D;
		Random random = new Random(number);

		for (int counter_ = 7; counter_ >= 0; --counter_) {
			translateXArray[counter_] = tempX;
			translateZArray[counter_] = tempZ;
			tempX += random.nextInt(11) - 5;
			tempZ += random.nextInt(11) - 5;
		}

		for (int shells = 0; shells < 4; ++shells) {
			Random random1 = new Random(number);
			for (int branches = 0; branches < NumberOfBranches; branches++) {
				for (int possibleSubBranches = 0; possibleSubBranches < NumberOfPossibleSubBranches + 1; ++possibleSubBranches) {
					int position = 7;
					int decendingHeight = 0;

					if (possibleSubBranches > 0) {
						position = 7 - possibleSubBranches;
					}

					if (possibleSubBranches > 0) {
						decendingHeight = position - 2;
					}

					double topTranslateX = translateXArray[position];
					double topTranslateZ = translateZArray[position];

					for (int yPos = position; yPos >= decendingHeight; --yPos) {
						double bottomTranslateX = topTranslateX;
						double bottomTranslateZ = topTranslateZ;

						if (possibleSubBranches == 0) { // Main branch
							topTranslateX += random1.nextInt(11) - 5;
							topTranslateZ += random1.nextInt(11) - 5;
						} else {// Sub branch
							topTranslateX += random1.nextInt(31) - 15;
							topTranslateZ += random1.nextInt(31) - 15;
						}

						bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
						double topWidth = 0.1D + shells * 0.2D;

						if (yPos == 0) {
							topWidth *= yPos * 0.1D + 1.0D;
						}

						double bottomWidth = 0.1D + shells * 0.2D;

						if (yPos == 0) {
							bottomWidth *= (yPos - 1) * 0.1D + 1.0D;
						}

						topWidth *= (scale / 16);
						bottomWidth *= (scale / 16);

						for (int side = 0; side < 5; ++side) {
							double topOffsetX = -topWidth;
							double topOffsetZ = -topWidth;

							if (side == 1 || side == 2) {
								topOffsetX += topWidth * 2.0D;
							}

							if (side == 2 || side == 3) {
								topOffsetZ += topWidth * 2.0D;
							}

							double bottomOffsetX = -bottomWidth;
							double bottomOffsetZ = -bottomWidth;

							if (side == 1 || side == 2) {
								bottomOffsetX += bottomWidth * 2.0D;
							}

							if (side == 2 || side == 3) {
								bottomOffsetZ += bottomWidth * 2.0D;
							}

							bufferbuilder.pos(bottomOffsetX + topTranslateX * scale16, yPos * scale, bottomOffsetZ + topTranslateZ * scale16).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
							bufferbuilder.pos(topOffsetX + bottomTranslateX * scale16, (yPos + 1) * scale, topOffsetZ + bottomTranslateZ * scale16).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
						}

						tessellator.draw();

					}
				}
			}

		}

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
	}

	public static Position getEntityRenderPos(final Entity entity, final double partialTicks) {
		double flyingMultiplier = 1.825;
		double yFlying = 1.02;
		double yAdd = 0.0784000015258789;

		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying) {
			flyingMultiplier = 1.1;
			yFlying = 1.67;
			yAdd = 0;
		}

		double yGround = entity.motionY + yAdd == 0 && entity.prevPosY > entity.posY ? entity.posY - entity.prevPosY : 0;
		double xFall = 1;
		if (flyingMultiplier == 1.825)
			if (entity.motionX != 0) {
				if (entity.motionY + yAdd != 0) {
					xFall = 0.6;
				} else if (yGround != 0) {
					xFall = 0.6;
				}
			} else {
				xFall = 0.6;
			}

		double zFall = 1;
		if (flyingMultiplier == 1.825)
			if (entity.motionZ != 0) {
				if (entity.motionY + yAdd != 0) {
					zFall = 0.6;
				} else if (yGround != 0) {
					zFall = 0.6;
				}
			} else {
				zFall = 0.6;
			}

		double dX = entity.posX - (entity.prevPosX - entity.posX) * partialTicks - (entity.motionX * xFall) * flyingMultiplier;
		double dY = entity.posY - yGround - (entity.prevPosY - entity.posY) * partialTicks - (entity.motionY + yAdd) * yFlying;
		double dZ = entity.posZ - (entity.prevPosZ - entity.posZ) * partialTicks - (entity.motionZ * zFall) * flyingMultiplier;

		return new Position(dX, dY, dZ);
	}

	public static void rotateTowardsPos(Position origin, Position destination) {
		double yAngle = Math.atan2(origin.getX() - destination.getX(), origin.getZ() - destination.getZ());
		yAngle = yAngle * (180 / Math.PI);
		yAngle = yAngle < 0 ? 360 - (-yAngle) : yAngle;
		GlStateManager.rotate((float) yAngle + 90, 0, 1, 0);

		double _Angle = Math.atan2(destination.getY(), Math.sqrt(destination.getX() * destination.getX() + destination.getZ() * destination.getZ()));
		_Angle = _Angle * (180 / Math.PI);
		_Angle = _Angle < 0 ? 360 - (-_Angle) : _Angle;
		GlStateManager.rotate(90 - (float) _Angle, 0, 0, 1);
	}

	public static void rotateHorizontal(EnumFacing facing) {
		GlStateManager.rotate(facing == EnumFacing.DOWN ? 0 : facing == EnumFacing.UP ? 180F : facing == EnumFacing.NORTH || facing == EnumFacing.EAST ? 90F : -90F, facing.getAxis() == EnumFacing.Axis.Z ? 1 : 0, 0,
				facing.getAxis() == EnumFacing.Axis.Z ? 0 : 1);
		GlStateManager.rotate(-90, 0, 0, 1);
	}

	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return (mouseX >= x) && (mouseX <= x + xSize) && (mouseY >= y) && (mouseY <= y + ySize);
	}

	public static void renderStack(ItemStack stack, World world) {
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
	}

	public static void renderStackWithoutTransforms(ItemStack stack, World world) {
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
	}

	public static void renderStackWithColor(ItemStack stack, World world, int color) {

		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);

		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values()) {
			renderQuadsColor(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), color, stack);
		}

		renderQuadsColor(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color, stack);
		tessellator.draw();

		GlStateManager.popMatrix();
	}

	private static void renderQuadsColor(BufferBuilder bufferbuilder, List<BakedQuad> quads, int color, ItemStack stack) {

		int i = 0;
		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);

			if (bakedquad.hasTintIndex()) {
				if (EntityRenderer.anaglyphEnable) {
					color = TextureUtil.anaglyphColor(color);
				}

				color = color | -16777216;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(bufferbuilder, bakedquad, color);
		}
	}

}
