package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.tileentity.ModTileEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public abstract class ModTileEntitySpecialRenderer<T extends ModTileEntity> extends TileEntitySpecialRenderer<T> {

	@Override
	public final void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te == null) {
			WIPTech.error("Error rendering tile entity! te is null");
			new Exception().printStackTrace();
			return;
		}

		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		try {
			this.renderAtCentre(te, partialTicks, destroyStage, alpha);
		} catch (Exception e) {
			e.printStackTrace();
		}

		GlStateManager.popMatrix();

	}

	public abstract void renderAtCentre(T te, float partialTicks, int destroyStage, float alpha);

}
