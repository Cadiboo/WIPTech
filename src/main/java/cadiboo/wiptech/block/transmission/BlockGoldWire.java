package cadiboo.wiptech.block.transmission;

import cadiboo.wiptech.block.BlockBase;
import net.minecraft.block.material.Material;

public class BlockGoldWire extends BlockBase {
	public BlockGoldWire(String name, Material material)
	{
		super(name, material);
		setCircuit();
	}
}
