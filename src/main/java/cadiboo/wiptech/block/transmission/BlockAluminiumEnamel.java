package cadiboo.wiptech.block.transmission;

import cadiboo.wiptech.block.BlockBase;
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
