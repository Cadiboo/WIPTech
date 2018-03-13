package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockGoldSpool extends BlockBase
{

	public BlockGoldSpool(String name, Material material)
	{
		super(name, material);
		this.setNonSolidBlock();
	}
	
}