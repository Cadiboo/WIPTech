package cadiboo.wiptech.item;

import cadiboo.wiptech.Reference;
import cadiboo.wiptech.entity.projectile.EntityFerromagneticProjectile;
import cadiboo.wiptech.handler.EnumHandler;
import cadiboo.wiptech.init.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemFerromagneticProjectile extends ItemBase {

	public static final String[] itemNames = {
			"iron_rod_large",
			"tungsten_rod_large",
			"osmium_rod_large",

			"iron_rod_medium",
			"tungsten_rod_medium",
			"osmium_rod_medium",

			"iron_rod_small",
			"tungsten_rod_small",
			"osmium_rod_small",

			//"plasma",

	};
	public static final int subTypesAmmount = itemNames.length;
	public static int subTypesAmmountZI = itemNames.length-1; //Zero Indexed

	public ItemFerromagneticProjectile(String name)
	{
		super(name);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		int i = stack.getMetadata();
		if(i>subTypesAmmount)
			i=0;
		return super.getUnlocalizedName() + "." + EnumHandler.FerromagneticProjectiles.byMetadata(i).getUnlocalizedName();
	}

	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab)) {
			for (int i = 0; i < subTypesAmmount; i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	public EntityFerromagneticProjectile createProjectile(World worldIn, ItemStack stack, EntityLivingBase shooter, boolean isPlasma)
	{
		EntityFerromagneticProjectile projectile = new EntityFerromagneticProjectile(worldIn, shooter);
		if(!isPlasma)
			projectile.setAmmoId(stack.getMetadata());
		else
			projectile.setAmmoId(9);
		projectile.setDamage(EntityFerromagneticProjectile.getProjectileDamage(stack));
		projectile.setKnockbackStrength(EntityFerromagneticProjectile.getProjectileKnockback(stack));

		return projectile;
	}

	public boolean isInfinite(ItemStack itemstack, ItemStack stack, EntityPlayer entityplayer) {
		return Reference.DEBUG_ENABLED;
	}

}
