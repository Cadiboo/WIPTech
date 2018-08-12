package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.entity.projectile.EntityRailgunCasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

public class EntityRailgunCasingRenderer extends ModEntityRenderer<EntityRailgunCasing> {

    public EntityRailgunCasingRenderer(RenderManager renderManager) {
	super(renderManager);
    }

    @Override
    public void doRender(EntityRailgunCasing entity, double x, double y, double z, float entityYaw,
	    float partialTicks) {
	super.doRender(entity, x, y, z, entityYaw, partialTicks);

	GlStateManager.pushMatrix();
	GlStateManager.translate((float) x, (float) y + 0.5, (float) z);

	GlStateManager.translate(0, -0.56f, 0);

	ItemStack stack = new ItemStack(Blocks.IRON_BLOCK);

	IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack,
		entity.getEntityWorld(), null);
	model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

	bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

	GlStateManager.popMatrix();

    }
}
