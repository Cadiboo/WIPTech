package cadiboo.wiptech.block.transmission;

import cadiboo.wiptech.block.BlockBase;
import net.minecraft.block.material.Material;

public class BlockSilverEnamel
extends BlockBase
{
	public BlockSilverEnamel(String name, Material material)
	{
		super(name, material);
		setCircuit();
	}
}
