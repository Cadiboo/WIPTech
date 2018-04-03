package cadiboo.wiptech.block;

import cadiboo.wiptech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBase extends Block {

	public BlockBase(String name, Material materialIn) {
		super(materialIn);

		setHardness(1.0F);
		setHarvestLevel(null, 0);

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
	public Block setOreBlock()		{
		this.setHarvestLevel("Stone", 3);
		this.setHardness(3.0F);
		return isOreBlock = this;
	}
	public final boolean isOreBlock() {	return this == isOreBlock;	}

	//Tile Entity
	private Block isTileEntity;
	public Block setTileEntity()	{
		this.setHardness(4.0F);
		return isTileEntity = this;
	}
	public boolean isTileEntity()	{	return this == isTileEntity;	}

	//Non-Solid Block
	private Block nonSolidBlock;
	public Block setNonSolidBlock()	{	return nonSolidBlock = this; }
	public boolean isNonSolidBlock()	{	return this== nonSolidBlock; }

	//Hidden Block (Not displayed in creative tab)
	private Block hiddenBlock;
	public Block setHiddenBlock()	{	return hiddenBlock = this;	 }
	public boolean isHiddenBlock()	{	return this== hiddenBlock;	 }

	//Tile Block Only (No item created for it)
	private Block tileOnly;
	public Block setTileOnly()		{
		this.setHiddenBlock();
		return tileOnly = this;
	}
	public boolean isTileOnly()		{	return this== tileOnly;		 }

	//Circuit Material (non-solid, breaks easily)
	private Block circuitMaterial;
	public Block setCircuitMaterial(){
		setNonSolidBlock();
		setHardness(0.1F);
		return circuitMaterial = this;
	}
	public boolean isCircuitMaterial(){	return this== circuitMaterial; }

	//Circuit (possibly use it for electricity checks in future)
	private Block circuit;
	public Block setCircuit(){
		setCircuitMaterial();
		return circuit = this;
	}
	public boolean isCircuit(){	return this== circuit; }

	@Override
	public final boolean isOpaqueCube(IBlockState state)
	{
		return !this.isNonSolidBlock();
	}

	@Override
	public final boolean isFullCube(IBlockState state) {
		return !this.isNonSolidBlock();
	}
	
	private Block transparentBlock;
	public Block setTransparentBlock(){
		return transparentBlock = this;
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return transparentBlock==this?0:super.getLightOpacity(state, world, pos);
	}

}
