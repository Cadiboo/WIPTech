package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.client.model.ModelEnamel;
import cadiboo.wiptech.client.model.ModelWire;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.Reference;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
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
