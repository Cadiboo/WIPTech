package cadiboo.wiptech.client.render.tileentity;

import java.util.Random;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.tileentity.TileEntityModFurnace;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityModFurnaceRenderer extends ModTileEntitySpecialRenderer<TileEntityModFurnace> {

	@Override
	public void renderAtCentre(final TileEntityModFurnace te, final float partialTicks, final int destroyStage, final float alpha) {

		final ItemStack input = te.getInventory().getStackInSlot(te.INPUT_SLOT);
		final ItemStack fuel = te.getInventory().getStackInSlot(te.FUEL_SLOT);
		ClientUtil.enableMaxLighting();

		if (!input.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(4 / 16d, 4 / 16d, 4 / 16d);
			GlStateManager.translate(0, 1 / 2d, 0);

			if (!(input.getItem() instanceof ItemBlock)) {
				GlStateManager.rotate(90, 1, 0, 0);
			}

			if (te.isOn()) {

				// Other systems, such as Java, will want an integer where bits 0-7 are the red value, 8-15 the green, and 16-23 the blue.

				final int red;
				final int green;
				final int blue;

				final int smeltTime = te.getSmeltTime();
				final int maxSmeltTime = te.getMaxSmeltTime();

				final double smeltPercentage = ModUtil.map(0, maxSmeltTime, 0, 1, smeltTime);

				if (Block.getBlockFromItem(input.getItem()).isFlammable(this.getWorld(), BlockPos.ORIGIN, EnumFacing.UP)) {
					if (smeltTime <= (maxSmeltTime / 2)) {
						// heat up
						red = 0xFF;
						green = (int) ((0.5d - smeltPercentage) * 2 * 0xFF);
						blue = green;
					} else {
						// turn black if its flamable
						red = (int) ((1 - smeltPercentage) * 2 * 0xFF);
						green = 0x00;
						blue = 0x00;
					}
				} else {
					// heat up
					red = 0xFF;
					green = (int) ((1 - smeltPercentage) * 0xFF);
					blue = green;
				}

				final int color = ClientUtil.color(red, green, blue);

				ClientUtil.renderModelWithColor(ClientUtil.getModelFromStack(input, te.getWorld()), color);
			} else {
				ClientUtil.renderModel(ClientUtil.getModelFromStack(input, te.getWorld()));
			}
			GlStateManager.popMatrix();
		}

		final int[] parts = ModUtil.splitIntoParts(fuel.getCount(), 8);

		final IBakedModel fuelModel = ClientUtil.getModelFromStack(fuel, te.getWorld());

		for (int i = 0; i < Math.min(8, fuel.getCount()); i++) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(2 / 16d, 2 / 16d, 2 / 16d);
			GlStateManager.translate(0, -3, 0);

			GlStateManager.rotate(((360 / 8) * i), 0, 1, 0);

			GlStateManager.translate(1.5, 0, 0);

			for (int j = 0; j < parts[i]; j++) {
				GlStateManager.pushMatrix();

				GlStateManager.translate(0, j * 0.05f, 0);
				GlStateManager.rotate(new Random(j + i).nextInt(360), 0, 1, 0);

				if (!(fuel.getItem() instanceof ItemBlock)) {
					GlStateManager.translate(0, -0.4, 0);
					GlStateManager.rotate(90, 1, 0, 0);
				}

				GlStateManager.translate(j % 1.88, 0, 0);

				ClientUtil.renderModel(fuelModel);
				if (!(fuel.getItem() instanceof ItemBlock)) {
					GlStateManager.rotate(-90, 1, 0, 0);
				}
				final double scale = ModUtil.map(0, te.getMaxFuelTime(), 0, 1, te.getFuelTimeRemaining());

				GlStateManager.translate(-(j % 1.88) * scale, 0, 0);

				GlStateManager.scale(scale, scale, scale);
				Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.FIRE.getDefaultState(), 1f);
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
		}

	}

}