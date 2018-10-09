package cadiboo.wiptech.material;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.util.ModEnums.BlockItemType;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class ModMaterialProperties {

	private final boolean hasOre;
	private final boolean hasBlock;
	private final boolean hasResource;
	@Nullable
	final String resourceSuffix;
	private final boolean hasResourcePiece;

	@Nullable
	final String resourcePieceSuffix;
	private final boolean hasHelmet;
	private final boolean hasChestplate;
	private final boolean hasLeggings;
	private final boolean hasBoots;
	private final boolean hasHorseArmor;
	private final boolean hasPickaxe;
	private final boolean hasAxe;
	private final boolean hasSword;
	private final boolean hasShovel;
	private final boolean hasHoe;
	private final float hardness;
	private final int conductivity;
	/** if null reverts to getItemFromBlock(block) */
	@Nullable
	private final Supplier<Item> getOreDrop;
	@Nonnull
	private final BlockRenderLayer[] blockRenderLayers;
	@Nonnull
	private final BiFunction<Integer, Random, Integer> getQuantityDroppedWithBonus;
	@Nonnull
	private final BiFunction<BlockItemType, EnumFacing, AxisAlignedBB> getBoundingBox;

	public ModMaterialProperties(
	/*@formatter:off*/
		final boolean hasOre,
		final boolean hasBlock,
		final boolean hasResource,
		@Nullable
		final String resourceSuffix,
		final boolean hasResourcePiece,
		@Nullable
		final String resourcePieceSuffix,
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
		final Supplier<Item> getOreDrop,
		@Nullable
		final BlockRenderLayer[] blockRenderLayers,
		@Nonnull
		@MethodsReturnNonnullByDefault
		final BiFunction<Integer, Random, Integer> getQuantityDroppedWithBonus,
		@Nullable
		final BiFunction<BlockItemType, EnumFacing, AxisAlignedBB>  getBoundingBox
	/*@formatter:on*/
	) {

		this.hasOre = hasOre;
		this.hasBlock = hasBlock;
		this.hasResource = hasResource;
		this.resourceSuffix = resourceSuffix;
		this.hasResourcePiece = hasResourcePiece;
		this.resourcePieceSuffix = resourcePieceSuffix;
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
		if (blockRenderLayers == null) {
			this.blockRenderLayers = new BlockRenderLayer[]{null};
		} else {
			this.blockRenderLayers = blockRenderLayers;
		}
		this.getQuantityDroppedWithBonus = getQuantityDroppedWithBonus;
		if (getBoundingBox == null) {
			this.getBoundingBox = (final BlockItemType type, final EnumFacing facing) -> {
				return Block.FULL_BLOCK_AABB;
			};
		} else {
			this.getBoundingBox = getBoundingBox;
		}

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

	public String getResourceSuffix() {
		return this.resourceSuffix;
	}

	public boolean hasResourcePiece() {
		return this.hasResourcePiece;
	}

	public String getResourcePieceSuffix() {
		return this.resourcePieceSuffix;
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
		// return Items.ACACIA_BOAT;
		return this.getOreDrop.get();
	}

	@Nonnull
	public List<BlockRenderLayer> getBlockRenderLayers() {
		// return Arrays.asList(new BlockRenderLayer[]{BlockRenderLayer.SOLID});
		return Arrays.asList(this.blockRenderLayers);
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

	public int getQuantityDroppedWithBonus(final int fortune, final Random random) {
		// return 1;
		return this.getQuantityDroppedWithBonus.apply(fortune, random);
	}

	public AxisAlignedBB getBoundingBox(final BlockItemType type, final EnumFacing facing) {
		// return Block.FULL_BLOCK_AABB;
		return this.getBoundingBox.apply(type, facing);
	}

	// TODO @NonDepreciated
	// public AxisAlignedBB getBoundingBox(final BlockItemType type) {
	// return Block.FULL_BLOCK_AABB;
	// }

}
