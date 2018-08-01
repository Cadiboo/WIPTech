package cadiboo.wiptech.block;

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
		super(Material.IRON, new ResourceLocation(materialIn.getResouceLocationDomain(nameSuffix.toLowerCase(), ForgeRegistries.BLOCKS), materialIn.getNameLowercase() + "_" + nameSuffix),
				materialIn.getNameLowercase() + "_" + nameSuffix);
	}

	public abstract ModMaterials getModMaterial();

	@Override
	public final int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return ModUtil.getMaterialLightValue(this.getModMaterial());
	}

	@Override
	public final int getLightValue(IBlockState state) {
		return ModUtil.getMaterialLightValue(this.getModMaterial());
	}

}
