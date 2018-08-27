package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ExistsForDebugging;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public abstract class ModMaterialBlock extends ModBlock {

	public ModMaterialBlock(ModMaterials materialIn, String nameSuffix) {
		super(Material.IRON, new ResourceLocation(materialIn.getResouceLocationDomain(nameSuffix.toLowerCase(), ForgeRegistries.BLOCKS), materialIn.getNameLowercase() + "_" + nameSuffix), materialIn
				.getNameLowercase() + "_" + nameSuffix);
		this.blockHardness = materialIn.getProperties().getHardness();
	}

	public abstract ModMaterials getModMaterial();

	@Override
	public final int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (getDebugOres())
			return 14;
		return ModUtil.getMaterialLightValue(this.getModMaterial());
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (getDebugOres())
			return 1;
		return ModUtil.getMaterialLightValue(this.getModMaterial());
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		if (getDebugOres())
			return false;
		return super.isOpaqueCube(state);
	}

	@ExistsForDebugging
	private boolean getDebugOres() {
		return false;
	}

}
