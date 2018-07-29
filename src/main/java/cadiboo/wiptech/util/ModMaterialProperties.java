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

	public final Material getMaterial() {
		return this.material;
	}

	public final boolean hasOre() {
		return this.ore;
	}

	public final boolean hasBlock() {
		return block;
	}

	public final boolean hasIngotAndNugget() {
		return ingotAndNugget;
	}

	public final boolean hasArmor() {
		return armor;
	}

	public final boolean hasTools() {
		return tools;
	}

	public final float getHardness() {
		return hardness;
	}

	public final int getConductivity() {
		return conductivity;
	}

	public final boolean isParamagnetic() {
		return paramagnetic;
	}

	public final boolean hasWire() {
		return conductivity > 50;
	}

	public final boolean hasEnamel() {
		return hasWire();
	}

	public final boolean hasCoil() {
		return hasWire();
	}

	public final boolean hasRail() {
		return hasWire();
	}

}
