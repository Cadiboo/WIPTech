package cadiboo.wiptech.client.render;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile2;
import cadiboo.wiptech.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

//Absolutely copied from Tinkers Construct

public class RenderEntityFerromagneticProjectile2<T extends EntityFerromagneticProjectile2> extends Render<T> {

	protected RenderEntityFerromagneticProjectile2(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		// preface: Remember that the rotations are applied in reverse order.
		// the rendering call does not apply any transformations.
		// That'd screw things up, since it'd be applied before our transformations
		// So remember to read this from the rendering call up to this line

		ItemStack itemStack = entity.getAmmoStack();

		WIPTech.logger.info("doRender");

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		// last step: translate from 0/0/0 to correct position in world
		GL11.glTranslated(x, y, z);
		// mkae it smaller
		GL11.glScalef(0.5F, 0.5F, 0.5F);

		customRendering(entity, x, y, z, entityYaw, partialTicks);

		// arrow shake
		float f11 = (float) entity.projectileShake - partialTicks;
		if(f11 > 0.0F) {
			float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
			GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
		}

		if(renderManager == null || renderManager.renderEngine == null) {
			return;
		}

		// draw correct texture. not some weird block fragments.
		renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

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

		WIPTech.logger.info("customRendering");

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