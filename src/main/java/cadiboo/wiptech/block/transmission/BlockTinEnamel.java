package cadiboo.wiptech.block.transmission;

import cadiboo.wiptech.block.BlockBase;
import net.minecraft.block.material.Material;

public class BlockTinEnamel
extends BlockBase
{
	public BlockTinEnamel(String name, Material material)
	{
		super(name, material);
		setCircuit();
	}
}
