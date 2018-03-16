package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockGoldRail
extends BlockBase
{
	public BlockGoldRail(String name, Material material)
	{
		super(name, material);
		setCircuitMaterial();
	}
}
