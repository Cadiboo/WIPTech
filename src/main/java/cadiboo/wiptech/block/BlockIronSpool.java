package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockIronSpool extends BlockBase
{

	public BlockIronSpool(String name, Material material)
	{
		super(name, material);
		this.setNonSolidBlock();
	}
	
}