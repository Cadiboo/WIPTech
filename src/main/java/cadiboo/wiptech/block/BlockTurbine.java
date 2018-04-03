package cadiboo.wiptech.block;

import java.util.List;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.GuiHandler;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockTurbine extends BlockTileEntity<TileEntityTurbine>{

	public BlockTurbine (String name, Material material) {
		super(name, material);
		this.setDefaultState(this.blockState.getBaseState());
		this.setTileEntity();
		this.setNonSolidBlock();
		this.setTransparentBlock();
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("\u00A76\u00A7o"+"Its windy");
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStackIn) {
		for(int i=1; i<=5; i++) {
			worldIn.setBlockState(pos.up(i), Blocks.PERIPHERAL.getDefaultState());
		}
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityTurbine) {
			if (itemStackIn.hasTagCompound()) {
				int energy = itemStackIn.getTagCompound().getInteger("Energy");
				((TileEntityTurbine) te).energy.setEnergyStored(energy);
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for(int i=1; i<=5; i++) {
			if(!worldIn.getBlockState(pos.up(i)).getBlock().isReplaceable(worldIn, pos.up(i)))
				return false;
		}
		return true;
	}

	@Override
	public TileEntityTurbine createTileEntity(World world, IBlockState state) {
		return new TileEntityTurbine();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityTurbine tile = (TileEntityTurbine)worldIn.getTileEntity(pos);
		if(tile!=null) {
			WIPTech.logger.info("not null");
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
			for (int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.getStackInSlot(i);
				if (!stack.isEmpty())
				{
					EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
					worldIn.spawnEntity(item);
				}
			}
			worldIn.removeTileEntity(pos);
			super.breakBlock(worldIn, pos, state);
			worldIn.setBlockToAir(pos);
			
			for(int i=1; i<=5; i++) {
				if(BlockPeripheralBlock.isPeripheral(worldIn, pos.up(i)))
					worldIn.setBlockToAir(pos.up(i));
			}
		}
	}

	/*@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		// TODO Auto-generated method stub
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}
	 */


	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		WIPTech.logger.info(getTileEntity(worldIn, pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));

		if (!worldIn.isRemote)
		{
			TileEntityTurbine tile = (TileEntityTurbine)getTileEntity(worldIn, pos);
			IItemHandler itemHandler = (IItemHandler)tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
			CustomEnergyStorage energy = (CustomEnergyStorage)tile.getCapability(CapabilityEnergy.ENERGY, side);
			WIPTech.logger.info("player.isSneaking(): " + player.isSneaking());
			if (!player.isSneaking())
			{
				player.openGui(WIPTech.instance, GuiHandler.TURBINE, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
			else
			{
				player.sendMessage(new TextComponentString("Energy Stored: "+energy.getEnergyStored()));
			}
		}
		return true;
	}
}
