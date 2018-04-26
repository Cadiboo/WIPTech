package cadiboo.wiptech.block;

import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;
import cadiboo.wiptech.tileentity.TileEntityWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockWire extends BlockTileEntity<TileEntityWire> {

	private final ConductiveMetals	metal;
	private boolean					enamel;

	public BlockWire(String nameIn, Material materialIn, ConductiveMetals metalIn, boolean enamelIn) {
		super(nameIn, materialIn);
		this.metal = metalIn;
		this.enamel = enamelIn;
		this.setTransparentBlock();
		this.setTileEntity();
	}

	@Override
	public TileEntityWire createTileEntity(World world, IBlockState state) {
		return new TileEntityWire();
	}

}