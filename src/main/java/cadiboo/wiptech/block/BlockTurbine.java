package cadiboo.wiptech.block;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.GuiHandler;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockTurbine extends BlockTileEntity<TileEntityTurbine> {

	public BlockTurbine(String name, Material material) {
		super(name, material);
		this.setDefaultState(this.blockState.getBaseState());
		this.setTileEntity();
		this.setTransparentBlock();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStackIn) {
		for (int i = 1; i <= 5; i++) {
			worldIn.setBlockState(pos.up(i), Blocks.PERIPHERAL.getDefaultState());
		}
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityTurbine) {
			if (itemStackIn.hasTagCompound()) {
				int energy = itemStackIn.getTagCompound().getInteger("Energy");
				((CustomEnergyStorage) ((TileEntityTurbine) te).getCapability(CapabilityEnergy.ENERGY, null)).setEnergyStored(energy);
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for (int i = 1; i <= 5; i++) {
			if (!worldIn.getBlockState(pos.up(i)).getBlock().isReplaceable(worldIn, pos.up(i)))
				return false;
		}
		return true;
	}

	@Override
	public TileEntityTurbine createTileEntity(World world, IBlockState state) {
		return new TileEntityTurbine();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityTurbine tile = (TileEntityTurbine) worldIn.getTileEntity(pos);
		if (tile != null) {
			IItemHandler inven = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for (int i = 0; i < inven.getSlots(); i++) {
				ItemStack stack = inven.getStackInSlot(i);
				if (!stack.isEmpty()) {
					EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
					worldIn.spawnEntity(item);
				}
			}
			worldIn.removeTileEntity(pos);
			super.breakBlock(worldIn, pos, state);
			worldIn.setBlockToAir(pos);
			for (int i = 1; i <= 5; i++) {
				if (BlockPeripheralBlock.isPeripheral(worldIn, pos.up(i)))
					worldIn.setBlockToAir(pos.up(i));
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityTurbine te = (TileEntityTurbine) worldIn.getTileEntity(pos);
		IItemHandler inven = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if (inven != null) {
			player.openGui(WIPTech.instance, GuiHandler.TURBINE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
