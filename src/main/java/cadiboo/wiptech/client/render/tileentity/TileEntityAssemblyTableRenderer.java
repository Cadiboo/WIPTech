package cadiboo.wiptech.client.render.tileentity;

import java.util.HashSet;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class TileEntityAssemblyTableRenderer extends ModTileEntitySpecialRenderer<TileEntityAssemblyTable> {

	private static final int HOLOGRAM_COLOR = 999999999;

	@Override
	public void renderAtCentre(final TileEntityAssemblyTable te, final float partialTicks, final int destroyStage, final float alpha) {

		final ItemStack main = te.getInventory().getStackInSlot(TileEntityAssemblyTable.ASSEMBLY_SLOT);
		if (main.isEmpty()) {
			return;
		}

		final HashSet<ItemStack> attachments = new HashSet<>();

		for (int slot = 0; slot < TileEntityAssemblyTable.ATTACHMENT_SLOTS_SIZE; slot++) {
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

		int color = HOLOGRAM_COLOR;

		if (te.isAssembling()) {
			// color it red based on assembly time
			color = ClientUtil.color(0, 0, 255 - (int) ModUtil.map(0, te.getMaxAssemblyTime(), 0, 255, te.getAssemblyTime()));
		}

		for (final ItemStack stack : attachments) {
			if (!stack.isEmpty()) {
				ClientUtil.renderStackWithColor(stack, te.getWorld(), color);
			}
		}

//		final IBakedModel model = ClientUtil.getModelFromStack(new ItemStack(ModItems.HEARTBEAT_SENSOR), te.getWorld());
//
//		final ImmutableMap<String, String> textures = ImmutableMap.<String, String>builder().put("screen", "minecraft:blocks/debug").build();
//
//		final IBakedModel model = ModelsCache.INSTANCE.getModel(new ModResourceLocation(ModReference.MOD_ID, "item/heartbeat_sensor")).retexture(textures).bake(ModelsCache.DEFAULTMODELSTATE, ModelsCache.DEFAULTVERTEXFORMAT, ModelsCache.DEFAULTTEXTUREGETTER);
//
//		ClientUtil.renderModelWithColor(model, HOLOGRAM_COLOR);

		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}

}
