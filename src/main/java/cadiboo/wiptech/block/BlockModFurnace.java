package cadiboo.wiptech.block;

import java.util.Random;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.inventory.ModItemStackHandler;
import cadiboo.wiptech.tileentity.TileEntityModFurnace;
import cadiboo.wiptech.util.ExistsForDebugging;
import cadiboo.wiptech.util.ModGuiHandler;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockModFurnace extends BlockHorizontal {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool ON = PropertyBool.create("on");

	public BlockModFurnace(final String name) {
		super(Material.ROCK);
		ModUtil.setRegistryNames(this, name);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ON, false));
	}

	@Override
	public boolean hasTileEntity(final IBlockState state) {
		return true;
	}

	@Override
	public TileEntityModFurnace createTileEntity(final World world, final IBlockState state) {
		return new TileEntityModFurnace();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{FACING, ON});
	}

	@ExistsForDebugging
	@Override
	public int getMetaFromState(final IBlockState state) {
		if (state.getValue(ON)) {
			return state.getValue(FACING).getHorizontalIndex() + EnumFacing.HORIZONTALS.length;
		} else {
			return state.getValue(FACING).getHorizontalIndex();
		}
	}

	@ExistsForDebugging
	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % EnumFacing.HORIZONTALS.length)).withProperty(ON, meta >= EnumFacing.HORIZONTALS.length);
	}

	@Override
	public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
		final EnumFacing enumfacing = placer.getHorizontalFacing().getOpposite();
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing);
	}

	@Override
	public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
		super.randomDisplayTick(stateIn, worldIn, pos, rand);
		if (stateIn.getValue(ON)) {
			final EnumFacing enumfacing = stateIn.getValue(FACING);
			final double d0 = pos.getX() + 0.5D;
			final double d1 = pos.getY() + ((rand.nextDouble() * 6.0D) / 16.0D);
			final double d2 = pos.getZ() + 0.5D;
			final double d3 = 0.52D;
			final double d4 = (rand.nextDouble() * 0.6D) - 0.3D;

			if (rand.nextDouble() < 0.1D) {
				worldIn.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			switch (enumfacing) {
				case WEST :
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case EAST :
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				default :
				case NORTH :
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
					break;
				case SOUTH :
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			final TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityModFurnace) {
				playerIn.openGui(WIPTech.instance, ModGuiHandler.MOD_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
				playerIn.addStat(StatList.FURNACE_INTERACTION);
			}

			return true;
		}
	}

	@Override
	public boolean hasComparatorInputOverride(final IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
		final TileEntity tile = worldIn.getTileEntity(pos);
		if ((tile == null) || !(tile instanceof TileEntityModFurnace)) {
			return 0;
		}

		final ModItemStackHandler inventory = ((TileEntityModFurnace) tile).getInventory();

		int max = 0;

		for (int i = 0; i < inventory.getSlots(); i++) {
			max += inventory.getSlotLimit(i);
		}

		int size = 0;
		for (int i = 0; i < inventory.getSlots(); i++) {
			size += inventory.getStackInSlot(i).getCount();
		}

		return Math.round(Math.round(ModUtil.map(0, max, 0, 15, size)));
	}

}
