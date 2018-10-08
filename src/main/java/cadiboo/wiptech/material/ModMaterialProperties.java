package cadiboo.wiptech.material;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.item.Item;

public class ModMaterialProperties {

	protected final boolean hasOre;
	protected final boolean hasBlock;
	protected final boolean hasResource;
	protected final boolean hasResourcePiece;
	protected final boolean hasHelmet;
	protected final boolean hasChestplate;
	protected final boolean hasLeggings;
	protected final boolean hasBoots;
	protected final boolean hasHorseArmor;
	protected final boolean hasPickaxe;
	protected final boolean hasAxe;
	protected final boolean hasSword;
	protected final boolean hasShovel;
	protected final boolean hasHoe;
	protected final float hardness;
	protected final int conductivity;
	/** if null reverts to getItemFromBlock(block) */
	@Nullable
	protected final Function<Void, Item> getOreDrop;

	public ModMaterialProperties(
	/*@formatter:off*/
		final boolean hasOre,
		final boolean hasBlock,
		final boolean hasResource,
		final boolean hasResourcePiece,
		final boolean hasHelmet,
		final boolean hasChestplate,
		final boolean hasLeggings,
		final boolean hasBoots,
		final boolean hasHorseArmor,
		final boolean hasPickaxe,
		final boolean hasAxe,
		final boolean hasSword,
		final boolean hasShovel,
		final boolean hasHoe,
		final float MOHS_Hardness,
		final int thermalConductivityAt20DegreesCelsius,
		@Nullable
		final Function<Void, Item> getOreDrop
	/*@formatter:on*/
	) {

		this.hasOre = hasOre;
		this.hasBlock = hasBlock;
		this.hasResource = hasResource;
		this.hasResourcePiece = hasResourcePiece;
		this.hasHelmet = hasHelmet;
		this.hasChestplate = hasChestplate;
		this.hasLeggings = hasLeggings;
		this.hasBoots = hasBoots;
		this.hasHorseArmor = hasHorseArmor;
		this.hasPickaxe = hasPickaxe;
		this.hasAxe = hasAxe;
		this.hasSword = hasSword;
		this.hasShovel = hasShovel;
		this.hasHoe = hasHoe;
		this.hardness = MOHS_Hardness;
		this.conductivity = thermalConductivityAt20DegreesCelsius;
		this.getOreDrop = getOreDrop;
	}

	public boolean hasOre() {
		return this.hasOre;
	}

	public boolean hasBlock() {
		return this.hasBlock;
	}

	public boolean hasResource() {
		return this.hasResource;
	}

	public boolean hasResourcePiece() {
		return this.hasResourcePiece;
	}

	public boolean hasHelmet() {
		return this.hasHelmet;
	}

	public boolean hasChestplate() {
		return this.hasChestplate;
	}

	public boolean hasLeggings() {
		return this.hasLeggings;
	}

	public boolean hasBoots() {
		return this.hasBoots;
	}

	public boolean hasHorseArmor() {
		return this.hasHorseArmor;
	}

	public boolean hasPickaxe() {
		return this.hasPickaxe;
	}

	public boolean hasAxe() {
		return this.hasAxe;
	}

	public boolean hasSword() {
		return this.hasSword;
	}

	public boolean hasShovel() {
		return this.hasShovel;
	}

	public boolean hasHoe() {
		return this.hasHoe;
	}

	public float getHardness() {
		return this.hardness;
	}

	public int getConductivity() {
		return this.conductivity;
	}

	public boolean hasRailgunSlug() {
		return this.hardness >= 4;
	}

	public boolean hasWire() {
		return this.conductivity > 50;
	}

	public boolean hasEnamel() {
		return this.hasWire();
	}

	public boolean hasCoil() {
		return this.hasWire();
	}

	public boolean hasRail() {
		return this.hasWire();
	}

	@Nullable
	public Item getOreDrop() {
		return this.getOreDrop.apply(null);
	}

	@Override
	public String toString() {
		String string = super.toString() + " - ";
		string += "hasOre: " + this.hasOre();
		string += ", hasBlock: " + this.hasBlock();
		string += ", hasResource: " + this.hasResource();
		string += ", hasResourcePiece: " + this.hasResourcePiece();
		string += ", hasHelmet: " + this.hasHelmet();
		string += ", hasChestplate: " + this.hasChestplate();
		string += ", hasLeggings: " + this.hasLeggings();
		string += ", hasBoots: " + this.hasBoots();
		string += ", hasPickaxe: " + this.hasPickaxe();
		string += ", hasAxe: " + this.hasAxe();
		string += ", hasSword: " + this.hasSword();
		string += ", hasShovel: " + this.hasShovel();
		string += ", hasHoe: " + this.hasHoe();
		string += ", hardness: " + this.getHardness();
		string += ", conductivity: " + this.getConductivity();
		string += ", hasRailgunSlug: " + this.hasRailgunSlug();
		string += ", hasWire: " + this.hasWire();
		string += ", hasEnamel: " + this.hasEnamel();
		string += ", hasCoil: " + this.hasCoil();
		string += ", hasRail: " + this.hasRail();
		return string;
	}

}
