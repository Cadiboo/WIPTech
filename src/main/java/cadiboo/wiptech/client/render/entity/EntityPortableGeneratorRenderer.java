package cadiboo.wiptech.client.render.entity;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import cadiboo.wiptech.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.ForgeHooksClient;

public class EntityPortableGeneratorRenderer extends ModEntityRenderer<EntityPortableGenerator> {

    public EntityPortableGeneratorRenderer(RenderManager renderManager) {
	super(renderManager);
    }

    @Override
    public void doRender(EntityPortableGenerator entity, double x, double y, double z, float entityYaw,
	    float partialTicks) {
	super.doRender(entity, x, y, z, entityYaw, partialTicks);

	GlStateManager.pushMatrix();
	GlStateManager.translate((float) x, (float) y + 0.5, (float) z);
	GlStateManager.enableRescaleNormal();
	GlStateManager.disableLighting();

	Vec3d entityPos = ClientUtil.getEntityRenderPos(entity, partialTicks).subtract(entity.posX, entity.posY,
		entity.posZ);

	GlStateManager.translate(entityPos.x, entityPos.y, entityPos.z);

	ItemStack stack = new ItemStack(ModItems.PORTABLE_GENERATOR);

	IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack,
		entity.getEntityWorld(), null);
	model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

	bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

	if (this.renderOutlines) {
	    GlStateManager.enableColorMaterial();
	    GlStateManager.enableOutlineMode(getTeamColor(entity));
	}

	ClientUtil.renderElectricity(1, 1, 1);

	if (this.renderOutlines) {
	    GlStateManager.disableOutlineMode();
	    GlStateManager.disableColorMaterial();
	}
	GlStateManager.enableLighting();
	GlStateManager.disableRescaleNormal();
	GlStateManager.popMatrix();

    }

}
