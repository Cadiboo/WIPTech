package cadiboo.wiptech.block;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.GuiHandler;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.CustomEnergyStorage;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockAssemblyTable extends BlockTileEntity<TileEntityAssemblyTable> {

	public BlockAssemblyTable(String name, Material material) {
		super(name, material);
		this.setTileEntity();
		this.setTransparentBlock();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStackIn) {
		for (int x = -1; x < 2; x++)
			for (int z = -1; z < 2; z++)
				for (int y = 0; y < 2; y++)
					if (!pos.up(y).north(z).west(x).equals(pos))
						worldIn.setBlockState(pos.up(y).north(z).west(x), Blocks.PERIPHERAL.getDefaultState());

		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityAssemblyTable) {
			if (itemStackIn.hasTagCompound()) {
				int energy = itemStackIn.getTagCompound().getInteger("Energy");
				((CustomEnergyStorage) ((TileEntityAssemblyTable) te).getCapability(CapabilityEnergy.ENERGY, null)).setEnergyStored(energy);
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for (int x = -1; x < 2; x++)
			for (int z = -1; z < 2; z++)
				for (int y = 0; y < 2; y++)
					if (!worldIn.getBlockState(pos.up(y).north(z).west(x)).getBlock().isReplaceable(worldIn, pos.up(y).north(z).west(x)))
						return false;
		return true;
	}

	@Override
	public TileEntityAssemblyTable createTileEntity(World world, IBlockState state) {
		return new TileEntityAssemblyTable();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityAssemblyTable tile = (TileEntityAssemblyTable) worldIn.getTileEntity(pos);
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
			for (int x = -1; x < 2; x++)
				for (int z = -1; z < 2; z++)
					for (int y = 0; y < 2; y++)
						if (BlockPeripheralBlock.isPeripheral(worldIn, pos.up(y).north(z).west(x)))
							worldIn.setBlockToAir(pos.up(y).north(z).west(x));
			super.breakBlock(worldIn, pos, state);
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityAssemblyTable te = (TileEntityAssemblyTable) worldIn.getTileEntity(pos);
		player.openGui(WIPTech.instance, GuiHandler.ASSEMBLY_TABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

}
