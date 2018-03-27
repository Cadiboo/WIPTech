package cadiboo.wiptech.block;

import java.util.List;

import cadiboo.wiptech.tileentity.TileEntityTurbine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockTurbine extends BlockTileEntity<TileEntityTurbine>{

	public BlockTurbine (String name, Material material) {
		super(name, material);
		this.setDefaultState(this.blockState.getBaseState());
		this.setTileEntity();
		this.setNonSolidBlock();

	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("\u00A76\u00A7o"+"Its windy");
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStackIn) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityTurbine) {
			if (itemStackIn.hasTagCompound()) {
				int energy = itemStackIn.getTagCompound().getInteger("Energy");
				((TileEntityTurbine) te).energy.forceReceive(energy, false);
			}
		}
	}

	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		/*TileEntityTurbine tile = (TileEntityTurbine)getTileEntity(world, pos);
		IItemHandler itemHandler = (IItemHandler)tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		for (int i = 0; i < itemHandler.getSlots(); i++)
		{
			ItemStack stack = itemHandler.getStackInSlot(i);
			if (!stack.isEmpty())
			{
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.spawnEntity(item);
			}
		}
		*/
		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntityTurbine createTileEntity(World world, IBlockState state) {
		return new TileEntityTurbine();
	}
}
