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
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;


//COPPIED FROM IMMERSIVE ENGINEERING
// https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/client/render/EntityRenderRevolvershot.java
/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */



public class RenderEntityFerromagneticProjectile<T extends EntityFerromagneticProjectile> extends Render<T> {

	protected RenderEntityFerromagneticProjectile(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull T entity, double x, double y, double z, float f0, float f1)
	{
		GlStateManager.pushMatrix();
		this.bindEntityTexture(entity);
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		GlStateManager.disableCull();
		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);

		//GlStateManager.scale(.25f, .25f, .25f);

		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(0, .0,-.25).tex(5/32d, 10/32d).endVertex();
		bufferbuilder.pos(0, .0, .25).tex(0/32d, 10/32d).endVertex();
		bufferbuilder.pos(0, .5, .25).tex(0/32d, 5/32d).endVertex();
		bufferbuilder.pos(0, .5,-.25).tex(5/32d, 5/32d).endVertex();
		tessellator.draw();

		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(.375,	.125,0).tex(8/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.125,0).tex(0/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.375,0).tex(0/32d, 0/32d).endVertex();
		bufferbuilder.pos(.375,	.375,0).tex(8/32d, 0/32d).endVertex();
		tessellator.draw();

		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(.375,	.25,-.25).tex(8/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.25,-.25).tex(0/32d, 5/32d).endVertex();
		bufferbuilder.pos(0,	.25, .25).tex(0/32d, 0/32d).endVertex();
		bufferbuilder.pos(.375,	.25, .25).tex(8/32d, 0/32d).endVertex();
		tessellator.draw();

		GlStateManager.enableCull();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	/** If you just want to rotate it or something but the overall "have it heading towards the target" should stay the same */
	protected void customCustomRendering(T entity, double x, double y, double z, float entityYaw, float partialTicks) {}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull T entity) {
		return TextureMap.LOCATION_MISSING_TEXTURE;
	}
}