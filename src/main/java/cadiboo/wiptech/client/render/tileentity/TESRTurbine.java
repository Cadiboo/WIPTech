package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.client.model.ModelTurbine;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TESRTurbine extends TileEntitySpecialRenderer<TileEntityTurbine>  {

	private static final ModelTurbine model = new ModelTurbine();

	@Override
	public void render(TileEntityTurbine tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		//GlStateManager.enableLight((int) (getWorld().getTotalWorldTime()%8));
		RenderHelper.disableStandardItemLighting();
		
		bindTexture(new ResourceLocation("minecraft", "textures/blocks/iron_block.png"));
		//bindTexture(Reference.DEBUG2_TEXTURE);

		//Why upside down? because UV
		GlStateManager.rotate(180, 0F, 0F, 1F);

		double angle = (getWorld().getTotalWorldTime() +partialTick ) * 20;
		//angle = 0;
		
		/*if(tileEntity.isActive)
		{
			angle = (tileEntity.angle+((tileEntity.getPos().getY()+4F)/TileEntityWindGenerator.SPEED_SCALED)*partialTick)%360;
		}
		*/

		//new ModelTurbine().render(0.1F, (tileEntity.getWorld().getWorldTime()+partialTick));
		new ModelTurbine().render(0.0625F, angle);
		
		//model.render(0.0625F, tileEntity.getWorld().getWorldTime());
		GlStateManager.popMatrix();
	}

}
