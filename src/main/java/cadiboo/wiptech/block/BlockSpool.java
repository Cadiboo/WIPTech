package cadiboo.wiptech.block;

import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * A block of wires stored for later use
 * @author Cadiboo
 */
public class BlockSpool extends Block implements IModBlock, IBlockModMaterial {

	protected final ModMaterial material;

	public BlockSpool(final ModMaterial material) {
		super(Material.WOOD);
		ModUtil.setRegistryNames(this, material, "spool");
		this.material = material;
	}

	@Override
	public final ModMaterial getModMaterial() {
		return this.material;
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
		return false;
	}

	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean canRenderInLayer(final IBlockState state, final BlockRenderLayer layer) {
		return this.material.getProperties().getBlockRenderLayers().contains(layer) || (this.getRenderLayer() == layer);
	}

	@Override
	public boolean isBeaconBase(final IBlockAccess worldObj, final BlockPos pos, final BlockPos beacon) {
		return true;
	}

}
