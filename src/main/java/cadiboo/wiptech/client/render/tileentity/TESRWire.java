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
	public void render(TileEntityWire tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
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
			GlStateManager.pushMatrix();
			// GlStateManager.translate(x, y, z);
			// GlStateManager.translate(0, 10, 0);

			double dX;
			double dY;
			double dZ;
			double scale = 16;
			int NumberOfBranches = 1;
			int NumberOfPossibleSubBranches = 2;

			GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
			// GlStateManager.rotate(getWorld().getWorldTime(), 0, 0, 1);
			GlStateManager.rotate(180, 1, 0, 0);
			GlStateManager.translate(-3.5, -8 * scale, -4);

			List<Entity> entities = tileEntity.getAllEntitiesWithinRangeAt(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), 3);
			for (int i = 0; i < entities.size(); i++) {
				for (int shells = 0; shells < 5; ++shells) {
					Random random1 = new Random(getWorld().getTotalWorldTime());
					// random1 = new Random();
					// random1 = new Random(2);
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

							double topTranslateX = 2;
							double topTranslateZ = 4;

							for (int yPos = position; yPos >= decendingHeight; --yPos) {
								double bottomTranslateX = topTranslateX;
								double bottomTranslateZ = topTranslateZ;

								// WIPTech.info(random1.nextInt(11) - 5);

								if (possibleSubBranches == 0) { // Main branch
									// topTranslateX += random1.nextInt(11) - 5;
									// topTranslateZ += random1.nextInt(11) - 5;
								} else {
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
									// double topOffsetX = x + 0.5D - topWidth;
									// double topOffsetZ = z + 0.5D - topWidth;

									double topOffsetX = topWidth;
									double topOffsetZ = topWidth;

									if (side == 1 || side == 2) {
										topOffsetX += topWidth * 2.0D;
									}

									if (side == 2 || side == 3) {
										topOffsetZ += topWidth * 2.0D;
									}

									// double bottomOffsetX = x + 0.5D - bottomWidth;
									// double bottomOffsetZ = z + 0.5D - bottomWidth;

									double bottomOffsetX = bottomWidth;
									double bottomOffsetZ = bottomWidth;

									if (side == 1 || side == 2) {
										bottomOffsetX += bottomWidth * 2.0D;
									}

									if (side == 2 || side == 3) {
										bottomOffsetZ += bottomWidth * 2.0D;
									}

									// topOffsetX /= scale;
									// topOffsetZ = 0;
									// bottomOffsetX = 0;
									// bottomOffsetZ = 0;

									// topTranslateX = -scale;
									// topTranslateZ = -scale;
									// bottomTranslateX = -scale;
									// bottomTranslateZ = -scale;

									bufferbuilder.pos(bottomOffsetX + topTranslateX * (scale / 16), yPos * scale, bottomOffsetZ + topTranslateZ * (scale / 16)).color(0.45F, 0.45F, 0.5F, 0.3F)
											.endVertex();
									bufferbuilder.pos(topOffsetX + bottomTranslateX * (scale / 16), (yPos + 1) * scale, topOffsetZ + bottomTranslateZ * (scale / 16)).color(0.45F, 0.45F, 0.5F, 0.3F)
											.endVertex();
								}

								tessellator.draw();

							}
						}
					}
				}
			}

			GlStateManager.popMatrix();
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
}
