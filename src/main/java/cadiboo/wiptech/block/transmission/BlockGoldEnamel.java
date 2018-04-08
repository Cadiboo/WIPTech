package cadiboo.wiptech.block.transmission;

import cadiboo.wiptech.block.BlockBase;
import net.minecraft.block.material.Material;

public class BlockGoldEnamel
  extends BlockBase
{
  public BlockGoldEnamel(String name, Material material)
  {
    super(name, material);
    setCircuit();
  }
}
