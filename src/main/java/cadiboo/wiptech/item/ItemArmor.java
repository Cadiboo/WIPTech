package cadiboo.wiptech.item;

import cadiboo.wiptech.util.Reference;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.common.util.EnumHelper;

public class ItemArmor extends net.minecraft.item.ItemArmor {

	public static ArmorMaterial	COPPER		= EnumHelper.addArmorMaterial("WIPTECH_COPPER", Reference.ID + ":copper", 15, new int[] { 2, 5, 6, 2 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	public static ArmorMaterial	TIN			= EnumHelper.addArmorMaterial("WIPTECH_TIN", Reference.ID + ":tin", 10, new int[] { 2, 5, 6, 2 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	public static ArmorMaterial	ALUMINIUM	= EnumHelper.addArmorMaterial("WIPTECH_ALUMINIUM", Reference.ID + ":aluminium", 15, new int[] { 2, 5, 6, 2 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
	public static ArmorMaterial	SILVER		= EnumHelper.addArmorMaterial("WIPTECH_SILVER", Reference.ID + ":silver", 15, new int[] { 2, 5, 6, 2 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 2.0F);
	public static ArmorMaterial	OSMIUM		= EnumHelper.addArmorMaterial("WIPTECH_OSMIUM", Reference.ID + ":osmium", 20, new int[] { 2, 5, 6, 2 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 3.0F);
	public static ArmorMaterial	TUNGSTEN	= EnumHelper.addArmorMaterial("WIPTECH_TUNGSTEN", Reference.ID + ":tungsten", 30, new int[] { 2, 5, 6, 2 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 5.0F);
	public static ArmorMaterial	TITANIUM	= EnumHelper.addArmorMaterial("WIPTECH_TITANIUM", Reference.ID + ":titanium", 25, new int[] { 2, 5, 6, 2 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4F);

	public ItemArmor(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
		setRegistryName(Reference.ID, name);
		setUnlocalizedName(name);
	}

}
