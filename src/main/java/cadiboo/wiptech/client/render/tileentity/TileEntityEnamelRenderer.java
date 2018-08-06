package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.tileentity.TileEntityEnamel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TileEntityEnamelRenderer extends ModTileEntitySpecialRenderer<TileEntityEnamel> {

	protected final ItemStack stack = new ItemStack(Items.IRON_INGOT);

	@Override
	public void renderAtCentre(TileEntityEnamel te, float partialTicks, int destroyStage, float alpha) {
	}

}
