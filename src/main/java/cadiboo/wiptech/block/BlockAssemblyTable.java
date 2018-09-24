package cadiboo.wiptech.block;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.ModGuiHandler;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Cadiboo
 */
public class BlockAssemblyTable extends Block implements IModBlock, IBlockCentral {

	public BlockAssemblyTable(final String name) {
		super(Material.IRON);
		ModUtil.setRegistryNames(this, name);
	}

	@Override
	public boolean canPlaceBlockAt(final World world, final BlockPos pos) {
		for (final BlockPos peripheralPos : this.getPeripheralPositions(pos)) {
			if (!world.getBlockState(peripheralPos).getBlock().isReplaceable(world, peripheralPos)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean hasTileEntity(final IBlockState state) {
		return true;
	}

	@Override
	public TileEntityAssemblyTable createTileEntity(final World world, final IBlockState state) {
		return new TileEntityAssemblyTable();
	}

	@Override
	public void onBlockAdded(final World world, final BlockPos pos, final IBlockState state) {
		super.onBlockAdded(world, pos, state);

		this.getPeripheralPositions(pos).forEach(peripheralPos -> {
			world.setBlockState(peripheralPos, ModBlocks.PERIPHERAL.getDefaultState());
		});
	}

	@Override
	public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
		this.getPeripheralPositions(pos).forEach(peripheralPos -> {
			world.setBlockToAir(peripheralPos);
		});

		final TileEntity tile = world.getTileEntity(pos);
		if ((tile != null) && (tile instanceof TileEntityAssemblyTable)) {
			final TileEntityAssemblyTable assemblyTable = (TileEntityAssemblyTable) tile;
			assemblyTable.getInventory().dropItems(world, pos.getX(), pos.getY(), pos.getZ());
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public void onBlockExploded(final World world, final BlockPos pos, final Explosion explosion) {
		this.getPeripheralPositions(pos).forEach(peripheralPos -> {
			world.setBlockState(peripheralPos, Blocks.AIR.getDefaultState());
		});
		super.onBlockExploded(world, pos, explosion);
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			final TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityAssemblyTable) {
				playerIn.openGui(WIPTech.instance, ModGuiHandler.ASSEMBLY_TABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}

			return true;
		}
	}

	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	@Override
	public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return 1;
	}

	@Override
	public int getLightOpacity(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return 0;
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 2;
	}

	@Override
	public int getDepth() {
		return 3;
	}

}
