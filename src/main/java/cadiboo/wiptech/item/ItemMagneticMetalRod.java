package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.projectile.EntityRailgunProjectile;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemMagneticMetalRod
extends ItemBase
{
	public static final String[] itemNames = { "iron_rod", "tungsten_rod", "osmium_rod" };

	public ItemMagneticMetalRod(String name)
	{
		super(name);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		int i = stack.getMetadata();
		return super.getUnlocalizedName() + "." + EnumHandler.MagneticMetalRods.byMetadata(i).getUnlocalizedName();
	}

	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab)) {
			for (int i = 0; i < 3; i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	public EntityRailgunProjectile createRailgunProjectile(World worldIn, ItemStack stack, EntityLivingBase shooter)
	{
		EntityRailgunProjectile railgunProjectile = new EntityRailgunProjectile(worldIn, shooter);
		railgunProjectile.setRodId(this.getDamage(stack));
		return railgunProjectile;
	}
}
