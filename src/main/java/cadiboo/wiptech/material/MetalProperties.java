package cadiboo.wiptech.material;

import java.util.Random;
import java.util.function.BiFunction;

import cadiboo.wiptech.util.ModEnums.BlockItemType;
import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class MetalProperties extends ModMaterialProperties {

	public static final String RESOURCE_SUFFIX = "ingot";
	public static final String RESOURCE_PIECE_SUFFIX = "nugget";

	public static final AxisAlignedBB INGOT_EW_AABB = new AxisAlignedBB(3d / 16d, 0, 6d / 16d, 1d - (3d / 16d), 3d / 16d, 1d - (6d / 16d));
	public static final AxisAlignedBB INGOT_NS_AABB = new AxisAlignedBB(6d / 16d, 0, 3d / 16d, 1d - (6d / 16d), 3d / 16d, 1d - (3d / 16d));

	public static final AxisAlignedBB NUGGET_EW_AABB = new AxisAlignedBB(4d / 16d, 0, 5d / 16d, 1d - (4d / 16d), 1d / 16d, 1d - (5d / 16d));
	public static final AxisAlignedBB NUGGET_NS_AABB = new AxisAlignedBB(5d / 16d, 0, 4d / 16d, 1d - (5d / 16d), 1d / 16d, 1d - (4d / 16d));

	public static final BlockRenderLayer[] BLOCK_RENDER_LAYERS = new BlockRenderLayer[]{BlockRenderLayer.SOLID};
	public static final BiFunction<BlockItemType, EnumFacing, AxisAlignedBB> GET_BOUNDING_BOX = (final BlockItemType type, final EnumFacing facing) -> {

		switch (type) {
			case RESOURCE :
				switch (facing) {
					case EAST :
					case WEST :
						return INGOT_EW_AABB;
					case NORTH :
					case SOUTH :
						return INGOT_NS_AABB;
					default :
						break;
				}
			case RESOURCE_PIECE :
				switch (facing) {
					case EAST :
					case WEST :
						return NUGGET_EW_AABB;
					case NORTH :
					case SOUTH :
						return NUGGET_NS_AABB;
					default :
						break;
				}
		}
		return Block.FULL_BLOCK_AABB;

	};
	public static final BiFunction<Integer, Random, Integer> GET_QUANTITY_DROPPED_WITH_BONUS = (fortune, random) -> {
		return 1;
	};

	public MetalProperties(final boolean hasOre, final float MOHS_Hardness, final int thermalConductivityAt20DegreesCelsius) {
		super(hasOre, true, true, RESOURCE_SUFFIX, true, RESOURCE_PIECE_SUFFIX, true, true, true, true, true, true, true, true, true, hasOre, MOHS_Hardness, thermalConductivityAt20DegreesCelsius, null, BLOCK_RENDER_LAYERS, GET_QUANTITY_DROPPED_WITH_BONUS, GET_BOUNDING_BOX);
	}

}
