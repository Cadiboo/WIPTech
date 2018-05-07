package cadiboo.wiptech.block;

import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;
import cadiboo.wiptech.tileentity.TileEntityWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWire extends BlockTileEntity<TileEntityWire> {

	private final ConductiveMetals	metal;
	private final boolean			enamel;

	private static final AxisAlignedBB	CORE_BB		= new AxisAlignedBB(0.4375D, 0.4375D, 0.4375D, 0.5625, 0.5625, 0.5625);
	private static final AxisAlignedBB	UP_BB		= new AxisAlignedBB(0.4375D, 0.4375D, 0.4375D, 0.5625, 1, 0.5625);
	private static final AxisAlignedBB	DOWN_BB		= new AxisAlignedBB(0.4375D, 0, 0.4375D, 0.5625, 0.5625, 0.5625);
	private static final AxisAlignedBB	NORTH_BB	= new AxisAlignedBB(0.4375D, 0.4375D, 0, 0.5625, 0.5625, 0.5625);
	private static final AxisAlignedBB	SOUTH_BB	= new AxisAlignedBB(0.4375D, 0.4375D, 0.4375D, 0.5625, 0.5625, 1);
	private static final AxisAlignedBB	EAST_BB		= new AxisAlignedBB(0.4375D, 0.4375D, 0.4375D, 1, 0.5625, 0.5625);
	private static final AxisAlignedBB	WEST_BB		= new AxisAlignedBB(0, 0.4375D, 0.4375D, 0.5625, 0.5625, 0.5625);

	public ConductiveMetals getMetal() {
		return metal;
	}

	public boolean isEnamel() {
		return enamel;
	}

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

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		TileEntity tile = source.getTileEntity(pos);
		AxisAlignedBB BB = this.isEnamel() ? CORE_BB.grow(0.0625, 0.0625, 0.0625) : CORE_BB;
		if (tile != null && tile instanceof TileEntityWire) {
			if (((TileEntityWire) tile).isConnectedTo(EnumFacing.DOWN))
				BB = BB.union(this.isEnamel() ? DOWN_BB.grow(0.0625, 0, 0.0625) : DOWN_BB);
			if (((TileEntityWire) tile).isConnectedTo(EnumFacing.UP))
				BB = BB.union(this.isEnamel() ? UP_BB.grow(0.0625, 0, 0.0625) : UP_BB);
			if (((TileEntityWire) tile).isConnectedTo(EnumFacing.NORTH))
				BB = BB.union(this.isEnamel() ? NORTH_BB.grow(0.0625, 0.0625, 0) : NORTH_BB);
			if (((TileEntityWire) tile).isConnectedTo(EnumFacing.SOUTH))
				BB = BB.union(this.isEnamel() ? SOUTH_BB.grow(0.0625, 0.0625, 0) : SOUTH_BB);
			if (((TileEntityWire) tile).isConnectedTo(EnumFacing.WEST))
				BB = BB.union(this.isEnamel() ? WEST_BB.grow(0, 0.0625, 0.0625) : WEST_BB);
			if (((TileEntityWire) tile).isConnectedTo(EnumFacing.EAST))
				BB = BB.union(this.isEnamel() ? EAST_BB.grow(0, 0.0625, 0.0625) : EAST_BB);
		}
		return BB;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityWire)
			if (!((TileEntityWire) tile).dumpEnergy()) { // couldnt dump energy into nearby storages
				((TileEntityWire) tile).electrocuteBreaker(player);
			}
		super.onBlockHarvested(worldIn, pos, state, player);
	}

}