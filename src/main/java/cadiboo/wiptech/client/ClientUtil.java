package cadiboo.wiptech.client;

import java.util.Random;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class ClientUtil {

	/**
	 * Rotation algorithm Taken off Max_the_Technomancer from <a href=
	 * "https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2772267-tesr-getting-darker-and-lighter-as-it-rotates">here</a>
	 * 
	 * @param face the {@link net.minecraft.util.EnumFacing face} to rotate for
	 */
	public static void rotateForFace(EnumFacing face) {
		GlStateManager.rotate(face == EnumFacing.DOWN ? 0 : face == EnumFacing.UP ? 180F : face == EnumFacing.NORTH || face == EnumFacing.EAST ? 90F : -90F,
				face.getAxis() == EnumFacing.Axis.Z ? 1 : 0, 0, face.getAxis() == EnumFacing.Axis.Z ? 0 : 1);
		GlStateManager.rotate(-90, 0, 0, 1);
	}

	/**
	 * All Light methods I can remember
	 * 
	 * @author Cadiboo
	 */
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

	/**
	 * Modifed by Cadiboo, based on Notch?'s lightning code, cleaned it up, made it
	 * readable & used pushMatrix, popMatrix & translate instead of drawing it
	 * straight onto the screen
	 * 
	 * @param number                      The seed for the randoms that are used
	 * @param NumberOfBranches            The number of main (full length) branches
	 * @param NumberOfPossibleSubBranches The number Sub branches for each main
	 *                                    branch
	 * @param scale                       How large/small it will render
	 * @author Cadiboo
	 */
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

	public static Vec3d getEntityRenderPos(final Entity entity, final double partialTicks) {
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

	public static void rotateTowardsPos(Vec3d origin, Vec3d destination) {
		double yAngle = Math.atan2(destination.x - origin.x, destination.z - origin.z);
		yAngle = yAngle * (180 / Math.PI);
		yAngle = yAngle < 0 ? 360 - (-yAngle) : yAngle;
		GlStateManager.rotate((float) yAngle + 90, 0, 1, 0);

		double _Angle = Math.atan2(destination.y, Math.sqrt(destination.x * destination.x + destination.z * destination.z));
		_Angle = _Angle * (180 / Math.PI);
		_Angle = _Angle < 0 ? 360 - (-_Angle) : _Angle;
		GlStateManager.rotate(90 - (float) _Angle, 0, 0, 1);
	}

	public static void renderElectricity(final int number, final int NumberOfBranches, double scale) {
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

}
