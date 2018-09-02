package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockModOre extends Block implements IBlockModMaterial {

	protected final ModMaterials material;

	public BlockModOre(final ModMaterials material) {
		super(Material.ROCK);
		ModUtil.setRegistryNames(this, material, "ore");
		this.material = material;
	}

	@Override
	public final ModMaterials getModMaterial() {
		return this.material;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return ModUtil.getMaterialLightValue(this.getModMaterial());
	}

	@Override
	public int getLightOpacity(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return ModUtil.getMaterialLightOpacity(this.getModMaterial());
	}

	@Override
	public boolean isFullCube(final IBlockState state) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		if (ModReference.Debug.debugOres()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
		super.getDrops(drops, world, pos, state, fortune);
	}

}
