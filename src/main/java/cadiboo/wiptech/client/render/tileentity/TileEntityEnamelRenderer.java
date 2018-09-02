package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.tileentity.TileEntityEnamel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityEnamelRenderer extends ModTileEntitySpecialRenderer<TileEntityEnamel> {

	protected final ItemStack stack = new ItemStack(Items.IRON_INGOT);

	@Override
	public void renderAtCentre(final TileEntityEnamel te, final float partialTicks, final int destroyStage, final float alpha) {
	}

}
