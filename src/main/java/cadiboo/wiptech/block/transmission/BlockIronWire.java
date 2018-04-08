package cadiboo.wiptech.block.transmission;

import cadiboo.wiptech.block.BlockBase;
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
