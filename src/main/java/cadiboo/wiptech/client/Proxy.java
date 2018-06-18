package cadiboo.wiptech.client;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.util.IProxy;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class Proxy implements IProxy {

	public static final ItemStack PLASMA_GUN_STACK = new ItemStack(Items.PLASMA_GUN);

	@Override
	public String localize(String unlocalized, Object... args) {
		return I18n.format(unlocalized, args);
	}

	public static CreativeTabs modTab = new CreativeTabs("wiptechtab") {
		@Override
		public ItemStack getTabIconItem() {
			return PLASMA_GUN_STACK;
		}

		@Override
		public boolean hasSearchBar() {
			return true;
		}

		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> list) {
			super.displayAllRelevantItems(list);
		}

	}.setBackgroundImageName("item_search.png");

	@Override
	public void addToCreativeTab() {

		for (int i = 0; i < Items.ITEMS.length; i++) {
			Items.ITEMS[i].setCreativeTab(modTab);
		}

		for (int i = 0; i < Blocks.BLOCKS.length; i++) {
			if (Blocks.getHiddenBlocks().contains(Blocks.BLOCKS[i]))
				continue;
			Blocks.BLOCKS[i].setCreativeTab(modTab);
		}

		WIPTech.info("createCreativeTab - Added all Items and Blocks to " + WIPTech.proxy.localize(new StringBuilder().append(modTab).append(".name").toString(), new Object[0]) + " Tab");

	}

	@Override
	public String getSide() {
		return "Client";
	}

}
