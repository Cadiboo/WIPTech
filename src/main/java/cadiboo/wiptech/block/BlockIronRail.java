package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockIronRail
extends BlockBase
{
	public BlockIronRail(String name, Material material)
	{
		super(name, material);
		setCircuitMaterial();
	}
}
