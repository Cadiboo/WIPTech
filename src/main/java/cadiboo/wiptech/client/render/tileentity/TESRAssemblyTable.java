package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.Utils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

public class TESRAssemblyTable extends TileEntitySpecialRenderer<TileEntityAssemblyTable> {

	@Override
	public void render(TileEntityAssemblyTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		GlStateManager.depthMask(true);
		GlStateManager.disableLighting();
		// main
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 1.44, z + 0.5);
		GlStateManager.scale(3, 3, 3);
		Utils.renderStackWithoutTransforms(new ItemStack(te.getBlockType()), te.getWorld());
		GlStateManager.popMatrix();

		// capacitor1
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5 + 1);
		Utils.renderStackWithoutTransforms(new ItemStack(Blocks.CAPACITOR_BANK), te.getWorld());
		GlStateManager.popMatrix();

		// capacitor2
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5 - 1);
		Utils.renderStackWithoutTransforms(new ItemStack(Blocks.CAPACITOR_BANK), te.getWorld());
		GlStateManager.popMatrix();

		// motor
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		Utils.renderStackWithoutTransforms(new ItemStack(Blocks.SILVER_MOTOR), te.getWorld());
		GlStateManager.popMatrix();

		for (int i = 0; i < te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getSlots() - 1; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
			Utils.renderStack(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(i), te.getWorld());
			GlStateManager.popMatrix();
		}

		// holo
		GlStateManager.pushMatrix();
		GlStateManager.depthMask(false);
		GlStateManager.translate(x + 0.5, y + 2.5, z + 0.5);
		GlStateManager.enableBlend();
		GlStateManager.rotate(getWorld().getWorldTime() + partialTicks, 0, 1, 0);
		GlStateManager.scale(4, 4, 4);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
		// http://www.color-hex.com/color-palette/5951
		// GlStateManager.color(0, 247, 253);
		Utils.renderStack(new ItemStack(te.getAssembleItem()), te.getWorld());
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
	}
}
