package cadiboo.wiptech.client.render.entity;

import java.math.BigDecimal;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.entity.projectile.EntityCoilgunBullet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityCoilgunBulletRenderer extends Render<EntityCoilgunBullet> {

	public EntityCoilgunBulletRenderer(final RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(final EntityCoilgunBullet entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.disableLighting();

		ClientUtil.enableMaxLighting();

		GlStateManager.pushMatrix();
		try {

//			if (entity.ticksExisted == 0) {
//				entity.lastTickPosX = entity.posX;
//				entity.lastTickPosY = entity.posY;
//				entity.lastTickPosZ = entity.posZ;
//			}
//
//			final double d0 = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
//			final double d1 = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
//			final double d2 = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);
//			final float f = entity.prevRotationYaw + ((entity.rotationYaw - entity.prevRotationYaw) * partialTicks);

			GlStateManager.translate(x, y + 0.125, z);

//			final double posX = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks) + (entity.getMotionX().doubleValue() * partialTicks);
//			final double posY = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks) + (entity.getMotionY().doubleValue() * partialTicks);
//			final double posZ = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks) + (entity.getMotionZ().doubleValue() * partialTicks);

			final double scale = 1000000000;

			final double posX = entity.getMotionX().multiply(new BigDecimal(partialTicks * scale)).doubleValue();
			final double posY = entity.getMotionY().multiply(new BigDecimal(partialTicks * scale)).doubleValue();
			final double posZ = entity.getMotionZ().multiply(new BigDecimal(partialTicks * scale)).doubleValue();
//
			GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);

			GlStateManager.translate(posX, posY, posZ);

			GlStateManager.scale(scale, scale, scale);

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			final TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();

			final TextureAtlasSprite lava = texturemap.getAtlasSprite("minecraft:blocks/lava_flow");

			ClientUtil.drawSeamlessCuboid(lava.getMinU(), lava.getMaxU(), lava.getMinV(), lava.getMaxV(), 0.125, 0.125, 0.125, 1);

		} catch (final Exception e) {
			// TODO: handle exception
		}

		GlStateManager.enableLighting();

		GlStateManager.popMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityCoilgunBullet entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
