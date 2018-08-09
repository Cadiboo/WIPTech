package cadiboo.wiptech.util;

public class ModMaterialProperties {

	private boolean	ore;
	private boolean	block;
	private boolean	ingotAndNugget;
	private boolean	armor;
	private boolean	tools;
	private float	hardness;
	private int		conductivity;

	public ModMaterialProperties(final boolean hasOre, final boolean hasBlock, final boolean hasIngotAndNugget, final boolean hasArmor, final boolean hasTools, final float MOHS_Hardness,
			final int thermalConductivityAt20DegreesCelsius) {
		this.ore = hasOre;
		this.block = hasBlock;
		this.ingotAndNugget = hasIngotAndNugget;
		this.armor = hasArmor;
		this.tools = hasTools;
		this.hardness = MOHS_Hardness;
		this.conductivity = thermalConductivityAt20DegreesCelsius;
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

	public final boolean hasRailgunSlug() {
		return this.hardness >= 4;
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
