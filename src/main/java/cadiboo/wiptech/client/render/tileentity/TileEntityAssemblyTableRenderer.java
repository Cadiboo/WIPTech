package cadiboo.wiptech.client.render.tileentity;

import java.util.HashSet;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class TileEntityAssemblyTableRenderer extends ModTileEntitySpecialRenderer<TileEntityAssemblyTable> {

	@Override
	public void renderAtCentre(final TileEntityAssemblyTable te, final float partialTicks, final int destroyStage, final float alpha) {

		final ItemStack main = te.getInventory().getStackInSlot(te.getInventory().getSlots() - 2);
		if (main.isEmpty()) {
			return;
		}

		final HashSet<ItemStack> attachments = new HashSet<>();

		for (int slot = 0; slot < (te.getInventory().getSlots() - 2); slot++) {
			attachments.add(te.getInventory().getStackInSlot(slot));
		}

		ClientUtil.enableMaxLighting();
		GlStateManager.enableLighting();

		GlStateManager.pushMatrix();

		GlStateManager.rotate(te.getWorld().getTotalWorldTime() + partialTicks, 0, 1, 0);

		GlStateManager.translate(0, 3, 0);

		GlStateManager.scale(3, 3, 3);

		ClientUtil.renderStack(main, te.getWorld());

		ClientUtil.enableMaxLighting();

		GlStateManager.enableBlend();

		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

		for (final ItemStack stack : attachments) {
			if (!stack.isEmpty()) {
				ClientUtil.renderStackWithColor(stack, te.getWorld(), 999999999);
			}
		}

		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}

}
