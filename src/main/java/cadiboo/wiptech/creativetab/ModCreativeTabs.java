package cadiboo.wiptech.creativetab;

import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.util.ModReference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Modified by Cadiboo
 * @author jabelar
 */

public class ModCreativeTabs {

	/** instantiate creative tabs */
	public static final CustomCreativeTab CREATIVE_TAB = new CustomCreativeTab(ModReference.MOD_ID, new ItemStack(ModItems.FLAMETHROWER), true);

	/**
	 * This class is used for an extra tab in the creative inventory. Many mods like to group their special items and blocks in a dedicated tab although it is also perfectly acceptable to put them in the vanilla tabs where it makes sense.
	 */
	public static class CustomCreativeTab extends CreativeTabs {

		private final ItemStack iconStack;
		private final boolean hasSearchBar;

		public CustomCreativeTab(final String name, final ItemStack iconStack, final boolean hasSearchBar) {
			super(name);
			this.iconStack = iconStack;
			this.hasSearchBar = hasSearchBar;
		}

		/**
		 * gets the {@link net.minecraft.item.ItemStack ItemStack} to display for the tab's icon
		 */
		@SideOnly(Side.CLIENT)
		@Override
		public ItemStack getTabIconItem() {
			return this.iconStack;
		}

		/**
		 * Useful for adding extra items such as full variants of energy related items
		 */
		@SideOnly(Side.CLIENT)
		@Override
		public void displayAllRelevantItems(final NonNullList<ItemStack> items) {
			super.displayAllRelevantItems(items);
		}

		@Override
		public boolean hasSearchBar() {
			return this.hasSearchBar;
		}

		@Override
		public String getBackgroundImageName() {
			if (this.hasSearchBar) {
				return "item_search.png";
			} else {
				return super.getBackgroundImageName();
			}
		}

	}
}