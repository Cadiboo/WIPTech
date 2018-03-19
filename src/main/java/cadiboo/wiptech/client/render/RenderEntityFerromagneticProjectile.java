package cadiboo.wiptech.client.render;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import cadiboo.wiptech.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

//Absolutely copied from Tinkers Construct

public class RenderEntityFerromagneticProjectile<T extends EntityFerromagneticProjectile> extends Render<T> {

	protected RenderEntityFerromagneticProjectile(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		ItemStack itemStack = entity.getAmmoStack();

		renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

		if(renderManager == null || renderManager.renderEngine == null) {
			return;
		}

		// draw correct texture. not some weird block fragments.

		if(!itemStack.isEmpty()) {
			Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, ItemCameraTransforms.TransformType.NONE);
		}
		else {
			ItemStack dummy = ItemStack.EMPTY;
			Minecraft.getMinecraft().getRenderItem().renderItem(dummy, Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getMissingModel());
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public void customRendering(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

		// flip it, flop it, pop it, pull it, push it, rotate it, translate it, TECHNOLOGY

		// rotate it into the direction we threw it
		GL11.glRotatef(entity.rotationYaw, 0f, 1f, 0f);
		GL11.glRotatef(-entity.rotationPitch, 1f, 0f, 0f);

		// adjust "stuck" depth
		if(entity.inGround) {
			GL11.glTranslated(0, 0, -entity.getStuckDepth());
		}

		customCustomRendering(entity, x, y, z, entityYaw, partialTicks);

		// rotate it so it faces forward
		GL11.glRotatef(-90f, 0f, 1f, 0f);

		// rotate the projectile it so it faces upwards
		GL11.glRotatef(-45, 0f, 0f, 1f);
	}

	/** If you just want to rotate it or something but the overall "have it heading towards the target" should stay the same */
	protected void customCustomRendering(T entity, double x, double y, double z, float entityYaw, float partialTicks) {}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull T entity) {
		return TextureMap.LOCATION_MISSING_TEXTURE;
	}
}