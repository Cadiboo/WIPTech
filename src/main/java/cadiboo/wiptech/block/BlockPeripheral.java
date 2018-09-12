package cadiboo.wiptech.block;

import cadiboo.wiptech.tileentity.TileEntityPeripheral;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Cadiboo
 */
public class BlockPeripheral extends Block {

	public BlockPeripheral(final String name) {
		super(Material.IRON);
		ModUtil.setRegistryNames(this, name);
	}

	@Override
	public boolean hasTileEntity(final IBlockState state) {
		return true;
	}

	@Override
	public TileEntityPeripheral createTileEntity(final World world, final IBlockState state) {
		return new TileEntityPeripheral();
	}

	@Override
	public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
		final TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityPeripheral) {
			final TileEntityPeripheral peripheral = (TileEntityPeripheral) tile;
			final BlockPos central = peripheral.getCentralPos();
//			world.getBlockState(central).getBlock().breakBlock(world, central, state);
			world.setBlockState(central, Blocks.AIR.getDefaultState());
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		if (world.isRemote) {
			return true;
		} else {
			final TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileEntityPeripheral) {
				final TileEntityPeripheral peripheral = (TileEntityPeripheral) tile;

				final TileEntity central = peripheral.getCentral();

				if (central == null) {
					return false;
				}

				central.getBlockType().onBlockActivated(world, central.getPos(), state, player, hand, facing, hitX, hitY, hitZ);

			}
			return true;
		}
	}

	@Override
	public void onBlockExploded(final World world, final BlockPos pos, final Explosion explosion) {
		final TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityPeripheral) {
			final TileEntityPeripheral peripheral = (TileEntityPeripheral) tile;
			final BlockPos central = peripheral.getCentralPos();
//			world.getBlockState(central).getBlock().onBlockExploded(world, central, explosion);
			world.setBlockState(central, Blocks.AIR.getDefaultState());
		}
		super.onBlockExploded(world, pos, explosion);
	}

	@Override
	public EnumBlockRenderType getRenderType(final IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
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
	public AxisAlignedBB getSelectedBoundingBox(final IBlockState state, final World world, final BlockPos pos) {
		final TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityPeripheral) {
			final TileEntityPeripheral peripheral = (TileEntityPeripheral) tile;
			final BlockPos central = peripheral.getCentralPos();
			final Block block = world.getBlockState(central).getBlock();
			if (block instanceof IBlockCentral) {
				AxisAlignedBB selectedBB = block.getSelectedBoundingBox(state, world, pos);
				for (final BlockPos peripheralPos : ((IBlockCentral) block).getPeripheralPositions(central)) {
					selectedBB = selectedBB.union(new AxisAlignedBB(peripheralPos));
				}
				return selectedBB;
			}
		}
		return super.getSelectedBoundingBox(state, world, pos);
	}

	@Override
	public ItemStack getPickBlock(final IBlockState state, final RayTraceResult target, final World world, final BlockPos pos, final EntityPlayer player) {
		final TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityPeripheral) {
			final TileEntityPeripheral peripheral = (TileEntityPeripheral) tile;
			final BlockPos central = peripheral.getCentralPos();
			final Block block = world.getBlockState(central).getBlock();
			if (block instanceof IBlockCentral) {
				return block.getPickBlock(state, target, world, central, player);
			}
		}
		return super.getPickBlock(state, target, world, pos, player);
	}

}
