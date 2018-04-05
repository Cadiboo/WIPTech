package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.client.model.ModelTurbine;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TESRTurbine extends TileEntitySpecialRenderer<TileEntityTurbine>  {

	private static final ModelTurbine MODEL = new ModelTurbine();
	private static final ResourceLocation IRON_TEXTURE = new ResourceLocation("minecraft", "textures/blocks/iron_block.png");

	@Override
	public void render(TileEntityTurbine tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);

		//GlStateManager.enableLight((int) (getWorld().getTotalWorldTime()%8));
		//RenderHelper.disableStandardItemLighting();

		bindTexture(IRON_TEXTURE);
		//bindTexture(Reference.DEBUG2_TEXTURE);

		//Why upside down? because UV
		GlStateManager.rotate(180, 0F, 0F, 1F);

		double angle = 0;

		if(tileEntity.canProduce())
		{
			angle = (getWorld().getTotalWorldTime() +partialTick ) * 15;
		}

		MODEL.render(0.0625F, angle);
		GlStateManager.popMatrix();
	}

}
