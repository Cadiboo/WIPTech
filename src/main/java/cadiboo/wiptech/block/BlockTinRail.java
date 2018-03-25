package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockTinRail
extends BlockBase
{
	public BlockTinRail(String name, Material material)
	{
		super(name, material);
		setCircuitMaterial();
	}
}
