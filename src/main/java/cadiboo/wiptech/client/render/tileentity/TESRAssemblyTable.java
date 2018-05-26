package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.Utils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;

public class TESRAssemblyTable extends TileEntitySpecialRenderer<TileEntityAssemblyTable> {

	public static final int HOLO_COLOR = 999999999;

	@Override
	public void render(TileEntityAssemblyTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		GlStateManager.depthMask(true);
		GlStateManager.disableLighting();
		GlStateManager.enableCull();
		GlStateManager.enableRescaleNormal();

		// main
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 1.44, z + 0.5);
		GlStateManager.scale(3, 3, 3);
		Utils.renderStackWithoutTransforms(new ItemStack(te.getBlockType()), te.getWorld());
		GlStateManager.popMatrix();

		// capacitors
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5 + 1);
		Utils.renderStackWithoutTransforms(new ItemStack(Blocks.CAPACITOR_BANK), te.getWorld());
		GlStateManager.translate(0, 0, -2);
		Utils.renderStackWithoutTransforms(new ItemStack(Blocks.CAPACITOR_BANK), te.getWorld());
		// motor
		GlStateManager.translate(0, 0, 1);
		Utils.renderStackWithoutTransforms(new ItemStack(Blocks.SILVER_MOTOR), te.getWorld());
		GlStateManager.popMatrix();

		for (int i = 0; i < te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP).getSlots(); i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 2.5, z + 0.5);
			Utils.renderStack(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(i), te.getWorld());
			GlStateManager.popMatrix();
		}

		// hologram
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 2.5, z + 0.5);

		ItemStack stack = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN).getStackInSlot(0);
		boolean holo = stack.isEmpty();
		GlStateManager.rotate(getWorld().getTotalWorldTime() + partialTicks, 0, 1, 0);

		if (holo && !te.getAssembleItem().isEmpty()) {
			GlStateManager.scale(2, 2, 2);
			GlStateManager.translate(0, 0.25, 0);
			GlStateManager.enableBlend();
			// te.getWorld().setWorldTime(new
			// Random(System.currentTimeMillis()).nextInt(24000));
			GlStateManager.blendFunc(te.getWorld().getWorldTime() % 24000d / 12000 < 1d ? GlStateManager.SourceFactor.SRC_COLOR : GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
			// http://www.color-hex.com/color-palette/5951

			Utils.renderItemWithColor(te.getAssembleItem(), Utils.getModelFromStack(te.getAssembleItem(), te.getWorld()), te.getAssemblyTime() > 0 ? (int) System.currentTimeMillis() : HOLO_COLOR);

			// Utils.renderItemWithColor(new ItemStack(te.getAssembleItem()),
			// Utils.getModelFromStack(new ItemStack(te.getAssembleItem()), te.getWorld()),
			// (int) System.currentTimeMillis());

			GlStateManager.disableBlend();

		} else {
			GlStateManager.scale(4, 4, 4);
			Utils.renderStack(stack, te.getWorld());
		}
		GlStateManager.popMatrix();
	}
}
