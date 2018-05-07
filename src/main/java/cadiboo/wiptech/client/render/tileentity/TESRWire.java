package cadiboo.wiptech.client.render.tileentity;

import java.util.List;
import java.util.Random;

import javax.vecmath.Vector3d;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.client.model.ModelEnamel;
import cadiboo.wiptech.client.model.ModelWire;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.Reference;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

//Rotation algorithm Taken off Max_the_Technomancer from
// www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2772267-tesr-getting-darker-and-lighter-as-it-rotates

public class TESRWire extends TileEntitySpecialRenderer<TileEntityWire> {

	private static final ResourceLocation ENAMEL_TEXTURE = new ResourceLocation("minecraft", "textures/blocks/concrete_brown.png");

	private static final float	ONE_SIXTEENTH		= 0.0625F;
	private static final float	SEVEN_SIXTEENTHS	= 0.4375F;

	private static final ModelWire	DOWN_MODEL_WIRE		= new ModelWire();
	private static final ModelWire	UP_MODEL_WIRE		= new ModelWire();
	private static final ModelWire	NORTH_MODEL_WIRE	= new ModelWire();
	private static final ModelWire	SOUTH_MODEL_WIRE	= new ModelWire();
	private static final ModelWire	WEST_MODEL_WIRE		= new ModelWire();
	private static final ModelWire	EAST_MODEL_WIRE		= new ModelWire();

	private static final ModelEnamel	DOWN_MODEL_ENAMEL	= new ModelEnamel();
	private static final ModelEnamel	UP_MODEL_ENAMEL		= new ModelEnamel();
	private static final ModelEnamel	NORTH_MODEL_ENAMEL	= new ModelEnamel();
	private static final ModelEnamel	SOUTH_MODEL_ENAMEL	= new ModelEnamel();
	private static final ModelEnamel	WEST_MODEL_ENAMEL	= new ModelEnamel();
	private static final ModelEnamel	EAST_MODEL_ENAMEL	= new ModelEnamel();

	@Override
	public void render(TileEntityWire tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (!(tileEntity.getBlockType() instanceof BlockWire)) {
			WIPTech.logger.error("WIRE RENDERING ERROR! BLOCK IS NOT WIRE");
			return;
		}
		final ResourceLocation texLoc = new ResourceLocation(Reference.ID, "textures/items/" + ((BlockWire) tileEntity.getBlockType()).getMetal().getName() + "_wire.png");
		final boolean isEnamel = ((BlockWire) tileEntity.getBlockType()).isEnamel();
		this.bindTexture(texLoc);

		GlStateManager.depthMask(false);

		if (tileEntity.electrocutionTime > 0) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

			double dX;
			double dY;
			double dZ;
			final int NumberOfBranches = 10;
			final int NumberOfPossibleSubBranches = 5;

			List<Entity> entities = tileEntity.getAllEntitiesWithinRangeAt(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), 113);
			for (int i = 0; i < entities.size(); i++) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

				double flyingMultiplier = 1.825;
				double yFlying = 1.02;
				double yAdd = 0.0784000015258789;

				if (entities.get(i) instanceof EntityPlayer) {
					if (((EntityPlayer) entities.get(i)).capabilities.isFlying) {
						flyingMultiplier = 1.1;
						yFlying = 1.67;
						yAdd = 0;
					}
				}

				double yGround = entities.get(i).motionY + yAdd == 0 && entities.get(i).prevPosY > entities.get(i).posY ? entities.get(i).posY - entities.get(i).prevPosY : 0;
				double xFall = 1;
				if (flyingMultiplier == 1.825) {
					if (entities.get(i).motionX != 0) {
						if (entities.get(i).motionY + yAdd != 0) {
							xFall = 0.6;
						} else if (yGround != 0) {
							xFall = 0.6;
						}
					} else {
						xFall = 0.6;
					}
				}

				double zFall = 1;
				if (flyingMultiplier == 1.825) {
					if (entities.get(i).motionZ != 0) {
						if (entities.get(i).motionY + yAdd != 0) {
							zFall = 0.6;
						} else if (yGround != 0) {
							zFall = 0.6;
						}
					} else {
						zFall = 0.6;
					}
				}

				dX = entities.get(i).posX - (entities.get(i).prevPosX - entities.get(i).posX) * partialTicks - (entities.get(i).motionX * xFall) * flyingMultiplier - tileEntity.getPos().getX() - 0.5;
				dY = entities.get(i).posY - yGround - (entities.get(i).prevPosY - entities.get(i).posY) * partialTicks - (entities.get(i).motionY + yAdd) * yFlying - tileEntity.getPos().getY() - 0.5;
				dZ = entities.get(i).posZ - (entities.get(i).prevPosZ - entities.get(i).posZ) * partialTicks - (entities.get(i).motionZ * zFall) * flyingMultiplier - tileEntity.getPos().getZ() - 0.5;
				dY += entities.get(i).getEyeHeight();

				float fX = (float) dX;
				float fY = (float) dY;
				float fZ = (float) dZ;

				double yAngle = Math.atan2(0 - dX, 0 - dZ);
				yAngle = yAngle * (180 / Math.PI);
				yAngle = yAngle < 0 ? 360 - (-yAngle) : yAngle;
				GlStateManager.rotate((float) yAngle + 90, 0, 1, 0);
				GlStateManager.rotate(180, 1, 0, 0);
				// GlStateManager.rotate(90, 0, 0, 1);

				double zAngle = Math.atan2(0 - dY, 0 - dX);
				zAngle = zAngle * (180 / Math.PI);
				zAngle = zAngle < 0 ? 360 - (-zAngle) : zAngle;
				// GlStateManager.rotate((float) zAngle + 270, 0, 0, 1);

				double xAngle = Math.atan2(0 - dZ, 0 - dY);
				xAngle = xAngle * (180 / Math.PI);
				xAngle = xAngle < 0 ? 360 - (-xAngle) : xAngle;
				// GlStateManager.rotate((float) xAngle, 1, 0, 0);

				Vector3d vec = new Vector3d(dX, dY, dZ);

				double _Angle = Math.atan2(dY, Math.sqrt(dX * dX + dZ * dZ));
				_Angle = _Angle * (180 / Math.PI);
				_Angle = _Angle < 0 ? 360 - (-_Angle) : _Angle;

				GlStateManager.rotate(90 - (float) _Angle, 0, 0, 1);

				// Math.cos(z / 2) = Math.cos(x/2) * Math.cos(y/2) − sin(x/2) * sin(b/2);

				// final double scale = 0.0625 * Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) +
				// Math.pow(dZ, 2));
				// final double scale = 0.5;// 0.125 * Math.sqrt(Math.pow(dX, 2) + Math.pow(dY,
				// 2) + Math.pow(dZ, 2));
				final double scale = 0.125 * Math.sqrt(dX * dX + dY * dY + dZ * dZ);
				// GlStateManager.translate(0, -(Math.sqrt(dX * dX + dZ * dZ) + 0.5) / 2, 0);
				// Utils.drawCuboid(0, 1, 0, 1, 0.01, (Math.sqrt(dX * dX + dZ * dZ) + 0.5) / 2,
				// 0.01, 1);

				// GlStateManager.translate(0, -Math.sqrt(dX * dX + dY * dY + dZ * dZ) / 2, 0);
				// Utils.drawCuboid(0, 1, 0, 1, 0.1, Math.sqrt(dX * dX + dY * dY + dZ * dZ) / 2,
				// 0.1, 1);

				// Utils.drawCuboid(0, 1, 0, 1, 1, 5, 1, 1);
				final double scale16 = scale / 16;
				GlStateManager.translate(-3.5 * scale / 16, -8 * scale, -4 * scale / 16);
				for (int shells = 0; shells < 5; ++shells) {
					Random random1 = new Random(getWorld().getTotalWorldTime());
					// random1 = new Random();
					for (int branches = 0; branches < NumberOfBranches; branches++) {
						// random1 = new Random(getWorld().getTotalWorldTime() + branches);
						// 2, 3, 4, 5, 6, 11, 16, 17, 19, 20, 21, 23, 25, 29, 40, 41, 42
						random1 = new Random(2);
						for (int possibleSubBranches = 0; possibleSubBranches < NumberOfPossibleSubBranches + 1; ++possibleSubBranches) {
							int position = 7;
							int decendingHeight = 0;

							if (possibleSubBranches > 0) {
								position = 7 - possibleSubBranches;
							}

							if (possibleSubBranches > 0) {
								decendingHeight = position - 2;
							}

							double topTranslateX = 2;
							double topTranslateZ = 4;

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
									double topOffsetX = topWidth;
									double topOffsetZ = topWidth;

									if (side == 1 || side == 2) {
										topOffsetX += topWidth * 2.0D;
									}

									if (side == 2 || side == 3) {
										topOffsetZ += topWidth * 2.0D;
									}

									double bottomOffsetX = bottomWidth;
									double bottomOffsetZ = bottomWidth;

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
				GlStateManager.popMatrix();
			}

			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
		}
		GlStateManager.depthMask(true);

		sides: for (int i = 0; i < EnumFacing.VALUES.length; i++) {
			final EnumFacing side = EnumFacing.VALUES[i];
			if (!tileEntity.isConnectedTo(side))
				continue sides;
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
			GlStateManager.rotate(side == EnumFacing.DOWN ? 0 : side == EnumFacing.UP ? 180F : side == EnumFacing.NORTH || side == EnumFacing.EAST ? 90F : -90F,
					side.getAxis() == EnumFacing.Axis.Z ? 1 : 0, 0, side.getAxis() == EnumFacing.Axis.Z ? 0 : 1);
			GlStateManager.rotate(-90, 0, 0, 1);

			if (isEnamel) {
				this.bindTexture(ENAMEL_TEXTURE);
				DOWN_MODEL_ENAMEL.render(ONE_SIXTEENTH);
				if (!tileEntity.isConnectedToWire(side))
					GlStateManager.translate(SEVEN_SIXTEENTHS, 0, 0);
				this.bindTexture(texLoc);
			}
			DOWN_MODEL_WIRE.render(ONE_SIXTEENTH);
			GlStateManager.popMatrix();
		}
	}

	// private static Matrix4d createMat4(final Vector3d pos, final Vector3d lookat)
	// {
	// Vector3d vz = lookat. - pos;
	// vz.normalize();
	// Vector3d vx = Vector3::cross(Vector3( 0, 1, 0 ), vz);
	// vx.normalize();
	// Vector3 vy = Vector3::cross(vz, vx);
	//
	// Matrix4 rotation ( vx.x, vy.x, vz.x, 0,
	// vx.y, vy.y, vz.y, 0,
	// vx.z, vy.z, vz.z, 0,
	// 0, 0, 0, 1);
	// return rotation;
	// }

	// public void zoomOn(double x, double y, double z, Vecteur direction) {
	// Transform3D viewTrans = new Transform3D();
	// if (direction == null) {
	// return;
	// }
	// if (direction.norme() == 0) {
	// return;
	// }
	// // point the view at the center of the object
	// Point3d center = new Point3d(x + this.translate.x, y + this.translate.y, z +
	// this.translate.z);
	// Point3d eyePos = new Point3d(x + this.translate.x + direction.getX(), y +
	// this.translate.y
	// + direction.getY(), z + this.translate.z + direction.getZ());
	// viewTrans.setIdentity();
	// viewTrans.lookAt(eyePos, center, new Vector3d(0, 0, 1));
	// // set the view transform
	// viewTrans.invert();
	// InterfaceMap3D.tgvu.setTransform(viewTrans);
	// }
}
