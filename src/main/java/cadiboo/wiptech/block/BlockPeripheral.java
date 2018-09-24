package cadiboo.wiptech.block;

import java.util.HashSet;

import javax.annotation.Nullable;

import cadiboo.wiptech.tileentity.TileEntityPeripheral;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
public class BlockPeripheral extends Block implements IModBlock {

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
		final Block block = this.getCentralBlock(world, pos);
		if (block != null) {
			block.breakBlock(world, pos, state);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		if (world.isRemote) {
			return true;
		}

		final Block block = this.getCentralBlock(world, pos);
		if (block == null) {
			return false;
		}

		block.onBlockActivated(world, this.getCentralPos(world, pos), state, player, hand, facing, hitX, hitY, hitZ);
		return true;
	}

	@Override
	public void onBlockExploded(final World world, final BlockPos pos, final Explosion explosion) {
		final BlockPos centralPos = this.getCentralPos(world, pos);
		if (centralPos != null) {
			world.setBlockToAir(centralPos);
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
		final Block block = this.getCentralBlock(world, pos);
		if (block == null) {
			return super.getSelectedBoundingBox(state, world, pos);
		}

		AxisAlignedBB selectedBB = block.getSelectedBoundingBox(state, world, pos);
		final HashSet<BlockPos> peripheralPositions = ((IBlockCentral) block).getPeripheralPositions(this.getCentralPos(world, pos));
		for (final BlockPos peripheralPos : peripheralPositions) {
			selectedBB = selectedBB.union(new AxisAlignedBB(peripheralPos));
		}
		return selectedBB;
	}

	@Override
	public ItemStack getPickBlock(final IBlockState state, final RayTraceResult target, final World world, final BlockPos pos, final EntityPlayer player) {
		final Block block = this.getCentralBlock(world, pos);
		if (block == null) {
			return super.getPickBlock(state, target, world, pos, player);
		}

		return block.getPickBlock(state, target, world, pos, player);
	}

	@Override
	public void onBlockHarvested(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
		final Block block = this.getCentralBlock(world, pos);
		if (block == null) {
			super.onBlockHarvested(world, pos, state, player);
			return;
		}

		block.onBlockHarvested(world, pos, state, player);
		return;
	}

	@Override
	public void harvestBlock(final World world, final EntityPlayer player, final BlockPos pos, final IBlockState state, final TileEntity te, final ItemStack stack) {
		final Block block = this.getCentralBlock(world, pos);
		if (block == null) {
			super.harvestBlock(world, player, pos, state, te, stack);
			return;
		}

		block.harvestBlock(world, player, pos, state, te, stack);
		return;
	}

	@Nullable
	public final <B extends Block & IBlockCentral> B getCentralBlock(final World world, final BlockPos pos) {
		final BlockPos centralPos = this.getCentralPos(world, pos);
		if (centralPos == null) {
			return null;
		}
		return (B) world.getBlockState(centralPos).getBlock();
	}

	@Nullable
	public final BlockPos getCentralPos(final World world, final BlockPos pos) {
		final TileEntity tile = world.getTileEntity(pos);
		if ((tile != null) && (tile instanceof TileEntityPeripheral)) {
			final TileEntityPeripheral peripheral = (TileEntityPeripheral) tile;
			final BlockPos centralPos = peripheral.getCentralPos();
			final IBlockState state = world.getBlockState(centralPos);
			final Block block = state.getBlock();
			if (block instanceof IBlockCentral) {
				return centralPos;
			}
		}
		return null;
	}

}
