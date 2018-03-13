package cadiboo.wiptech.block.crusher;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import cadiboo.wiptech.GuiHandler;
import cadiboo.wiptech.ModGuiHandler;
import cadiboo.wiptech.Utils;
import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.WIPTechMod;
import cadiboo.wiptech.block.BlockTileEntity;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
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
	//private final boolean isCrushing;
	private static boolean keepInventory;
	//private boolean isCrushing;

	public BlockCrusher(String name, Material material) {
		super(name, material);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setTileEntity();
		this.setNonSolidBlock();
		//Utils.getLogger().info("new ItemStack(this).getItem().getIdFromItem(new ItemStack(this).getItem()) - " + new ItemStack(this).getItem().getIdFromItem(new ItemStack(this).getItem()));
		//Utils.getLogger().info("Item.getIdFromItem(new ItemStack(this).getItem()) - " + Item.getIdFromItem(new ItemStack(this).getItem()));
		//this.isCrushing = isCrushing;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("\u00A76\u00A7o"+"Why is this so hard to make.");
		tooltip.add("Im gonna skip this and come back to it later.");
		tooltip.add("You can get the stuff some other way for now.");
	}

	@SideOnly(Side.CLIENT)
	public void animateCrush(IBlockState stateIn, World worldIn, BlockPos pos) {
		Random rand = new Random();
		animateCrush(stateIn, worldIn, pos, rand);
	}

	@SideOnly(Side.CLIENT)
	private void animateCrush(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (TileEntityCrusher.isCrushing(this.getTileEntity(worldIn, pos)))
		{
			EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);

			TileEntityCrusher tile = getTileEntity(worldIn, pos);
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
			ItemStack stack = itemHandler.getStackInSlot(1);
			if (!stack.isEmpty()) {

				double x = (double)pos.getX() + 0.5D;
				double y = (double)pos.getY() + 0.2D;
				double z = (double)pos.getZ() + 0.5D;
				
				switch(enumfacing)
				{
				case NORTH: z+=0.2; break;
				case EAST: x-=0.2; break;
				case SOUTH: z-=0.2; break;
				case WEST: x+=0.2; break;
				default: break;
				}
				
				int spawnParticleCount = 4; //should be around 4
				for(int i = 0; i<spawnParticleCount; i++) {
					worldIn.spawnParticle(EnumParticleTypes.ITEM_CRACK, x, y, z, randomBetween(-35, 35)/1000, 0.15, randomBetween(-35, 35)/1000, Item.getIdFromItem(stack.getItem()), stack.getItem().getMetadata(stack));
				}

				/*double x = (double)pos.getX() + 0.5D;
				double y = (double)pos.getY() + 0.5D;
				double z = (double)pos.getZ() + 0.5D;

				//double vecYMin = 0.23;
				//double vecYMax = vecYMin;

				double vecYMin = -0.23;
				double vecYMax = vecYMin;

				double vecXMax =  0.35;
				double vecXMin = -0.35;
				double vecZMax =  0.35;
				double vecZMin = -0.35;

				//int spawnParticleCount = 4; //should be around 4
				double spawnAt = y-0.1;

				//double multiplier = 4;
				double multiplier = 2;

				switch (enumfacing)
				{
				case NORTH:
					vecZMax=0;
					vecZMin=vecZMin*multiplier;
					break;
				case EAST:
					vecXMax=vecXMax*multiplier;
					vecXMin=0;
					break;
				case SOUTH:
					vecZMax=vecZMax*multiplier;
					vecZMin=0;
					break;
				case WEST:
					vecXMax=0;
					vecXMin=vecXMin*multiplier;
					break;
				default: //shoot up
					vecYMin = 0.275;
					vecYMax = 0.35;
					break;
				}
				 */

				/*for(int i = 0; i<spawnParticleCount; i++) {
					double vecY = Math.abs(randomBetween((int) (vecYMin*100), (int) (vecYMax*100)))/100;
					double vecX = randomBetween((int) (vecXMin*100), (int) (vecXMax*100))/1000;
					double vecZ = randomBetween((int) (vecZMin*100), (int) (vecZMax*100))/1000;
					/*
					Utils.getLogger().info(enumfacing);
					Utils.getLogger().info("vecXMin: "+vecXMin+", vecXMax: "+vecXMax+" | vecZMin: "+vecZMin+", vecZMax: "+vecZMax);
					Utils.getLogger().info("vecX: "+vecX+", vecZ: "+vecZ);
				 *\/
					worldIn.spawnParticle(EnumParticleTypes.ITEM_CRACK, x, spawnAt, z, vecX, vecY, vecZ, Item.getIdFromItem(stack.getItem()), stack.getItem().getMetadata(stack));
				}*/
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (TileEntityCrusher.isCrushing(this.getTileEntity(worldIn, pos)))
		{
			EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
			double x = (double)pos.getX() + 0.5D;
			double y = (double)pos.getY() + 0.5D;
			double z = (double)pos.getZ() + 0.5D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;

			if (rand.nextDouble() < 0.1D)
			{
				worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + d4, y+0.4+(d4/10), y + d4, 0.0D, 0.0D, 0.0D);

		}
	}

	private double randomBetween(int min, int max) {
		return new Random().nextInt((int) ((max - min)+1) )+min;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote) {
			TileEntityCrusher tile = getTileEntity(world, pos);
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
			if (!player.isSneaking()) {
				player.openGui(WIPTech.instance, GuiHandler.CRUSHER, world, pos.getX(), pos.getY(), pos.getZ());
			} else {
				ItemStack stack = itemHandler.getStackInSlot(0);
				player.sendMessage(new TextComponentString((!stack.isEmpty()?stack.getCount() + "x " + WIPTech.proxy.localize(stack.getUnlocalizedName() + ".name"):"Empty")+" "+tile.getCrushTime(tile)));
			}
		}
		return true;
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.setDefaultFacing(worldIn, pos, state);
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			IBlockState iblockstate = worldIn.getBlockState(pos.north());
			IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
			IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
			IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

			if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock())
			{
				enumfacing = EnumFacing.SOUTH;
			}
			else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock())
			{
				enumfacing = EnumFacing.NORTH;
			}
			else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock())
			{
				enumfacing = EnumFacing.EAST;
			}
			else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock())
			{
				enumfacing = EnumFacing.WEST;
			}

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}


	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

		if (stack.hasDisplayName())
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityFurnace)
			{
				((TileEntityFurnace)tileentity).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}

	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityCrusher tile = getTileEntity(world, pos);
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		for(int i = 0; i<8; i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if (!stack.isEmpty()) {
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.spawnEntity(item);
			}
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public Class<TileEntityCrusher> getTileEntityClass() {
		return TileEntityCrusher.class;
	}

	@Nullable
	@Override
	public TileEntityCrusher createTileEntity(World world, IBlockState state) {
		return new TileEntityCrusher();
	}

}