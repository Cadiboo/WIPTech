package cadiboo.wiptech.client.render.tileentity;

import java.util.List;

import cadiboo.wiptech.client.model.ModelTurbine;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TESRWire extends TileEntitySpecialRenderer<TileEntityWire> {

	private static final ModelTurbine		MODEL			= new ModelTurbine();
	private static final ResourceLocation	IRON_TEXTURE	= new ResourceLocation("minecraft", "textures/blocks/iron_block.png");

	@Override
	public void render(TileEntityWire tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha) {

		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(tileEntity.getBlockType()));
		if (model == null)
			return;
		List<BakedQuad> quads = model.getQuads(null, null, 0L);
		if (quads == null || quads.size() <= 0)
			return;
		TextureAtlasSprite sprite = quads.get(0).getSprite();
		if (sprite == null)
			return;

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

		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		GlStateManager.enableLighting();
		GlStateManager.enableColorMaterial();

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

		boolean draw = false;

		if (tileEntity.isConnectedTo(EnumFacing.UP)) { // UP
			GlStateManager.translate(0, 0.28125, 0);
			Utils.drawCuboid(minU, maxU, minV, maxV, 1, 3.5, 1, 0.0625);
			GlStateManager.translate(0, -0.28125, 0);
		}

		if (tileEntity.isConnectedTo(EnumFacing.DOWN)) { // DOWN
			GlStateManager.translate(0, -0.28125, 0);
			Utils.drawCuboid(minU, maxU, minV, maxV, 1, 3.5, 1, 0.0625);
			GlStateManager.translate(0, 0.28125, 0);
		}

		if (tileEntity.isConnectedTo(EnumFacing.SOUTH)) { // SOUTH
			GlStateManager.translate(0, 0, 0.28125);
			Utils.drawCuboid(minU, maxU, minV, maxV, 3.5, 1, 1, 0.0625);
			GlStateManager.translate(0, 0, -0.28125);
		}

		if (tileEntity.isConnectedTo(EnumFacing.NORTH)) { // NORTH
			GlStateManager.translate(0, 0, -0.28125);
			Utils.drawCuboid(minU, maxU, minV, maxV, 3.5, 1, 1, 0.0625);
			GlStateManager.translate(0, 0, 0.28125);
		}

		if (tileEntity.isConnectedTo(EnumFacing.EAST)) { // EAST
			GlStateManager.translate(0.28125, 0, 0);
			Utils.drawCuboid(minU, maxU, minV, maxV, 1, 1, 3.5, 0.0625);
			GlStateManager.translate(-0.28125, 0, 0);
		}

		if (tileEntity.isConnectedTo(EnumFacing.WEST)) { // WEST
			GlStateManager.translate(-0.28125, 0, 0);
			Utils.drawCuboid(minU, maxU, minV, maxV, 1, 1, 3.5, 0.0625);
			GlStateManager.translate(0.28125, 0, 0);
		}

		GlStateManager.popMatrix();
	}

}
