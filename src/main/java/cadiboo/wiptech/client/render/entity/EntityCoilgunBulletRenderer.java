package cadiboo.wiptech.client.render.entity;

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
			GlStateManager.translate(x, y + 0.125, z);

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
