package cadiboo.wiptech.client.render.tileentity;

import java.util.List;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.client.model.ModelEnamel;
import cadiboo.wiptech.client.model.ModelWire;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.Position;
import cadiboo.wiptech.util.Reference;
import cadiboo.wiptech.util.Utils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

//Rotation algorithm Taken off Max_the_Technomancer from
// www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2772267-tesr-getting-darker-and-lighter-as-it-rotates

public class TESRWire extends TileEntitySpecialRenderer<TileEntityWire> {

	private static final ResourceLocation ENAMEL_TEXTURE = new ResourceLocation("minecraft", "textures/blocks/concrete_brown.png");

	private static final int[] RANDOMS = new int[] { 5, 6, 9, 11, 12, 14, 15, 20, 24, 25, 27, 29, 31, 37, 39, 46, 50, 51, 55, 56, 59, 61, 67, 72, 73, 74, 77, 78, 79, 83, 85, 89 };

	public static final int	BRANCHES		= 1;
	public static final int	SUB_BRANCHES	= 2;

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
		final boolean isEnamel = ((BlockWire) tileEntity.getBlockType()).isEnamel();

		if (!isEnamel && tileEntity.electrocutionTime > 0) {
			List<Entity> entities = tileEntity.getElectrocutableEntities();
			for (int i = 0; i < entities.size(); i++) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

				Position entityPos = Utils.getEntityRenderPos(entities.get(i), partialTicks);
				entityPos.add(-tileEntity.getPos().getX() - 0.5, -tileEntity.getPos().getY() - 0.5, -tileEntity.getPos().getZ() - 0.5);
				entityPos.y += entities.get(i).getEyeHeight() / 2;

				double yAngle = Math.atan2(-entityPos.getX(), -entityPos.getZ());
				yAngle = yAngle * (180 / Math.PI);
				yAngle = yAngle < 0 ? 360 - (-yAngle) : yAngle;
				GlStateManager.rotate((float) yAngle + 90, 0, 1, 0);

				GlStateManager.rotate(180, 1, 0, 0);

				double _Angle = Math.atan2(entityPos.getY(), Math.sqrt(entityPos.getX() * entityPos.getX() + entityPos.getZ() * entityPos.getZ()));
				_Angle = _Angle * (180 / Math.PI);
				_Angle = _Angle < 0 ? 360 - (-_Angle) : _Angle;
				GlStateManager.rotate(90 - (float) _Angle, 0, 0, 1);

				final double scale = 0.125 * Math.sqrt(Math.pow(entityPos.getX(), 2) + Math.pow(entityPos.getY(), 2) + Math.pow(entityPos.getZ(), 2));

				final int number = RANDOMS[(int) ((getWorld().getTotalWorldTime() + tileEntity.getPos().getX() + tileEntity.getPos().getY() + tileEntity.getPos().getZ()) % RANDOMS.length)];

				GlStateManager.translate(0, -8 * scale, 0);
				Utils.drawLightning(number, BRANCHES, SUB_BRANCHES, scale);
				GlStateManager.popMatrix();
			}
		}

		GlStateManager.depthMask(true);

		final ResourceLocation texLoc = new ResourceLocation(Reference.ID, "textures/items/" + ((BlockWire) tileEntity.getBlockType()).getMetal().getName() + "_wire.png");
		this.bindTexture(texLoc);

		sides: for (int i = 0; i < EnumFacing.VALUES.length; i++) {
			final EnumFacing side = EnumFacing.VALUES[i];
			if (!tileEntity.isConnectedTo(side))
				continue sides;
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
			Utils.rotateHorizontal(side);

			if (isEnamel) {
				this.bindTexture(ENAMEL_TEXTURE);
				DOWN_MODEL_ENAMEL.render(ONE_SIXTEENTH);
				this.bindTexture(texLoc);
				if (!tileEntity.isConnectedToWire(side))
					GlStateManager.translate(SEVEN_SIXTEENTHS, 0, 0);
			} else if (!tileEntity.isConnectedToWire(side)) {
				GlStateManager.translate(SEVEN_SIXTEENTHS, 0, 0);
				DOWN_MODEL_WIRE.render(ONE_SIXTEENTH);
				GlStateManager.translate(-SEVEN_SIXTEENTHS, 0, 0);
			}
			DOWN_MODEL_WIRE.render(ONE_SIXTEENTH);
			GlStateManager.popMatrix();
		}
	}
}
