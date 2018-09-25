package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityNapalmRenderer extends Render<EntityNapalm> {

	public EntityNapalmRenderer(final RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(final EntityNapalm entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.disableLighting();

		ClientUtil.enableMaxLighting();

		// speed = distance/time

		final Vec3d vec3d = new Vec3d(entity.posX, entity.posY, entity.posZ);
		final Vec3d vec3d1 = new Vec3d(entity.posX + entity.motionX, entity.posY + entity.motionY, entity.posZ + entity.motionZ);
		final Vec3d vec3d2 = new Vec3d(entity.posX + (entity.motionX * partialTicks), entity.posY + (entity.motionY * partialTicks), entity.posZ + (entity.motionZ * partialTicks));

		// Interpolating everything back to 0,0,0. These are transforms you can find at RenderEntity class
		final double d0 = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
		final double d1 = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
		final double d2 = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);

		// Interpolating everything back to 0,0,0. These are transforms you can find at RenderEntity class
		final double d01 = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks) + (entity.motionX * partialTicks);
		final double d11 = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks) + (entity.motionY * partialTicks);
		final double d21 = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks) + (entity.motionZ * partialTicks);

//		GlStateManager.pushMatrix();
//		GlStateManager.translate(vec3d.x, vec3d.y, vec3d.z);
//		this.bindTexture(TextureMap.LOCATION_MISSING_TEXTURE);
//		ClientUtil.drawSeamlessCuboid(0, 1, 0, 1, 1, 1, 1, 1);
//		GlStateManager.popMatrix();
//
//		GlStateManager.pushMatrix();
//		GlStateManager.translate(vec3d1.x, vec3d1.y, vec3d1.z);
//		this.bindTexture(TextureMap.LOCATION_MISSING_TEXTURE);
//		ClientUtil.drawSeamlessCuboid(0, 1, 0, 1, 1, 1, 1, 1);
//		GlStateManager.popMatrix();

//		GlStateManager.pushMatrix();
//		GlStateManager.translate(vec3d2.x, vec3d2.y, vec3d2.z);
//		this.bindTexture(TextureMap.LOCATION_MISSING_TEXTURE);
//		ClientUtil.drawSeamlessCuboid(0, 1, 0, 1, 1, 1, 1, 1);
//		GlStateManager.popMatrix();

//		GlStateManager.pushMatrix();
//		GlStateManager.translate(d0, d1, d2);
//		this.bindTexture(TextureMap.LOCATION_MISSING_TEXTURE);
//		ClientUtil.drawSeamlessCuboid(0, 1, 0, 1, 1, 1, 1, 1);
//		GlStateManager.popMatrix();

		if (entity.getThrower() == null) {
			return;
		}
		final Vec3d look = entity.getThrower().getLook(partialTicks);

		GlStateManager.pushMatrix();
		GlStateManager.translate(look.x * partialTicks, look.y * partialTicks, look.z * partialTicks);
		this.bindTexture(TextureMap.LOCATION_MISSING_TEXTURE);
		ClientUtil.drawSeamlessCuboid(0, 1, 0, 1, 1, 1, 1, 1);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		try {
			GlStateManager.translate(x, y + 0.125, z);

			ClientUtil.rotateForPitchYaw(-entity.rotationPitch, entityYaw);

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			final TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();

			final TextureAtlasSprite lava = texturemap.getAtlasSprite("minecraft:blocks/lava_flow");

//			ClientUtil.drawSeamlessCuboid(lava.getMinU(), lava.getMaxU(), lava.getMinV(), lava.getMaxV(), 0.125, 0.125, 0.125, 10);

		} catch (final Exception e) {
			// TODO: handle exception
		}

		GlStateManager.enableLighting();

		GlStateManager.popMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityNapalm entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
