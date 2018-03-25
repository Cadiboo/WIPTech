package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;

public class BlockSilverWire
  extends BlockBase
{
  public BlockSilverWire(String name, Material material)
  {
    super(name, material);
    setCircuit();
  }
}
