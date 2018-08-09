package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.entity.item.EntityRailgun;
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

public class EntityRailgunRenderer extends Render<EntityRailgun> {

	public EntityRailgunRenderer(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityRailgun entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y + 0.5, (float) z);

		GlStateManager.scale(entity.width, entity.height, entity.width);

		ItemStack stack = new ItemStack(Blocks.IRON_BLOCK);

		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, entity.getEntityWorld(), null);
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

		GlStateManager.popMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityRailgun entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
