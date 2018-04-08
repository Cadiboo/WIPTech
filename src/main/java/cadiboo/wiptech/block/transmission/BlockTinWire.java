package cadiboo.wiptech.block.transmission;

import cadiboo.wiptech.block.BlockBase;
import net.minecraft.block.material.Material;

public class BlockTinWire
  extends BlockBase
{
  public BlockTinWire(String name, Material material)
  {
    super(name, material);
    setCircuit();
  }
}
