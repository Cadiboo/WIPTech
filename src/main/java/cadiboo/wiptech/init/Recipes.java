package cadiboo.wiptech.init;

import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;

public class Recipes
{
  private static ArrayList<ArrayList> crushRecipes = new ArrayList();
  private static ArrayList<ArrayList> hammerRecipes = new ArrayList();
  private static ArrayList<ArrayList> coilRecipes = new ArrayList();
  
  public static void createRecipes()
  {
    addCrushRecipe(
      new ItemStack(Blocks.BAUXITE_ORE), 
      new ItemStack(Items.ALUMINA), 
      new ItemStack(Items.GALLIUM), 
      new ItemStack(Items.IRON_OXIDE), 
      new ItemStack(Items.SILICA), 
      new ItemStack(Items.TITANIA), 
      new ItemStack(net.minecraft.init.Blocks.GRAVEL), 
      100);
    
    addCrushRecipe(
      new ItemStack(net.minecraft.init.Blocks.SAND), 
      new ItemStack(Items.SILICON), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      20);
    
    addHammerRecipe(
      new ItemStack(Items.COPPER_INGOT), 
      new ItemStack(Blocks.COPPER_RAIL), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      20);
    
    addHammerRecipe(
      new ItemStack(Items.COPPER_NUGGET), 
      new ItemStack(Blocks.COPPER_WIRE), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      20);
    
    addHammerRecipe(
      new ItemStack((Item)Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_ingot"))), 
      new ItemStack(Blocks.GOLD_RAIL), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      20);
    
    addHammerRecipe(
      new ItemStack((Item)Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_nugget"))), 
      new ItemStack(Blocks.GOLD_WIRE), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      new ItemStack(net.minecraft.init.Blocks.AIR), 
      20);
    
    addCoilRecipe(
      new ItemStack(Blocks.GOLD_ENAMEL, 64), 
      new ItemStack(Blocks.GOLD_SPOOL, 4), 
      20);
    
    addCoilRecipe(
      new ItemStack(Blocks.GOLD_SPOOL), 
      new ItemStack(Items.GOLD_COIL, 4), 
      20);
  }
  
  public static ArrayList getCrushResult(ItemStack input)
  {
    for (ArrayList recipe : crushRecipes) {
      if (((ItemStack)recipe.get(0)).getItem() == input.getItem()) {
        return recipe;
      }
    }
    return null;
  }
  
  public static ArrayList getHammerResult(ItemStack input)
  {
    for (ArrayList recipe : hammerRecipes) {
      if (((ItemStack)recipe.get(0)).getItem() == input.getItem()) {
        return recipe;
      }
    }
    return null;
  }
  
  public static ArrayList getCoilResult(ItemStack input)
  {
    for (ArrayList recipe : coilRecipes) {
      if (((ItemStack)recipe.get(0)).getItem() == input.getItem()) {
        return recipe;
      }
    }
    return null;
  }
  
  public static ArrayList<ItemStack> getCrushResults(int slot)
  {
    if ((slot > 7) || (slot < 0)) {
      return null;
    }
    ArrayList<ItemStack> crushResults = new ArrayList();
    for (ArrayList recipe : crushRecipes) {
      crushResults.add((ItemStack)recipe.get(slot));
    }
    return crushResults;
  }
  
  public static ArrayList<ItemStack> getHammerResults(int slot)
  {
    if ((slot > 7) || (slot < 0)) {
      return null;
    }
    ArrayList<ItemStack> hammerResults = new ArrayList();
    for (ArrayList recipe : hammerRecipes) {
      hammerResults.add((ItemStack)recipe.get(slot));
    }
    return hammerResults;
  }
  
  public static ArrayList<ItemStack> getCoilResult(int slot)
  {
    if ((slot > 9) || (slot < 0)) {
      return null;
    }
    ArrayList<ItemStack> coilResults = new ArrayList();
    for (ArrayList recipe : coilRecipes) {
      coilResults.add((ItemStack)recipe.get(slot));
    }
    return coilResults;
  }
  
  public static void addCrushRecipe(ItemStack input, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4, ItemStack output5, ItemStack output6, int time)
  {
    ArrayList recipe = new ArrayList();
    recipe.add(input);
    recipe.add(output1);
    recipe.add(output2);
    recipe.add(output3);
    recipe.add(output4);
    recipe.add(output5);
    recipe.add(output6);
    recipe.add(Integer.valueOf(time));
    crushRecipes.add(recipe);
  }
  
  public static void addHammerRecipe(ItemStack input, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4, ItemStack output5, ItemStack output6, int time)
  {
    ArrayList recipe = new ArrayList();
    recipe.add(input);
    recipe.add(output1);
    recipe.add(output2);
    recipe.add(output3);
    recipe.add(output4);
    recipe.add(output5);
    recipe.add(output6);
    recipe.add(Integer.valueOf(time));
    hammerRecipes.add(recipe);
  }
  
  public static void addCoilRecipe(ItemStack input, ItemStack output, int time)
  {
    ArrayList recipe = new ArrayList();
    recipe.add(input);
    recipe.add(output);
    recipe.add(Integer.valueOf(time));
    coilRecipes.add(recipe);
  }
}
