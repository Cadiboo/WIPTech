package cadiboo.wiptech.util;

public class ModMaterialProperties {

	private final boolean hasOre;
	private final boolean hasBlock;
	private final boolean hasResouce;
	private final boolean hasArmor;
	private final boolean hasTools;
	private final float hardness;
	private final int conductivity;

	public ModMaterialProperties(final boolean hasOre, final boolean hasBlock, final boolean hasResouce, final boolean hasArmor, final boolean hasTools, final float MOHS_Hardness, final int thermalConductivityAt20DegreesCelsius) {
		this.hasOre = hasOre;
		this.hasBlock = hasBlock;
		this.hasResouce = hasResouce;
		this.hasArmor = hasArmor;
		this.hasTools = hasTools;
		this.hardness = MOHS_Hardness;
		this.conductivity = thermalConductivityAt20DegreesCelsius;
	}

	public final boolean hasOre() {
		return this.hasOre;
	}

	public final boolean hasBlock() {
		return this.hasBlock;
	}

	public final boolean hasResource() {
		return this.hasResouce;
	}

	public final boolean hasArmor() {
		return this.hasArmor;
	}

	public final boolean hasTools() {
		return this.hasTools;
	}

	public final float getHardness() {
		return this.hardness;
	}

	public final int getConductivity() {
		return this.conductivity;
	}

	public final boolean hasRailgunSlug() {
		return this.hardness >= 4;
	}

	public final boolean hasWire() {
		return this.conductivity > 50;
	}

	public final boolean hasEnamel() {
		return this.hasWire();
	}

	public final boolean hasCoil() {
		return this.hasWire();
	}

	public final boolean hasRail() {
		return this.hasWire();
	}

}
