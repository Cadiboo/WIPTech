package cadiboo.wiptech.block.capacitorbank;

import java.util.List;

import cadiboo.wiptech.block.BlockTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockCapacitorBank extends BlockTileEntity<TileEntityCapacitorBank>{

	public BlockCapacitorBank (String name, Material material) {
		super(name, material);
		this.setDefaultState(this.blockState.getBaseState());
		this.setTileEntity();
		this.setNonSolidBlock();
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("\u00A76\u00A7o"+"I'm trying ok!");
	}

	@Override
	public TileEntityCapacitorBank createTileEntity(World world, IBlockState state) {
		return new TileEntityCapacitorBank();
	}
}
