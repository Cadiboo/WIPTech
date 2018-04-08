package cadiboo.wiptech.block.transmission;

import cadiboo.wiptech.block.BlockBase;
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
