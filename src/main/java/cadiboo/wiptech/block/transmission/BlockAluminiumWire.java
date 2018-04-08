package cadiboo.wiptech.block.transmission;

import cadiboo.wiptech.block.BlockBase;
import net.minecraft.block.material.Material;

public class BlockAluminiumWire
  extends BlockBase
{
  public BlockAluminiumWire(String name, Material material)
  {
    super(name, material);
    setCircuit();
  }
}
