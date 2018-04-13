package cadiboo.wiptech.block;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockWire extends BlockBase {
	public static final PropertyEnum<ConductiveMetals> METAL = PropertyEnum.<ConductiveMetals>create("metal",
			ConductiveMetals.class);
	public static final PropertyBool ENAMEL = PropertyBool.create("enamel");

	public static final int METALS_LENGTH = ConductiveMetals.values().length;

	public BlockWire(String name, Material material) {
		super(name, material);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(METAL, ConductiveMetals.TIN).withProperty(ENAMEL, false));
		this.setCircuit();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < METALS_LENGTH * 2; i++) {
			WIPTech.logger.info(i);
			WIPTech.logger.info(items);
			items.add(new ItemStack(this, 1, i));
			WIPTech.logger.info(items.get(items.size() - 1));
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		WIPTech.logger.info("wire state: " + state);
		return state.getValue(METAL).getMetadata() + (state.getValue(ENAMEL) ? METALS_LENGTH : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		WIPTech.logger.info("wire meta: " + meta);
		IBlockState state = this.getDefaultState();
		state = state.withProperty(ENAMEL, meta > METALS_LENGTH - 1);
		int metaData = meta;
		if (meta > METALS_LENGTH - 1)
			metaData -= METALS_LENGTH;
		state = state.withProperty(METAL, ConductiveMetals.byMetadata(metaData));
		return state;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { METAL, ENAMEL });
	}
}