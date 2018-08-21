package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.client.model.ModelsCache;
import cadiboo.wiptech.entity.item.EntityRailgun;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.ForgeHooksClient;

public class EntityRailgunRenderer extends Render<EntityRailgun> {

	public EntityRailgunRenderer(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityRailgun entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + 1.5 + 1, z);
		GlStateManager.scale(5, 5, 5);

		IBakedModel baseModel = ModelsCache.INSTANCE.getModel(new ResourceLocation(ModReference.ID, "entity/railgun_base"));
		baseModel = ForgeHooksClient.handleCameraTransforms(baseModel, ItemCameraTransforms.TransformType.GROUND, false);
		ClientUtil.renderModel(baseModel);

		GlStateManager.rotate(90 - entityYaw, 0, 1, 0);

		IBakedModel turretModel = ModelsCache.INSTANCE.getModel(new ResourceLocation(ModReference.ID, "entity/railgun_turret"));
		turretModel = ForgeHooksClient.handleCameraTransforms(turretModel, ItemCameraTransforms.TransformType.GROUND, false);
		ClientUtil.renderModel(turretModel);

		GlStateManager.popMatrix();

		//

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.translate(0, 1.5, 0);

		Vec3d look = entity.getLook(partialTicks);
		ClientUtil.rotateTowardsPos(new Vec3d(0, 0, 0), look);
		GlStateManager.translate(0, 32, 0);

		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

//		TextureAtlasSprite beam = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:entity/end_crystal/end_crystal_beam");
//		TextureAtlasSprite beam = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:entity/endercrystal/endercrystal_beam");
		TextureAtlasSprite beam = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_block");

		ClientUtil.enableMaxLighting();
		ClientUtil.drawSeamlessCuboid(beam.getMinU(), beam.getMaxU(), beam.getMinV(), beam.getMaxV(), 0.05, 32, 0.05, 1);

		GlStateManager.popMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityRailgun entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
