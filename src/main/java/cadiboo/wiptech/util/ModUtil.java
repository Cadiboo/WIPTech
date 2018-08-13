package cadiboo.wiptech.util;

import org.apache.commons.lang3.StringUtils;

import cadiboo.wiptech.creativetab.ModCreativeTabs;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModUtil {

	/**
	 * gets the game name in uppercase
	 */
	public static final String getSlotGameNameUppercase(final EntityEquipmentSlot slotIn) {
		switch (slotIn) {
		case CHEST:
			return "CHESTPLATE";
		case FEET:
			return "BOOTS";
		case HEAD:
			return "HELMET";
		case LEGS:
			return "LEGGINGS";
		default:
			return slotIn.name();
		}
	}

	/**
	 * Converts the game name to lowercase as per
	 * {@link java.lang.String#toLowerCase() String.toLowerCase}.
	 */
	public static final String getSlotGameNameLowercase(final EntityEquipmentSlot slotIn) {
		return getSlotGameNameUppercase(slotIn).toLowerCase();
	}

	/**
	 * Capitalizes the game name as per
	 * {@link org.apache.commons.lang3.StringUtils#capitalize(String)
	 * StringUtils.capitalize}.
	 */
	public static final String getSlotGameNameFormatted(final EntityEquipmentSlot slotIn) {
		return StringUtils.capitalize(getSlotGameNameLowercase(slotIn));
	}

	public static void setNameForMaterialItem(Item item, ModMaterials materialIn, String nameSuffix) {

		ResourceLocation name = new ResourceLocation(materialIn.getResouceLocationDomain(nameSuffix.toLowerCase(), ForgeRegistries.ITEMS), materialIn.getVanillaNameLowercase(nameSuffix) + "_"
				+ nameSuffix);
		item.setRegistryName(name);
		Item overriddenItem = ForgeRegistries.ITEMS.getValue(name);
		item.setUnlocalizedName(overriddenItem != null ? overriddenItem.getUnlocalizedName().replace("item.", "") : name.getResourcePath());
		// item.setUnlocalizedName("shovelIron");
	}

	public static void setNameForMaterialBlock(Block block, ModMaterials materialIn, String nameSuffix) {

		ResourceLocation name = new ResourceLocation(materialIn.getResouceLocationDomain(nameSuffix.toLowerCase(), ForgeRegistries.ITEMS), materialIn.getNameLowercase() + "_" + nameSuffix);
		block.setRegistryName(name);
		block.setUnlocalizedName(materialIn.getResouceLocationDomain(nameSuffix.toLowerCase(), ForgeRegistries.ITEMS) + "." + name.getResourcePath());
	}

	public static CreativeTabs[] getCreativeTabs(Item item) {
		return new CreativeTabs[] { item.getCreativeTab(), ModCreativeTabs.CREATIVE_TAB, CreativeTabs.SEARCH };
	}

	public static void setCreativeTab(Item item) {
		if (item.getCreativeTab() == null)
			item.setCreativeTab(ModCreativeTabs.CREATIVE_TAB);
	}

	public static int getMaterialLightValue(ModMaterials material) {
		switch (material) {
		case PLUTONIUM:
			return 6;
		case URANIUM:
			return 8;
		default:
			return 0;
		}
	}

	/**
	 * https://stackoverflow.com/a/5732117
	 * 
	 * @param input_start
	 * @param input_end
	 * @param output_start
	 * @param output_end
	 * @param input
	 * @return
	 */
	public static double map(double input_start, double input_end, double output_start, double output_end, double input) {
		double input_range = input_end - input_start;
		double output_range = output_end - output_start;

		return (input - input_start) * output_range / input_range + output_start;
	}

}
