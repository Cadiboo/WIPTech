package cadiboo.wiptech.material;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.util.ModEnums.BlockItemType;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class GemProperties extends ModMaterialProperties {

	public static final BlockRenderLayer[] BLOCK_RENDER_LAYERS = new BlockRenderLayer[]{BlockRenderLayer.SOLID, BlockRenderLayer.TRANSLUCENT};
	public static final AxisAlignedBB DEFAULT_BOUNDING_BOX = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.2, 0.8);
	public static final BiFunction<BlockItemType, EnumFacing, AxisAlignedBB> DEFAULT_GET_BOUNDING_BOX = (final BlockItemType type, final EnumFacing facing) -> {
		return DEFAULT_BOUNDING_BOX;
	};
	public static final String RESOURCE_SUFFIX = "";
	public static final String RESOURCE_PIECE_SUFFIX = "shard";

	public GemProperties(final boolean hasOre, final float MOHS_Hardness, final int thermalConductivityAt20DegreesCelsius, @Nullable final Supplier<Item> getOreDrop, @Nonnull @MethodsReturnNonnullByDefault final BiFunction<Integer, Random, Integer> getQuantityDroppedWithBonus) {
		this(hasOre, MOHS_Hardness, thermalConductivityAt20DegreesCelsius, getOreDrop, getQuantityDroppedWithBonus, DEFAULT_GET_BOUNDING_BOX);
	}

	public GemProperties(final boolean hasOre, final float MOHS_Hardness, final int thermalConductivityAt20DegreesCelsius, @Nullable final Supplier<Item> getOreDrop, @Nonnull @MethodsReturnNonnullByDefault final BiFunction<Integer, Random, Integer> getQuantityDroppedWithBonus, @Nullable final BiFunction<BlockItemType, EnumFacing, AxisAlignedBB> getBoundingBox) {
		super(hasOre, true, true, RESOURCE_SUFFIX, true, RESOURCE_PIECE_SUFFIX, true, true, true, true, true, true, true, true, true, true, MOHS_Hardness, thermalConductivityAt20DegreesCelsius, getOreDrop, BLOCK_RENDER_LAYERS, getQuantityDroppedWithBonus, getBoundingBox);
	}

}
