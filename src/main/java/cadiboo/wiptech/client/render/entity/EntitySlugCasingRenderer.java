package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.entity.projectile.EntitySlugCasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntitySlugCasingRenderer extends Render<EntitySlugCasing> {

	public EntitySlugCasingRenderer(final RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(final EntitySlugCasing entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		try {

			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x, (float) y + 0.5, (float) z);

			GlStateManager.translate(0, -0.56f, 0);

			final ItemStack stack = new ItemStack(Blocks.IRON_BLOCK);

			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, entity.getEntityWorld(), null);
			model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

			this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

			GlStateManager.popMatrix();

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected ResourceLocation getEntityTexture(final EntitySlugCasing entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}
