package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockAluminiumEnamel
extends BlockBase
{
	public BlockAluminiumEnamel(String name, Material material)
	{
		super(name, material);
		setCircuit();
	}
}
