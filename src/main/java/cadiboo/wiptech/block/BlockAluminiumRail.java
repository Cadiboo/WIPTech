package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockAluminiumRail
extends BlockBase
{
	public BlockAluminiumRail(String name, Material material)
	{
		super(name, material);
		setCircuitMaterial();
	}
}
