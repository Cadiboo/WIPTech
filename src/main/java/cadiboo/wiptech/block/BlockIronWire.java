package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockIronWire
  extends BlockBase
{
  public BlockIronWire(String name, Material material)
  {
    super(name, material);
    setCircuit();
  }
}
