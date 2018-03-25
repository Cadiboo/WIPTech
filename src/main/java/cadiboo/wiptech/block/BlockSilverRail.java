package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockSilverRail
extends BlockBase
{
	public BlockSilverRail(String name, Material material)
	{
		super(name, material);
		setCircuitMaterial();
	}
}
