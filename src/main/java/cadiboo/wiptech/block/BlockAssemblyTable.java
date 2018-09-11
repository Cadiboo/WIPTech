package cadiboo.wiptech.block;

import java.util.HashSet;

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
public class BlockAssemblyTable extends Block {

	public static final int	WIDTH	= 3;
	public static final int	HEIGHT	= 2;
	public static final int	DEPTH	= 3;

	public BlockAssemblyTable(final String name) {
		super(Material.IRON);
		ModUtil.setRegistryNames(this, name);
	}

	@Override
	public boolean canPlaceBlockAt(final World world, final BlockPos pos) {
		for (final BlockPos peripheralPos : getPeripheralPositions(pos)) {
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
			world.setBlockState(peripheralPos, Blocks.AIR.getDefaultState());
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

	public static HashSet<BlockPos> getPeripheralPositions(final BlockPos pos) {
		final int smallX = (int) -(WIDTH / 2f);
		final int smallY = 0;
		final int smallZ = (int) -(DEPTH / 2f);

		final int bigX = (int) (WIDTH / 2f);
		final int bigY = HEIGHT - 1;
		final int bigZ = (int) (DEPTH / 2f);

		final HashSet<BlockPos> peripheralPositions = new HashSet<>();

		final BlockPos from = new BlockPos(smallX, smallY, smallZ).add(pos);

		final BlockPos to = new BlockPos(bigX, bigY, bigZ).add(pos);

		BlockPos.getAllInBox(from, to).forEach(peripheralPos -> {
			if (!peripheralPos.equals(pos)) {
				peripheralPositions.add(peripheralPos);
			}
		});

		return peripheralPositions;
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

}
