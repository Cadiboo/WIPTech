package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.client.model.ModelsCache;
import cadiboo.wiptech.entity.item.EntityRailgun;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityRailgunRenderer extends Render<EntityRailgun> {

	public EntityRailgunRenderer(final RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(final EntityRailgun entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		final Vec3d look = entity.getLook(partialTicks).normalize();
		final float scale = 5;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableLighting();

		try {

			GlStateManager.translate(0, entity.height + 0.5, 0);
			GlStateManager.scale(scale, scale, scale);
			try {
				GlStateManager.pushMatrix();
				ClientUtil.renderModel(ModelsCache.INSTANCE.getBakedModel(new ModResourceLocation(ModReference.MOD_ID, "entity/railgun_base")));
				GlStateManager.popMatrix();
			} catch (final Exception e) {
				WIPTech.error("Error rendering railgun base");
				e.printStackTrace();
			}

			try {
				GlStateManager.pushMatrix();
				GlStateManager.rotate(180 - entityYaw, 0, 1, 0);
				ClientUtil.renderModel(ModelsCache.INSTANCE.getBakedModel(new ModResourceLocation(ModReference.MOD_ID, "entity/railgun_turret")));
				GlStateManager.popMatrix();
			} catch (final Exception e) {
				WIPTech.error("Error rendering railgun turret");
				e.printStackTrace();
			}

			try {
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, -1f / scale, 0);
				GlStateManager.rotate(180 - entityYaw, 0, 1, 0);
				GlStateManager.rotate(90 - (float) ClientUtil.getPitch(new Vec3d(0, 0, 0), look), 1, 0, 0);
				ClientUtil.renderModel(ModelsCache.INSTANCE.getBakedModel(new ModResourceLocation(ModReference.MOD_ID, "entity/railgun_gun")));
				GlStateManager.popMatrix();
			} catch (final Exception e) {
				WIPTech.error("Error rendering railgun gun");
				e.printStackTrace();
			}

			// throw new Exception();

		} catch (final Exception e) {
			WIPTech.error("Error rendering railgun parts");
			e.printStackTrace();
		}

		GlStateManager.popMatrix();

		//

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.translate(0, 1.5, 0);

		ClientUtil.rotateZAxisTowardsPos(new Vec3d(0, 0, 0), look);
		GlStateManager.translate(32, 0, 0);

		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		// TextureAtlasSprite beam = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:entity/end_crystal/end_crystal_beam");
		// TextureAtlasSprite beam = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:entity/endercrystal/endercrystal_beam.png");
		final TextureAtlasSprite beam = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_block");

		ClientUtil.enableMaxLighting();
		ClientUtil.drawSeamlessCuboid(beam.getMinU(), beam.getMaxU(), beam.getMinV(), beam.getMaxV(), 0.05, 0.05, 32, 1);

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(look.x, look.y, look.z);
		int size = 0;
		for (int i = 0; i < entity.getInventory().getSlots(); i++) {
			size += entity.getInventory().getStackInSlot(i).getCount();
		}
		int maxSize = 0;
		for (int i = 0; i < entity.getInventory().getSlots(); i++) {
			maxSize += entity.getInventory().getStackInSlot(i).getMaxStackSize();
		}
		// TODO color doesnt work rn, fix it
		final float color = (float) ModUtil.map(0, maxSize, 0, 1, size);
		GlStateManager.color(1 - color, color, 0);
		this.renderLivingLabel(entity, size + "/" + maxSize, x, y, z, 64);
		GlStateManager.popMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityRailgun entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
