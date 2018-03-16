package cadiboo.wiptech.block;

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
