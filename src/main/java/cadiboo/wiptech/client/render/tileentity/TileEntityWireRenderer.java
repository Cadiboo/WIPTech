package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.tileentity.TileEntityWire;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class TileEntityWireRenderer extends ModTileEntitySpecialRenderer<TileEntityWire> {

	protected static final ItemStack stack = new ItemStack(net.minecraft.init.Items.IRON_INGOT);

	@Override
	public void renderAtCentre(TileEntityWire te, float partialTicks, int destroyStage, float alpha) {

		te.getElectrocutableEntities().forEach(entity -> {

			if (!te.shouldElectrocuteEntity(entity))
				return;

			GlStateManager.pushMatrix();
			try {
				final Vec3d tilePos = new Vec3d(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
				final Vec3d entityPos = ClientUtil.getEntityRenderPos(entity, partialTicks).addVector(-0.5, entity.getEyeHeight() / 2, -0.5);
//				destination.subtract(new Vec3d(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));

//				ClientUtil.rotateTowardsPos(tilePos, entityPos);

				ClientUtil.rotateTowardsPos(new Vec3d(0, 0, 0), tilePos.subtract(entityPos));

//				GlStateManager.rotate(180, 1, 0, 0);

				final Vec3d relativeEntityPos = tilePos.subtract(entityPos);

				final double scale = 0.125 * Math.sqrt(Math.pow(relativeEntityPos.x, 2) + Math.pow(relativeEntityPos.y, 2) + Math.pow(relativeEntityPos.z, 2));

				GlStateManager.translate(0, -8 * scale, 0);

				for (int i = 0; i < 2; i++) {
					ClientUtil.renderElectricity((int) te.getWorld().getTotalWorldTime() + i, 1, scale);
//					ClientUtil.drawLightning(new Random().nextInt(), 1, 0, scale);
//					ClientUtil.renderElectricity(i + 1, 1, scale);
				}

//				ClientUtil.drawLightning(2, 1, 0, scale);

			} catch (Exception e) {
				WIPTech.error(e.getLocalizedMessage());
				e.printStackTrace();
			}

			GlStateManager.popMatrix();

		});

	}

}