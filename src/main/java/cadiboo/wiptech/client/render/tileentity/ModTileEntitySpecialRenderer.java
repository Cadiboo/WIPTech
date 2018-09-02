package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.WIPTech;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ModTileEntitySpecialRenderer<T extends TileEntity> extends TileEntitySpecialRenderer<T> {

	@Override
	public final void render(final T te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
		if (te == null) {
			WIPTech.error("Error rendering tile entity! te is null");
			new Exception().printStackTrace();
			return;
		}

		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		try {
			this.renderAtCentre(te, partialTicks, destroyStage, alpha);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		GlStateManager.popMatrix();

	}

	public abstract void renderAtCentre(T te, float partialTicks, int destroyStage, float alpha);

}
