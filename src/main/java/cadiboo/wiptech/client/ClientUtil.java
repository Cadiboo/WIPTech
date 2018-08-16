package cadiboo.wiptech.client;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import cadiboo.wiptech.WIPTech;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

/**
 * Util that is only used on the client i.e. Rendering code
 * 
 * 
 *
 * @author Cadiboo
 */
public class ClientUtil {

	/**
	 * Rotation algorithm Taken off Max_the_Technomancer from <a href=
	 * "https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2772267-tesr-getting-darker-and-lighter-as-it-rotates">here</a>
	 * 
	 * @param face the {@link net.minecraft.util.EnumFacing face} to rotate for
	 * 
	 * 
	 * @author Cadiboo
	 */
	public static final void rotateForFace(EnumFacing face) {
		GlStateManager.rotate(face == EnumFacing.DOWN ? 0 : face == EnumFacing.UP ? 180F : face == EnumFacing.NORTH || face == EnumFacing.EAST ? 90F : -90F, face.getAxis() == EnumFacing.Axis.Z ? 1
				: 0, 0, face.getAxis() == EnumFacing.Axis.Z ? 0 : 1);
		GlStateManager.rotate(-90, 0, 0, 1);
	}

	/**
	 * All Light methods I can remember
	 * 
	 * 
	 * @author Cadiboo
	 */
	private static final void allLightMethods() {

//	GlStateManager.disableLighting();
//	GlStateManager.enableLighting();
//
//	RenderHelper.disableStandardItemLighting();
//	RenderHelper.enableStandardItemLighting();
//
//	RenderHelper.enableGUIStandardItemLighting();
//
//	net.minecraftforge.client.model.pipeline.LightUtil.diffuseLight(EnumFacing side);
//	net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(BufferBuilder wr, BakedQuad quad, int auxColor);
//	
//	Tessellator.getInstance().getBuffer().lightmap(p_187314_1_, p_187314_2_);
//	
//	OpenGlHelper.setLightmapTextureCoords(int target, float p_77475_1_, float t)

	}

	/**
	 * Modifed by Cadiboo, based on Notch?'s lightning code. Cleaned it up, made it
	 * readable & used pushMatrix, popMatrix & translate instead of drawing it
	 * straight onto the screen
	 * 
	 * @param number                      The seed for the randoms that are used
	 * @param NumberOfBranches            The number of main (full length) branches
	 * @param NumberOfPossibleSubBranches The number Sub branches for each main
	 *                                    branch
	 * @param scale                       How large/small it will render
	 * 
	 * @author Cadiboo
	 */
	public static final void renderLightning(final int number, final int NumberOfBranches, final int NumberOfPossibleSubBranches, final double scale) {
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
						position = Math.abs((7 - possibleSubBranches) % translateXArray.length);
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

	/**
	 * Put a lot of effort into this, it gets the entities exact (really, really
	 * exact) position
	 * 
	 * @param entity       The entity to calculate the position of
	 * @param partialTicks The multiplier used to predict where the entity is/will
	 *                     be
	 * 
	 * @return The position of the entity as a Vec3d
	 * 
	 * @author Cadiboo
	 */
	public static final Vec3d getEntityRenderPos(final Entity entity, final double partialTicks) {
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

		return new Vec3d(dX, dY, dZ);
	}

	/**
	 * 
	 * @param source      The position to rotate from (I think that I've messed up
	 *                    the maths, currently only works with 0, 0, 0 i.e. the
	 *                    Origin)
	 * @param destination The position to rotate towards
	 * @author Cadiboo
	 */
	public static final void rotateTowardsPos(Vec3d source, Vec3d destination) {
		double yAngle = Math.atan2(destination.x - source.x, destination.z - source.z);
		yAngle = yAngle * (180 / Math.PI);
		yAngle = yAngle < 0 ? 360 - (-yAngle) : yAngle;
		GlStateManager.rotate((float) yAngle + 90, 0, 1, 0);

		double xzAngle = Math.atan2(destination.y, Math.sqrt(destination.x * destination.x + destination.z * destination.z));
		xzAngle = xzAngle * (180 / Math.PI);
		xzAngle = xzAngle < 0 ? 360 - (-xzAngle) : xzAngle;
		GlStateManager.rotate(90 - (float) xzAngle, 0, 0, 1);
	}

	public static final void rotateForPitchYaw(double pitch, double yaw) {
		WIPTech.info("rotateForPitchYaw isn\'t implemented yet");
	}

	/**
	 * 
	 * Modified version of {@link ClientUtil#renderLightning(int, int, int, double)
	 * renderLightning} that looks more electricity-arcy
	 * 
	 * @param number           The seed for the randoms that are used
	 * @param NumberOfBranches The number of main (full length) branches
	 * @param scale            How large/small/long it will be
	 * 
	 * 
	 * @author Cadiboo
	 */
	public static final void renderElectricity(final int number, final int NumberOfBranches, double scale) {
		GlStateManager.depthMask(true);
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		final double scale16 = scale / 16;

		int NumberOfPossibleSubBranches = 3;

		double[] translateXArray = new double[8];
		double[] translateZArray = new double[8];
		double tempX = 0.0D;
		double tempZ = 0.0D;
		Random random = new Random(number);

		for (int counter_ = 7; counter_ >= 0; --counter_) {
			translateXArray[counter_] = tempX;
			translateZArray[counter_] = tempZ;

			tempX += random.nextInt(10) * 0.1;
			tempZ += random.nextInt(10) * 0.1;
			tempX *= -3 * -counter_ * 0.11;
			tempZ *= -3 * -counter_ * 0.11;

			if (new Random(counter_ - 1).nextBoolean()) {
				tempX *= -1;
				tempZ *= -1;
			}
		}

		for (int shells = 0; shells < 4; ++shells) {
			Random random1 = new Random(number);
			for (int branches = 0; branches < NumberOfBranches; branches++) {
				for (int possibleSubBranches = 0; possibleSubBranches < NumberOfPossibleSubBranches + 1; ++possibleSubBranches) {
					int position = 7;
					int decendingHeight = 0;

					if (possibleSubBranches > 0) {
						position = Math.abs((7 - possibleSubBranches) % translateXArray.length);
					}

					if (possibleSubBranches > 0) {
						decendingHeight = position - 2;
					}

					double topTranslateX = translateXArray[position];
					double topTranslateZ = translateZArray[position];

					for (int yPos = position; yPos >= decendingHeight; --yPos) {
						double bottomTranslateX = topTranslateX;
						double bottomTranslateZ = topTranslateZ;

//						if (possibleSubBranches == 0) { // Main branch
						topTranslateX += random1.nextInt(10) * 0.1;
						topTranslateZ += random1.nextInt(10) * 0.1;
						topTranslateX *= -3 * yPos * 0.11;
						topTranslateZ *= -3 * yPos * 0.11;

						if (new Random(possibleSubBranches - 1).nextBoolean()) {
							topTranslateX *= -1;
							topTranslateZ *= -1;
						}

						if (possibleSubBranches != 0) {// Sub branch
							topTranslateX += random1.nextInt(9) - 3;
							topTranslateZ += random1.nextInt(9) - 3;
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

	/**
	 * Draws a cuboid similar to the way blocks are drawn
	 * 
	 * 
	 * @param minU   Minimum texture U (x) to draw from (between 0 and 1)
	 * @param maxU   Maximum texture U (x) to draw from (between 0 and 1)
	 * @param minV   Minimum texture V (y) to draw from (between 0 and 1)
	 * @param maxV   Maximum texture V (y) to draw from (between 0 and 1)
	 * @param x_size The size of the cuboid on the X plane
	 * @param y_size The size of the cuboid on the Y plane
	 * @param z_size The size of the cuboid on the Z plane
	 * @param scale  How much to scale in
	 *               {@link net.minecraft.client.renderer.GlStateManager#scale(double, double, double)
	 *               GLStateManager.scale}
	 * @author Cadiboo
	 */
	public static final void drawCuboid(float minU, float maxU, float minV, float maxV, double x_size, double y_size, double z_size, double scale) {

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

	/**
	 * 
	 * Draws a cuboid that has its texture always be seamless
	 * 
	 * 
	 * @param minU   Minimum texture U (x) to draw from (between 0 and 1)
	 * @param maxU   Maximum texture U (x) to draw from (between 0 and 1)
	 * @param minV   Minimum texture V (y) to draw from (between 0 and 1)
	 * @param maxV   Maximum texture V (y) to draw from (between 0 and 1)
	 * @param x_size The size of the cuboid on the X plane
	 * @param y_size The size of the cuboid on the Y plane
	 * @param z_size The size of the cuboid on the Z plane
	 * @param scale  How much to scale in
	 *               {@link net.minecraft.client.renderer.GlStateManager#scale(double, double, double)
	 *               GLStateManager.scale}
	 * @author Cadiboo
	 */
	public static final void drawSeamlessCuboid(float minU, float maxU, float minV, float maxV, double z_size, double y_size, double x_size, double scale) {

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

	/**
	 * Draws a part of a texture
	 * 
	 * @param minU   Minimum texture U (x) to draw from (between 0 and 1)
	 * @param maxU   Maximum texture U (x) to draw from (between 0 and 1)
	 * @param minV   Minimum texture V (y) to draw from (between 0 and 1)
	 * @param maxV   Maximum texture V (y) to draw from (between 0 and 1)
	 * @param x_size The size of the cuboid on the X plane
	 * @param y_size The size of the cuboid on the Y plane
	 * @param scale  How much to scale in
	 *               {@link net.minecraft.client.renderer.GlStateManager#scale(double, double, double)
	 *               GLStateManager.scale}
	 * @author Cadiboo
	 */
	public static final void drawQuad(float minU, float maxU, float minV, float maxV, double x_size, double y_size, double scale) {
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

	/**
	 * Renders an {@link ItemStack} with the ground camera transform
	 * 
	 * @param stack
	 * @param world
	 * @author Cadiboo
	 */
	public static final void renderStack(ItemStack stack, World world) {
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
	}

	/**
	 * Renders an {@link ItemStack} without any camera transforms
	 * 
	 * @param stack
	 * @param world
	 * @author Cadiboo
	 */
	public static final void renderStackWithoutTransforms(ItemStack stack, World world) {
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
	}

	/**
	 * Renders an {@link ItemStack} with the specified {@link IBakedModel} with the
	 * specified color
	 * 
	 * @param stack
	 * @param model
	 * @param color
	 * @author Cadiboo
	 */
	public static final void renderStackWithColor(ItemStack stack, IBakedModel model, int color) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);

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

	/**
	 * Renders a list of quads with the specified color
	 * 
	 * @param bufferbuilder
	 * @param quads
	 * @param color
	 * @param stack
	 * @author Cadiboo
	 */
	private static final void renderQuadsColor(BufferBuilder bufferbuilder, List<BakedQuad> quads, int color, ItemStack stack) {

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

	/**
	 * gets the model from a stack with overrides
	 * 
	 * @param stack
	 * @param world
	 * @return The stack's model
	 * 
	 * @author Cadiboo
	 */
	public static final IBakedModel getModelFromStack(ItemStack stack, World world) {
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);
		return model;
	}

	/**
	 * Sets lightmap texture coords to brightest possbile
	 */
	public static final void enableMaxLighting() {
		int i = 15728880;

		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

}
