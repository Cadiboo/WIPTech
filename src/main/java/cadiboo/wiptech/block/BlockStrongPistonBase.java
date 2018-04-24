package cadiboo.wiptech.block;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import cadiboo.wiptech.block.state.BlockStrongPistonStructureHelper;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStrongPistonBase extends BlockPistonBase {

	private boolean isSticky;

	public BlockStrongPistonBase(String name, boolean isSticky) {
		super(isSticky);
		this.isSticky = isSticky;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTENDED, Boolean.valueOf(false)));
		this.setRegistryName(new ResourceLocation(Reference.ID, name));
		this.setUnlocalizedName(name);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		tooltip.add("\u00A76\u00A7o" + "Should be able to push obsidian but for some reason it just can\'t.");
		tooltip.add("After about 4 hours non stop trying to do the eqivalent of changing ONE LINE of minecraft source code I give up.");
		tooltip.add("Update - After more attepts this block doesn\'t even do anything at all anymore.");
	}

	public static boolean canPush(IBlockState state, World worldIn, BlockPos pos, EnumFacing facing, boolean destroyBlocks, EnumFacing face) {
		Block block = state.getBlock();

		if (block == net.minecraft.init.Blocks.OBSIDIAN)
			return true;
		else
			return BlockPistonBase.canPush(state, worldIn, pos, facing, destroyBlocks, face);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!worldIn.isRemote) {
			this.checkForMove(worldIn, pos, state);
		}
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity
	 * is set
	 */
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to
	 * allow for adjustments to the IBlockstate
	 */
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer) {
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}

	private void checkForMove(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing enumfacing = state.getValue(FACING);
		boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);

		if (flag && !state.getValue(EXTENDED).booleanValue()) {
			if ((new BlockStrongPistonStructureHelper(worldIn, pos, enumfacing, true)).canMove()) {
				worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex());
			}
		} else if (!flag && state.getValue(EXTENDED).booleanValue()) {
			worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
		}
	}

	private boolean shouldBeExtended(World worldIn, BlockPos pos, EnumFacing facing) {
		for (EnumFacing enumfacing : EnumFacing.values()) {
			if (enumfacing != facing && worldIn.isSidePowered(pos.offset(enumfacing), enumfacing)) {
				return true;
			}
		}

		if (worldIn.isSidePowered(pos, EnumFacing.DOWN)) {
			return true;
		} else {
			BlockPos blockpos = pos.up();

			for (EnumFacing enumfacing1 : EnumFacing.values()) {
				if (enumfacing1 != EnumFacing.DOWN && worldIn.isSidePowered(blockpos.offset(enumfacing1), enumfacing1)) {
					return true;
				}
			}

			return false;
		}
	}

	/**
	 * Called on server when World#addBlockEvent is called. If server returns true,
	 * then also called on the client. On the Server, this may perform additional
	 * changes to the world, like pistons replacing the block with an extended base.
	 * On the client, the update may involve replacing tile entities or effects such
	 * as sounds or particles
	 */
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		EnumFacing enumfacing = state.getValue(FACING);

		if (!worldIn.isRemote) {
			boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);

			if (flag && id == 1) {
				worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 2);
				return false;
			}

			if (!flag && id == 0) {
				return false;
			}
		}

		if (id == 0) {
			if (!this.doMove(worldIn, pos, enumfacing, true)) {
				return false;
			}

			worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 3);
			worldIn.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F,
					worldIn.rand.nextFloat() * 0.25F + 0.6F);
		} else if (id == 1) {
			TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));

			if (tileentity1 instanceof TileEntityPiston) {
				((TileEntityPiston) tileentity1).clearPistonTileEntity();
			}

			worldIn.setBlockState(pos,
					net.minecraft.init.Blocks.PISTON_EXTENSION.getDefaultState().withProperty(BlockPistonMoving.FACING, enumfacing).withProperty(
							BlockPistonMoving.TYPE,
							this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT),
					3);
			worldIn.setTileEntity(pos, BlockPistonMoving.createTilePiston(this.getStateFromMeta(param), enumfacing, false, true));

			if (this.isSticky) {
				BlockPos blockpos = pos.add(enumfacing.getFrontOffsetX() * 2, enumfacing.getFrontOffsetY() * 2, enumfacing.getFrontOffsetZ() * 2);
				IBlockState iblockstate = worldIn.getBlockState(blockpos);
				Block block = iblockstate.getBlock();
				boolean flag1 = false;

				if (block == net.minecraft.init.Blocks.PISTON_EXTENSION) {
					TileEntity tileentity = worldIn.getTileEntity(blockpos);

					if (tileentity instanceof TileEntityPiston) {
						TileEntityPiston tileentitypiston = (TileEntityPiston) tileentity;

						if (tileentitypiston.getFacing() == enumfacing && tileentitypiston.isExtending()) {
							tileentitypiston.clearPistonTileEntity();
							flag1 = true;
						}
					}
				}

				if (!flag1 && !iblockstate.getBlock().isAir(iblockstate, worldIn, blockpos)
						&& canPush(iblockstate, worldIn, blockpos, enumfacing.getOpposite(), false, enumfacing)
						&& (iblockstate.getMobilityFlag() == EnumPushReaction.NORMAL || block == Blocks.STRONG_PISTON
								|| block == Blocks.STRONG_PISTON)) {
					this.doMove(worldIn, pos, enumfacing, false);
				}
			} else {
				worldIn.setBlockToAir(pos.offset(enumfacing));
			}

			worldIn.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F,
					worldIn.rand.nextFloat() * 0.15F + 0.6F);
		}

		return true;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return super.isFullCube(state);
	}

	@Nullable
	public static EnumFacing getFacing(int meta) {
		return BlockPistonBase.getFacing(meta);
	}

	private boolean doMove(World worldIn, BlockPos pos, EnumFacing direction, boolean extending) {
		if (!extending) {
			worldIn.setBlockToAir(pos.offset(direction));
		}

		BlockStrongPistonStructureHelper blockpistonstructurehelper = new BlockStrongPistonStructureHelper(worldIn, pos, direction, extending);

		if (!blockpistonstructurehelper.canMove()) {
			return false;
		} else {
			List<BlockPos> list = blockpistonstructurehelper.getBlocksToMove();
			List<IBlockState> list1 = Lists.<IBlockState>newArrayList();

			for (int i = 0; i < list.size(); ++i) {
				BlockPos blockpos = list.get(i);
				list1.add(worldIn.getBlockState(blockpos).getActualState(worldIn, blockpos));
			}

			List<BlockPos> list2 = blockpistonstructurehelper.getBlocksToDestroy();
			int k = list.size() + list2.size();
			IBlockState[] aiblockstate = new IBlockState[k];
			EnumFacing enumfacing = extending ? direction : direction.getOpposite();

			for (int j = list2.size() - 1; j >= 0; --j) {
				BlockPos blockpos1 = list2.get(j);
				IBlockState iblockstate = worldIn.getBlockState(blockpos1);
				// Forge: With our change to how snowballs are dropped this needs to disallow to
				// mimic vanilla behavior.
				float chance = iblockstate.getBlock() instanceof BlockSnow ? -1.0f : 1.0f;
				iblockstate.getBlock().dropBlockAsItemWithChance(worldIn, blockpos1, iblockstate, chance, 0);
				worldIn.setBlockState(blockpos1, net.minecraft.init.Blocks.AIR.getDefaultState(), 4);
				--k;
				aiblockstate[k] = iblockstate;
			}

			for (int l = list.size() - 1; l >= 0; --l) {
				BlockPos blockpos3 = list.get(l);
				IBlockState iblockstate2 = worldIn.getBlockState(blockpos3);
				worldIn.setBlockState(blockpos3, net.minecraft.init.Blocks.AIR.getDefaultState(), 2);
				blockpos3 = blockpos3.offset(enumfacing);
				worldIn.setBlockState(blockpos3, net.minecraft.init.Blocks.PISTON_EXTENSION.getDefaultState().withProperty(FACING, direction), 4);
				worldIn.setTileEntity(blockpos3, BlockPistonMoving.createTilePiston(list1.get(l), direction, extending, false));
				--k;
				aiblockstate[k] = iblockstate2;
			}

			BlockPos blockpos2 = pos.offset(direction);

			if (extending) {
				BlockStrongPistonExtension.EnumPistonType type = this.isSticky ? BlockStrongPistonExtension.EnumPistonType.STICKY
						: BlockStrongPistonExtension.EnumPistonType.NORMAL;
				IBlockState iblockstate3 = Blocks.STRONG_PISTON_HEAD.getDefaultState().withProperty(BlockStrongPistonExtension.FACING, direction)
						.withProperty(BlockStrongPistonExtension.TYPE, type);
				IBlockState iblockstate1 = net.minecraft.init.Blocks.PISTON_EXTENSION.getDefaultState()
						.withProperty(BlockPistonMoving.FACING, direction).withProperty(BlockPistonMoving.TYPE,
								this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
				worldIn.setBlockState(blockpos2, iblockstate1, 4);
				worldIn.setTileEntity(blockpos2, BlockPistonMoving.createTilePiston(iblockstate3, direction, true, true));
			}

			for (int i1 = list2.size() - 1; i1 >= 0; --i1) {
				worldIn.notifyNeighborsOfStateChange(list2.get(i1), aiblockstate[k++].getBlock(), false);
			}

			for (int j1 = list.size() - 1; j1 >= 0; --j1) {
				worldIn.notifyNeighborsOfStateChange(list.get(j1), aiblockstate[k++].getBlock(), false);
			}

			if (extending) {
				worldIn.notifyNeighborsOfStateChange(blockpos2, net.minecraft.init.Blocks.PISTON_HEAD, false);
			}

			return true;
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return super.getMetaFromState(state);
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return super.withRotation(state, rot);
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return super.withMirror(state, mirrorIn);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return super.createBlockState();
	}

	/*
	 * ======================================== FORGE START
	 * =====================================
	 */
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return super.rotateBlock(world, pos, axis);
	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is
	 * used to decide whether things like buttons are allowed to be placed on the
	 * face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED},
	 * which represents something that does not fit the other descriptions and will
	 * generally cause other things not to connect to the face.
	 * 
	 * @return an approximation of the form of the given face
	 */
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return super.getBlockFaceShape(worldIn, state, pos, face);
	}

}
