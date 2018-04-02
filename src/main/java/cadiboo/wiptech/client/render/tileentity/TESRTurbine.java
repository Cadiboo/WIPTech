package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.tileentity.TileEntityTurbine;
import cadiboo.wiptech.util.Reference;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TESRTurbine extends TileEntitySpecialRenderer<TileEntityTurbine>  {

	private static final ResourceLocation MODEL_MIDDLE	= new ResourceLocation(Reference.ID, "/models/block/turbine_middle.json");
	private static final ResourceLocation MODEL_TOP		= new ResourceLocation(Reference.ID, "/models/block/turbine_top.json");

	public void render(TileEntityTurbine te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.translate(x, y, z);
	}

}
