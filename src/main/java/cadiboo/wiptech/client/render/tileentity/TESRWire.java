package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.client.model.ModelEnamel;
import cadiboo.wiptech.client.model.ModelWire;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.Reference;
import cadiboo.wiptech.util.Utils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TESRWire extends TileEntitySpecialRenderer<TileEntityWire> {

	private static final ResourceLocation ENAMEL_TEXTURE = new ResourceLocation("minecraft", "textures/blocks/concrete_brown.png");

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

		TextureAtlasSprite sprite = Utils.getSpriteFromItemStack(new ItemStack(tileEntity.getBlockType()));
		if (sprite == null) {
			WIPTech.logger.info("Sprite is null!");
			return;
		}

		float minU = sprite.getMinU();
		float maxU = sprite.getMaxU();
		float minV = sprite.getMinV();
		float maxV = sprite.getMaxV();

		float width = maxU - minU;
		float height = maxV - minV;

		float midU = minU + (width / 2);
		float midV = minV + (height / 2);

		float multiplier = 2;

		minU = midU;
		maxU = midU + (width / 16);
		minV = midV - (height / 16);
		maxV = midV;

		ResourceLocation texLoc = TextureMap.LOCATION_BLOCKS_TEXTURE;
		boolean isEnamel = false;

		if (tileEntity.getBlockType() instanceof BlockWire) {
			texLoc = new ResourceLocation(Reference.ID, "textures/items/" + ((BlockWire) tileEntity.getBlockType()).getMetal().getName() + "_wire.png");
			isEnamel = ((BlockWire) tileEntity.getBlockType()).isEnamel();
		} else
			WIPTech.logger.error("WIRE RENDERING ERROR!!!!");

		this.bindTexture(texLoc);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		setLightmapDisabled(false);

		/* Why upside down? because UV */
		GlStateManager.rotate(180, 0F, 0F, 1F);

		if (tileEntity.isConnectedTo(EnumFacing.DOWN)) {
			GlStateManager.rotate(180, 1F, 1F, 0F);
			DOWN_MODEL_WIRE.render(0.0625F);
			if (isEnamel) {
				this.bindTexture(ENAMEL_TEXTURE);
				DOWN_MODEL_ENAMEL.render(0.0625F);
				this.bindTexture(texLoc);
			}
			GlStateManager.rotate(-180, 1F, 1F, 0F);
		}

		if (tileEntity.isConnectedTo(EnumFacing.UP)) {
			GlStateManager.rotate(270, 0F, 0F, 1F);
			UP_MODEL_WIRE.render(0.0625F);
			if (isEnamel) {
				this.bindTexture(ENAMEL_TEXTURE);
				UP_MODEL_ENAMEL.render(0.0625F);
				this.bindTexture(texLoc);
			}
			GlStateManager.rotate(-270, 0F, 0F, 1F);
		}

		if (tileEntity.isConnectedTo(EnumFacing.NORTH)) {
			GlStateManager.rotate(90, 0F, 1F, 0F);
			NORTH_MODEL_WIRE.render(0.0625F);
			if (isEnamel) {
				this.bindTexture(ENAMEL_TEXTURE);
				NORTH_MODEL_ENAMEL.render(0.0625F);
				this.bindTexture(texLoc);
			}
			GlStateManager.rotate(-90, 0F, 1F, 0F);
		}

		if (tileEntity.isConnectedTo(EnumFacing.SOUTH)) {
			GlStateManager.rotate(180, 1F, 0F, 1F);
			SOUTH_MODEL_WIRE.render(0.0625F);
			if (isEnamel) {
				this.bindTexture(ENAMEL_TEXTURE);
				SOUTH_MODEL_ENAMEL.render(0.0625F);
				this.bindTexture(texLoc);
			}
			GlStateManager.rotate(-180, 1F, 0F, 1F);
		}

		if (tileEntity.isConnectedTo(EnumFacing.WEST)) {
			WEST_MODEL_WIRE.render(0.0625F);
			if (isEnamel) {
				this.bindTexture(ENAMEL_TEXTURE);
				WEST_MODEL_ENAMEL.render(0.0625F);
				this.bindTexture(texLoc);
			}
		}

		if (tileEntity.isConnectedTo(EnumFacing.EAST)) {
			GlStateManager.rotate(180, 0F, 0F, 1F);
			EAST_MODEL_WIRE.render(0.0625F);
			if (isEnamel) {
				this.bindTexture(ENAMEL_TEXTURE);
				EAST_MODEL_ENAMEL.render(0.0625F);
				this.bindTexture(texLoc);
			}
			GlStateManager.rotate(-180, 0F, 0F, 1F);
		}

		GlStateManager.popMatrix();
	}

}
