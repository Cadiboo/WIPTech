package cadiboo.wiptech.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.GuiHandler;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import cadiboo.wiptech.util.Utils;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockCrusher extends BlockTileEntity<TileEntityCrusher> {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockCrusher(String name, Material material) {
		super(name, material);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setTileEntity();
		this.setTransparentBlock();
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Crushes & Hammers");
		tooltip.add("UPDATE - now I have to break it so that it needs electricity D:");
	}

	@SideOnly(Side.CLIENT)
	public void animateCrush(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (getTileEntity(worldIn, pos).isCrushing()) {
			EnumFacing enumfacing = stateIn.getValue(FACING);

			TileEntityCrusher tile = getTileEntity(worldIn, pos);
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			ItemStack stack = itemHandler.getStackInSlot(1);
			if (!stack.isEmpty()) {
				double x = pos.getX() + 0.5D;
				double y = pos.getY() + 0.2D;
				double z = pos.getZ() + 0.5D;
				switch (enumfacing) {
				case NORTH:
					z += 0.2D;
					break;
				case EAST:
					x -= 0.2D;
					break;
				case SOUTH:
					z -= 0.2D;
					break;
				case WEST:
					x += 0.2D;
					break;
				default:
					WIPTech.logger.info("Crusher animateCrush Error!!");
					break;
				}
				int spawnParticleCount = 4;
				for (int i = 0; i < spawnParticleCount; i++) {
					worldIn.spawnParticle(EnumParticleTypes.ITEM_CRACK, x, y, z, Utils.randomBetween(-35, 35) / 1000.0D,
							0.15D, Utils.randomBetween(-35, 35) / 1000.0D,
							new int[] { Item.getIdFromItem(stack.getItem()), stack.getItem().getMetadata(stack) });
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (getTileEntity(worldIn, pos).isCrushing()) {
			EnumFacing enumfacing = stateIn.getValue(FACING);
			double x = pos.getX() + 0.5D;
			double y = pos.getY() + 0.5D;
			double z = pos.getZ() + 0.5D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			if (rand.nextDouble() < 0.1D) {
				worldIn.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
						SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + d4, y + 0.4D + d4 / 10.0D, y + d4, 0.0D, 0.0D,
					0.0D, new int[0]);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityCrusher tile = getTileEntity(world, pos);
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			WIPTech.logger.info(itemHandler);
			WIPTech.logger.info("player.isSneaking(): " + player.isSneaking());
			if (!player.isSneaking()) {
				player.openGui(WIPTech.instance, GuiHandler.CRUSHER, world, pos.getX(), pos.getY(), pos.getZ());
			} else {
				ItemStack stack = itemHandler.getStackInSlot(0);
				player.sendMessage(new TextComponentString((!stack.isEmpty()
						? stack.getCount() + "x "
								+ WIPTech.proxy.localize(new StringBuilder(String.valueOf(stack.getUnlocalizedName()))
										.append(".name").toString(), new Object[0])
						: "Empty") + " " + tile.getCrushTime()));
			}
		}
		return true;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		setDefaultFacing(worldIn, pos, state);
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			IBlockState iblockstate = worldIn.getBlockState(pos.north());
			IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
			IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
			IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = state.getValue(FACING);
			if ((enumfacing == EnumFacing.NORTH) && (iblockstate.isFullBlock()) && (!iblockstate1.isFullBlock())) {
				enumfacing = EnumFacing.SOUTH;
			} else if ((enumfacing == EnumFacing.SOUTH) && (iblockstate1.isFullBlock())
					&& (!iblockstate.isFullBlock())) {
				enumfacing = EnumFacing.NORTH;
			} else if ((enumfacing == EnumFacing.WEST) && (iblockstate2.isFullBlock())
					&& (!iblockstate3.isFullBlock())) {
				enumfacing = EnumFacing.EAST;
			} else if ((enumfacing == EnumFacing.EAST) && (iblockstate3.isFullBlock())
					&& (!iblockstate2.isFullBlock())) {
				enumfacing = EnumFacing.WEST;
			}
			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);
		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}
		return getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityCrusher tile = getTileEntity(world, pos);
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if (!stack.isEmpty()) {
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.spawnEntity(item);
			}
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	@Nullable
	public TileEntityCrusher createTileEntity(World world, IBlockState state) {
		return new TileEntityCrusher();
	}
}