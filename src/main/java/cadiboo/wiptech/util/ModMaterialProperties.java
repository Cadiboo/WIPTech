package cadiboo.wiptech.util;

import net.minecraft.block.material.Material;

public class ModMaterialProperties {

	private Material	material;
	private boolean		ore;
	private boolean		block;
	private boolean		ingotAndNugget;
	private boolean		armor;
	private boolean		tools;
	private float		hardness;
	private int			conductivity;
	private boolean		paramagnetic;

	public ModMaterialProperties(final Material materialIn, final boolean hasOre, final boolean hasBlock, final boolean hasIngotAndNugget, final boolean hasArmor, final boolean hasTools,
			final float MOHS_Hardness, final int thermalConductivityAt20DegreesCelsius, final boolean isParamagnetic) {
		this.material = materialIn;
		this.ore = hasOre;
		this.block = hasBlock;
		this.ingotAndNugget = hasIngotAndNugget;
		this.armor = hasArmor;
		this.tools = hasTools;
		this.hardness = MOHS_Hardness;
		this.conductivity = thermalConductivityAt20DegreesCelsius;
		this.paramagnetic = isParamagnetic;
	}

	public synchronized final Material getMaterial() {
		return this.material;
	}

	public synchronized final boolean hasOre() {
		return this.ore;
	}

	public synchronized final boolean hasBlock() {
		return block;
	}

	public synchronized final boolean hasIngotAndNugget() {
		return ingotAndNugget;
	}

	public synchronized final boolean hasArmor() {
		return armor;
	}

	public synchronized final boolean hasTools() {
		return tools;
	}

	public synchronized final float getHardness() {
		return hardness;
	}

	public synchronized final int getConductivity() {
		return conductivity;
	}

	public synchronized final boolean isParamagnetic() {
		return paramagnetic;
	}

	public synchronized final String[] getPotentialVanillaTypes() {
		return new String[] { "block", "ore", "ingot", "helmet", "axe" };
	}

	public synchronized final boolean hasWire() {
		return conductivity > 50;
	}

	public synchronized final boolean hasEnamel() {
		return hasWire();
	}

	public synchronized final boolean hasCoil() {
		return hasWire();
	}

	public synchronized final boolean hasRail() {
		return hasWire();
	}

//	public synchronized final String[] getPotentialTypes() {
//		return new String[] { "block", "ore", "ingot", "helmet", "chestplate", "leggings", "boots", "pickaxe", "axe",
//				"sword", "shovel", "wire", "enamel" };
//	}

}
