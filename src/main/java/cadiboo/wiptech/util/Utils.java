package cadiboo.wiptech.util;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Utils {

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

}
