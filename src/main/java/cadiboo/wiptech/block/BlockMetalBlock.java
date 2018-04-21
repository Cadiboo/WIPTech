package cadiboo.wiptech.block;

import cadiboo.wiptech.handler.EnumHandler.Metals;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class BlockMetalBlock extends BlockBase {

	public static final PropertyEnum<Metals> METAL = PropertyEnum.<Metals>create("metal", Metals.class);

	public BlockMetalBlock(String name, Material materialIn) {
		super(name, materialIn);
		this.setBeaconBase();
		this.setDefaultState(this.blockState.getBaseState().withProperty(METAL, Metals.COPPER));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(METAL, Metals.byID(meta % (Metals.values().length - 1)));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(METAL).getID();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { METAL });
	}

}
