package cadiboo.wiptech.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.IBlockAccess;

public class BlockGoldNugget
  extends BlockBase
{
  public BlockGoldNugget(String name, Material material)
  {
    super(name, material);
    setNonSolidBlock();
    setHiddenBlock();
    setHardness(0.1F);
  }
  
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
  {
    drops.add(new ItemStack((Item)Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_nugget"))));
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
  {
    return new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.25D, 0.75D);
  }
}
