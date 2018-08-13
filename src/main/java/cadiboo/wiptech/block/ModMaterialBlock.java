package cadiboo.wiptech.block;

import java.util.Random;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public abstract class ModMaterialBlock extends ModBlock {

	public ModMaterialBlock(ModMaterials materialIn, String nameSuffix) {
		super(Material.IRON, new ResourceLocation(materialIn.getResouceLocationDomain(nameSuffix.toLowerCase(), ForgeRegistries.BLOCKS), materialIn.getNameLowercase() + "_" + nameSuffix), materialIn
				.getNameLowercase() + "_" + nameSuffix);
	}

	public abstract ModMaterials getModMaterial();

	@Override
	public final int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
//		return ModUtil.getMaterialLightValue(this.getModMaterial());
		return 14;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
//		return ModUtil.getMaterialLightValue(this.getModMaterial());
		return 0;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		if (true)
			return false;
//		if (getModMaterial() == ModMaterials.TUNGSTEN || getModMaterial() == ModMaterials.TITANIUM || getModMaterial() == ModMaterials.TUNGSTEN)
//			return false;
		return super.isOpaqueCube(state);
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		if (true)
			return false;
//		if (getModMaterial() == ModMaterials.TUNGSTEN || getModMaterial() == ModMaterials.TITANIUM || getModMaterial() == ModMaterials.TUNGSTEN)
//			return false;
		return super.isFullBlock(state);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		if (true)
			return false;
//		if (getModMaterial() == ModMaterials.TUNGSTEN || getModMaterial() == ModMaterials.TITANIUM || getModMaterial() == ModMaterials.TUNGSTEN)
//			return false;
		return super.isFullCube(state);
	}

	@Override
	public boolean isTranslucent(IBlockState state) {
		if (true)
			return true;
//		if (getModMaterial() == ModMaterials.TUNGSTEN || getModMaterial() == ModMaterials.TITANIUM || getModMaterial() == ModMaterials.TUNGSTEN)
//			return true;
		return super.isTranslucent(state);
	}

	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.randomDisplayTick(stateIn, worldIn, pos, rand);
//		worldIn.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 20, true);
		if (getModMaterial() == ModMaterials.TUNGSTEN || getModMaterial() == ModMaterials.TITANIUM)
			worldIn.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 20, true);
	}
}
