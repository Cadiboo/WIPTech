package cadiboo.wiptech.block.transmission;

import cadiboo.wiptech.block.BlockBase;
import net.minecraft.block.material.Material;

public class BlockCopperEnamel
extends BlockBase
{
	public BlockCopperEnamel(String name, Material material)
	{
		super(name, material);
		setCircuit();
	}
}
