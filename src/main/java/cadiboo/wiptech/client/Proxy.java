package cadiboo.wiptech.client;

import cadiboo.wiptech.IProxy;
import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockBase;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Items;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Proxy implements IProxy {

	@Override
	public void logLogicalSide() {
		WIPTech.logger.info("Logical Side: Client");
	}

	@Override
	public String localize(String unlocalized, Object... args) {
		return I18n.format(unlocalized, args);
	}

	public static CreativeTabs modTab = new CreativeTabs("wiptechtab")
	{
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(Blocks.COPPER_ORE);
		}

		@Override
		public boolean hasSearchBar()
		{
			return true;
		}
	}.setBackgroundImageName("item_search.png");

	@Override
	public void addToCreativeTab()
	{
		for(Item item : Items.ITEMS) {
			item.setCreativeTab(modTab);
		}

		for(Block block :Blocks.BLOCKS) {
			if ((block instanceof BlockBase))
			{
				if (!((BlockBase)block).isHiddenBlock()) {
					block.setCreativeTab(modTab);
				}
			}
		}

		WIPTech.logger.info("createCreativeTab - Added all Items and Blocks to " + WIPTech.proxy.localize(new StringBuilder().append(modTab).append(".name").toString(), new Object[0]) + " Tab");
	}

}
