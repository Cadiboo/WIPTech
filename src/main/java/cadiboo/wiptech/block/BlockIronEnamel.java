package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockIronEnamel
extends BlockBase
{
	public BlockIronEnamel(String name, Material material)
	{
		super(name, material);
		setCircuit();
	}
}
