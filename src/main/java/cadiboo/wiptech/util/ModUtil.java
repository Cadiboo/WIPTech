package cadiboo.wiptech.util;

import org.apache.commons.lang3.StringUtils;

import cadiboo.wiptech.creativetab.ModCreativeTabs;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

	public static final void setNameForMaterialItem(Item item, ModMaterials materialIn, String nameSuffix) {

		ResourceLocation name = new ResourceLocation(materialIn.getResouceLocationDomain(nameSuffix.toLowerCase(), ForgeRegistries.ITEMS), materialIn.getVanillaNameLowercase(nameSuffix) + "_"
				+ nameSuffix);
		item.setRegistryName(name);
		Item overriddenItem = ForgeRegistries.ITEMS.getValue(name);
		item.setUnlocalizedName(overriddenItem != null ? overriddenItem.getUnlocalizedName().replace("item.", "") : name.getResourcePath());
		// item.setUnlocalizedName("shovelIron");
	}

	public static final void setNameForMaterialBlock(Block block, ModMaterials materialIn, String nameSuffix) {

		ResourceLocation name = new ResourceLocation(materialIn.getResouceLocationDomain(nameSuffix.toLowerCase(), ForgeRegistries.ITEMS), materialIn.getNameLowercase() + "_" + nameSuffix);
		block.setRegistryName(name);
		block.setUnlocalizedName(materialIn.getResouceLocationDomain(nameSuffix.toLowerCase(), ForgeRegistries.ITEMS) + "." + name.getResourcePath());
	}

	public static final CreativeTabs[] getCreativeTabs(Item item) {
		return new CreativeTabs[] { item.getCreativeTab(), ModCreativeTabs.CREATIVE_TAB, CreativeTabs.SEARCH };
	}

	public static final void setCreativeTab(Item item) {
		if (item.getCreativeTab() == null)
			item.setCreativeTab(ModCreativeTabs.CREATIVE_TAB);
	}

	public static final int getMaterialLightValue(ModMaterials material) {
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
	public static final double map(double input_start, double input_end, double output_start, double output_end, double input) {
		double input_range = input_end - input_start;
		double output_range = output_end - output_start;

		return (input - input_start) * output_range / input_range + output_start;
	}

	/**
	 * Examples:<br>
	 * (TileEntitySuperAdvancedFurnace, "TileEntity") -> super_advanced_furnace<br>
	 * (EntityPortableGenerator, "Entity") -> portable_generator<br>
	 * (TileEntityPortableGenerator, "Entity") -> tile_portable_generator<br>
	 * (EntityPortableEntityGeneratorEntity, "Entity") -> portable_generator<br>
	 * 
	 * @param clazz      the class
	 * @param removeType the string to be removed from the class's name
	 * @return the recommended registry name for the class
	 */
	public static final String getRegistryNameForClass(Class clazz, String removeType) {
		return org.apache.commons.lang3.StringUtils.uncapitalize(clazz.getSimpleName().replace(removeType, "")).replaceAll("([A-Z])", "_$1").toLowerCase();
	}

	/**
	 * Examples:<br>
	 * super_advanced_furnace -> Super Advanced Furnace<br>
	 * portable_generator -> Portable Generator<br>
	 * tile_portable_generator -> Tile Portable Generator <br>
	 * 
	 * @param clazz      the class
	 * @param removeType the string to be removed from the class's name
	 * @return the recommended registry name for the class
	 */
	public static final String getLocalisedName(String unlocalised) {
		String[] strs = unlocalised.split("_");
		for (int i = 0; i < strs.length; i++) {
			strs[i] = org.apache.commons.lang3.StringUtils.capitalize(strs[i]);
		}
		String localisedName = String.join(" ", strs);
		return localisedName;
	}

	public static final String getRomanNumeralFor(int number) {
		String[] normal = { "I", "V", "X", "L", "C", "M" };
		String[] thousands = { "I̅", "V̅", "X̅", "L̅", "C̅", "M̅" };

		String roman = "";

		if (number > 2000) {
			String str = getRomanNumeralFor((int) (number / 1000f));
			for (int i = 0; i < normal.length; i++) {
				str.replaceAll(normal[i], thousands[i]);
			}
			roman += str;
			number -= (int) (number / 1000f);
		} else if (number > 1000) {
			roman += normal[5];
			number -= 1000;
		}
		if (number > 100) {
			for (int i = 0; i < (int) (number / 100f); i++) {
				roman += normal[4];
				number -= (int) (number / 100f);
			}
		}

		if (number > 50) {
			for (int i = 0; i < (int) (number / 50f); i++) {
				roman += normal[3];
				number -= (int) (number / 50f);
			}
		}

		if (number > 10) {
			for (int i = 0; i < (int) (number / 10f); i++) {
				roman += normal[2];
				number -= (int) (number / 10f);
			}
		}

		if (number > 5) {
			for (int i = 0; i < (int) (number / 5f); i++) {
				roman += normal[1];
				number -= (int) (number / 5f);
			}
		}

		if (number > 1) {
			for (int i = 0; i < (int) (number / 1f); i++) {
				roman += normal[0];
				number -= (int) (number / 1f);
			}
		}

		return roman;

	}

	/**
	 * Generic & dynamic version of
	 * {@link Container#transferStackInSlot(EntityPlayer, int)}<br>
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this
	 * moves the stack between the player inventory and the other inventory(s).
	 * 
	 * @param player
	 * @param index
	 * @param container the container to apply the transfer to
	 * @return
	 */
	public static final ItemStack transferStackInSlot(EntityPlayer player, int index, Container container) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = container.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int containerSlots = container.inventorySlots.size() - player.inventory.mainInventory.size();
			if (index < containerSlots) {
				if (!mergeItemStack(itemstack1, containerSlots, container.inventorySlots.size(), true, container)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(itemstack1, 0, containerSlots, false, container)) {
				return ItemStack.EMPTY;
			}
			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}
		return itemstack;
	}

	/**
	 * Exact copy of {@link net.minecraft.inventory.Container#mergeItemStack} with
	 * the same javadoc (improved for readability)<br>
	 * Merges provided ItemStack with the first avaliable one in the
	 * container/player inventor between startIndex (included) and endIndex
	 * (excluded).<br>
	 * <font color="#FDCA42"> ⚠WARNING⚠: The Container implementation does not check
	 * if the item is valid for the slot! </font>
	 * 
	 * @param stack            the stack to merge
	 * @param startIndex
	 * @param endIndex
	 * @param reverseDirection
	 * @param container        the container to apply the merge to
	 */
	public static final boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection, Container container) {
		boolean flag = false;
		int i = startIndex;

		if (reverseDirection) {
			i = endIndex - 1;
		}

		if (stack.isStackable()) {
			while (!stack.isEmpty()) {
				if (reverseDirection) {
					if (i < startIndex) {
						break;
					}
				} else if (i >= endIndex) {
					break;
				}

				Slot slot = container.inventorySlots.get(i);
				ItemStack itemstack = slot.getStack();

				if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(
						stack, itemstack)) {
					int j = itemstack.getCount() + stack.getCount();
					int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());

					if (j <= maxSize) {
						stack.setCount(0);
						itemstack.setCount(j);
						slot.onSlotChanged();
						flag = true;
					} else if (itemstack.getCount() < maxSize) {
						stack.shrink(maxSize - itemstack.getCount());
						itemstack.setCount(maxSize);
						slot.onSlotChanged();
						flag = true;
					}
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		if (!stack.isEmpty()) {
			if (reverseDirection) {
				i = endIndex - 1;
			} else {
				i = startIndex;
			}

			while (true) {
				if (reverseDirection) {
					if (i < startIndex) {
						break;
					}
				} else if (i >= endIndex) {
					break;
				}

				Slot slot1 = container.inventorySlots.get(i);
				ItemStack itemstack1 = slot1.getStack();

				if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
					if (stack.getCount() > slot1.getSlotStackLimit()) {
						slot1.putStack(stack.splitStack(slot1.getSlotStackLimit()));
					} else {
						slot1.putStack(stack.splitStack(stack.getCount()));
					}

					slot1.onSlotChanged();
					flag = true;
					break;
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		return flag;
	}

}
