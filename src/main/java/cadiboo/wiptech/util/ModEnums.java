package cadiboo.wiptech.util;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import cadiboo.wiptech.WIPTech;
import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * 
 * @author Cadiboo
 *
 */

public class ModEnums {

	protected interface EnumNameFormattable {

		/**
		 * Converts the name to lowercase as per {@link java.lang.String#toLowerCase()
		 * String.toLowerCase}.
		 */
		public default String getNameLowercase() {
			return this.name().toLowerCase();
		}

		/**
		 * Converts the name to uppercase as per {@link java.lang.String#toUpperCase()
		 * String.toUpperCase}.
		 */
		public default String getNameUppercase() {
			return this.name().toUpperCase();
		}

		/**
		 * Capitalizes the name of the material as per
		 * {@link org.apache.commons.lang3.StringUtils#capitalize(String)
		 * StringUtils.capitalize}.
		 */
		public default String getNameFormatted() {
			return StringUtils.capitalize(this.name().toLowerCase());
		}

		public String name(); // not exactly hacky, but this method is provided by enum

	}

	/**
	 * 
	 * 
	 * MOHS Hardness from <a href=
	 * "https://en.wikipedia.org/wiki/Mohs_scale_of_mineral_hardness">Wikipedia</a>
	 * and <a href=
	 * "http://periodictable.com/Properties/A/MohsHardness.v.html">Periodictable</a>
	 * 
	 * @author Cadiboo
	 *
	 */
	public static enum ModMaterials implements EnumNameFormattable {

		/* @formatter:off */
		/*													  material			ore	block ingot armor tools hard   cond	para */
		URANIUM				(0,		new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 6.00f, 24,	false)),
		TUNGSTEN			(1,		new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 7.50f, 173,	false)),
		TUNGSTEN_CARBITE	(2,		new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 9.00f, 173,	false)),
		TITANIUM			(3,		new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 6.00f, 23,	false)),
		TIN					(4,		new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 1.50f, 68,	false)),
		THORIUM				(5,		new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 3.00f, 42,	false)),
		SILVER				(6,		new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 2.50f, 407,	false)),
		PLATINUM			(7,		new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 3.50f, 73,	false)),
		PLUTONIUM			(8,		new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 0.01f, 8,		false)),
		OSMIUM				(9,		new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 7.00f, 61,	false)),
		NICKEL				(10,	new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 4.00f, 90,	false)),
		STEEL				(11,	new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 4.50f, 54,	false)),
		ALUMINUM			(12,	new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 2.75f, 204,	false)),
		COPPER				(13,	new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 3.00f, 386,	false)),
		GOLD				(14,	new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 2.50f, 315,	false)),
		IRON				(15,	new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 4.00f, 73,	false)),
		LEAD				(16,	new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 1.50f, 35,	false)),
		GALLIUM				(17,	new ModMaterialProperties(Material.IRON,	true, true, true, true, true, 1.50f, 29,	false));
		
		/* @formatter:on */

		private final int					id;
		private final ModMaterialProperties	properties;
		private final ArmorMaterial			armorMaterial;
		private final ToolMaterial			toolMaterial;

		private ModMaterials(final int idIn, final ModMaterialProperties propertiesIn) {
			this.id = idIn;
			this.properties = propertiesIn;

			if (this.getProperties().hasArmor()) {
				String name = this.getNameUppercase();

				String textureName = new ResourceLocation(this.getResouceLocationDomain(), getNameLowercase()).toString();

				int durability = Math.round(this.getProperties().getHardness() * ModReference.ARMOR_MATERIAL_HARDNESS_MULTIPLIER);

				int[] reductionAmounts = new int[4];
				Arrays.fill(reductionAmounts, Math.round(this.getProperties().getHardness() / 2f));

				int enchantability = Math.round(this.getProperties().getConductivity() / 10f);

				SoundEvent soundOnEquip = SoundEvents.ITEM_ARMOR_EQUIP_IRON;

				float toughness = Math.round(this.getProperties().getHardness() / 5f);

				this.armorMaterial = EnumHelper.addArmorMaterial(name, textureName, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
			} else
				this.armorMaterial = ItemArmor.ArmorMaterial.LEATHER;

			if (this.getProperties().hasTools()) {

				String name = this.getNameUppercase();
				int harvestLevel = Math.min(3, Math.round(this.getProperties().getHardness() / 3f));
				int maxUses = Math.round(this.getProperties().getHardness() * 150f);
				float efficiency = this.getProperties().getHardness();
				float damageVsEntity = this.getProperties().getHardness();
				int enchantability = Math.round(this.getProperties().getConductivity() / 10f);

				this.toolMaterial = EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damageVsEntity, enchantability);
			} else
				this.toolMaterial = ToolMaterial.WOOD;
		}

		public final int getId() {
			return this.id;
		}

		public final ModMaterialProperties getProperties() {
			return properties;
		}

		public final ArmorMaterial getArmorMaterial() {
			return this.armorMaterial;
		}

		public final ToolMaterial getToolMaterial() {
			return this.toolMaterial;
		}

		/**
		 * if the item already exists in the registry, overwrite it
		 */
		public final String getResouceLocationDomain() {
			for (Field field : ForgeRegistries.class.getFields()) {
				if (!(field.getType().isAssignableFrom(IForgeRegistry.class)))
					continue;
				IForgeRegistry registry;
				try {
					registry = (IForgeRegistry) field.get(ForgeRegistries.class);

					for (String type : properties.getPotentialVanillaTypes())
						if (registry.containsKey(new ResourceLocation("minecraft", getNameLowercase() + "_" + type)))
							return "minecraft";
				} catch (IllegalArgumentException | IllegalAccessException e) {
					WIPTech.error("Error that should never have happened!!!!", e.getMessage());
				}

			}
			return ModReference.ID;
		}

	}

	public static enum BlockItemTypes implements EnumNameFormattable {

		/* @formatter:off */
		NUGGET		(0),
		INGOT		(1);
		/* @formatter:on */

		private final int id;

		BlockItemTypes(int idIn) {
			this.id = idIn;
		}
	}

}
