package cadiboo.wiptech.client;

import cadiboo.wiptech.IProxy;
import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockBase;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Rails;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.provider.ModularWeaponProvider;
import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.energy.CapabilityEnergy;

public class Proxy implements IProxy {

	public static final ItemStack PLASMA_GUN_STACK = new ItemStack(Items.PLASMA_GUN);

	@Override
	public void logLogicalSide() {
		WIPTech.logger.info("Logical Side: Client");
	}

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

			ItemStack plasmaGun = new ItemStack(Items.PLASMA_GUN);
			plasmaGun.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null).setCoil(Coils.GOLD)
					.setRail(Rails.GOLD).setCircuit(Circuits.OVERCLOCKED);
			((CustomEnergyStorage) plasmaGun.getCapability(CapabilityEnergy.ENERGY, null))
					.setEnergyStored(ModularWeaponProvider.WEAPON_ENERGY_CAPACITY);
			list.add(plasmaGun);
		}

	}.setBackgroundImageName("item_search.png");

	@Override
	public void addToCreativeTab() {
		for (Item item : Items.ITEMS) {
			item.setCreativeTab(modTab);
		}

		for (Block block : Blocks.BLOCKS) {
			if ((block instanceof BlockBase)) {
				if (!((BlockBase) block).isHiddenBlock()) {
					block.setCreativeTab(modTab);
				}
			}
		}

		WIPTech.logger.info("createCreativeTab - Added all Items and Blocks to "
				+ WIPTech.proxy.localize(new StringBuilder().append(modTab).append(".name").toString(), new Object[0])
				+ " Tab");
	}

	@Override
	public String getSide() {
		return "Client";
	}

}
