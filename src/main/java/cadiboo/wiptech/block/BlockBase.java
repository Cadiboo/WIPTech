package cadiboo.wiptech.block;

import cadiboo.wiptech.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBase extends Block {

	public BlockBase(String name, Material materialIn) {
		super(materialIn);
		this.setRegistryName(new ResourceLocation(Reference.ID, name));
		this.setUnlocalizedName(name);
	}
	
	//Beacon Base
	private Block beaconBase;
	public Block setBeaconBase(){
		return beaconBase = this;
	}
	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon)
	{
		return this == beaconBase;
	}
	
	//Ore Block
	private Block isOreBlock;
	public Block setOreBlock(){
		return isOreBlock = this;
	}
	public final boolean isOreBlock()
	{
		return this == isOreBlock;
	}
	
	//Tile Entity
	private Block isTileEntity;
	public Block setTileEntity(){
		return isTileEntity = this;
	}
	public final boolean isTileEntity()
	{
		return this == isTileEntity;
	}

	//Non-Solid Block
	private Block nonSolidBlock;
	public Block setNonSolidBlock(){
		return nonSolidBlock = this;
	}
	public final boolean isNonSolidBlock()
	{
		return this == nonSolidBlock;
	}

	@Override
	public final boolean isOpaqueCube(IBlockState state)
	{
		return !this.isNonSolidBlock();
	}

	@Override
	public final boolean isFullCube(IBlockState state) {
		return !this.isNonSolidBlock();
	}

}
