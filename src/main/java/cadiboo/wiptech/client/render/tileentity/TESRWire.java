package cadiboo.wiptech.client.render.tileentity;

import java.util.List;
import java.util.Random;

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

	private static final int[] RANDOMS = new int[] { 2, 3, 4, 5, 6, 11, 16, 17, 19, 20, 21, 23, 25, 29, 40, 41, 42 };

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

			List<Entity> entities = tileEntity.getAllEntitiesWithinRangeAt(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), 3);
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

				double dX = entities.get(i).posX - (entities.get(i).prevPosX - entities.get(i).posX) * partialTicks - (entities.get(i).motionX * xFall) * flyingMultiplier - tileEntity.getPos().getX()
						- 0.5;
				double dY = entities.get(i).posY - yGround - (entities.get(i).prevPosY - entities.get(i).posY) * partialTicks - (entities.get(i).motionY + yAdd) * yFlying - tileEntity.getPos().getY()
						- 0.5;
				double dZ = entities.get(i).posZ - (entities.get(i).prevPosZ - entities.get(i).posZ) * partialTicks - (entities.get(i).motionZ * zFall) * flyingMultiplier - tileEntity.getPos().getZ()
						- 0.5;
				dY += entities.get(i).getEyeHeight();

				double yAngle = Math.atan2(0 - dX, 0 - dZ);
				yAngle = yAngle * (180 / Math.PI);
				yAngle = yAngle < 0 ? 360 - (-yAngle) : yAngle;
				// GlStateManager.rotate((float) yAngle + 90, 0, 1, 0);
				// GlStateManager.rotate(180, 1, 0, 0);

				double _Angle = Math.atan2(dY, Math.sqrt(dX * dX + dZ * dZ));
				_Angle = _Angle * (180 / Math.PI);
				_Angle = _Angle < 0 ? 360 - (-_Angle) : _Angle;
				// GlStateManager.rotate(90 - (float) _Angle, 0, 0, 1);

				// final double scale = 0.125 * Math.sqrt(dX * dX + dY * dY + dZ * dZ);
				final double scale = 16;
				final double scale16 = scale / 16;

				// GlStateManager.translate(-3.5 * scale / 16, -8 * scale, -4 * scale / 16);

				Random random = new Random(RANDOMS[(int) (getWorld().getTotalWorldTime() % RANDOMS.length)]);

				final int NumberOfBranches = 1;
				final int NumberOfPossibleSubBranches = 2;

				double[] adouble = new double[8];
				double[] adouble1 = new double[8];
				double d0 = 0.0D;
				double d1 = 0.0D;

				for (int __ = 7; __ >= 0; --__) {
					adouble[__] = d0;
					adouble1[__] = d1;
					d0 += random.nextInt(11) - 5;
					d1 += random.nextInt(11) - 5;
				}

				for (int k1 = 0; k1 < 4; ++k1) {
					Random random1 = random;

					for (int j = 0; j < 3; ++j) {
						int k = 7;
						int l = 0;

						if (j > 0) {
							k = 7 - j;
						}

						if (j > 0) {
							l = k - 2;
						}

						double d2 = adouble[k] - d0;
						double d3 = adouble1[k] - d1;

						for (int i1 = k; i1 >= l; --i1) {
							double d4 = d2;
							double d5 = d3;

							if (j == 0) {
								d2 += random1.nextInt(11) - 5;
								d3 += random1.nextInt(11) - 5;
							} else {
								d2 += random1.nextInt(31) - 15;
								d3 += random1.nextInt(31) - 15;
							}

							bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
							float f = 0.5F;
							float f1 = 0.45F;
							float f2 = 0.45F;
							float f3 = 0.5F;
							double d6 = 0.1D + k1 * 0.2D;

							if (j == 0) {
								d6 *= i1 * 0.1D + 1.0D;
							}

							double d7 = 0.1D + k1 * 0.2D;

							if (j == 0) {
								d7 *= (i1 - 1) * 0.1D + 1.0D;
							}

							for (int j1 = 0; j1 < 5; ++j1) {
								double d8 = x + 0.5D - d6;
								double d9 = z + 0.5D - d6;

								if (j1 == 1 || j1 == 2) {
									d8 += d6 * 2.0D;
								}

								if (j1 == 2 || j1 == 3) {
									d9 += d6 * 2.0D;
								}

								double d10 = x + 0.5D - d7;
								double d11 = z + 0.5D - d7;

								if (j1 == 1 || j1 == 2) {
									d10 += d7 * 2.0D;
								}

								if (j1 == 2 || j1 == 3) {
									d11 += d7 * 2.0D;
								}

								bufferbuilder.pos(d10 + d2, y + i1 * 16, d11 + d3).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
								bufferbuilder.pos(d8 + d4, y + (i1 + 1) * 16, d9 + d5).color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
							}

							tessellator.draw();
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
				this.bindTexture(texLoc);
			}
			if (!tileEntity.isConnectedToWire(side))
				GlStateManager.translate(SEVEN_SIXTEENTHS, 0, 0);
			DOWN_MODEL_WIRE.render(ONE_SIXTEENTH);
			GlStateManager.popMatrix();
		}
	}
}
