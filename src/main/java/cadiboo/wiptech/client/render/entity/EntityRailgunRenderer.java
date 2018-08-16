package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.entity.item.EntityRailgun;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;

public class EntityRailgunRenderer extends ModEntityRenderer<EntityRailgun> {

	public EntityRailgunRenderer(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityRailgun entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.pushMatrix();

		GlStateManager.translate(x, y, z);

		GlStateManager.translate(0, 1.5, 0);

		Vec3d look = entity.getLook(partialTicks);

		ClientUtil.rotateTowardsPos(new Vec3d(0, 0, 0), look);

//		GlStateManager.translate(0, 0, 3);

		GlStateManager.bindTexture(1);

		ClientUtil.drawCuboid(0, 0.1f, 0, 0.1f, 0.1, 110.1, 0.1, 1);

		GlStateManager.popMatrix();

	}

}
