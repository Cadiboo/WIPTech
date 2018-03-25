package cadiboo.wiptech.item;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.IWeaponModular;
import cadiboo.wiptech.capability.WeaponModular;
import cadiboo.wiptech.handler.EnumHandler;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Circuits;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Coils;
import cadiboo.wiptech.handler.EnumHandler.WeaponModules.Scopes;
import cadiboo.wiptech.init.Capabilities;
import cadiboo.wiptech.init.Capabilities.CapabilityWeaponModular;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.provider.ModularWeaponProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemTestModularWeapon extends ItemBase {

	//private final IWeaponModular modules = null;

	public ItemTestModularWeapon(String name) {
		super(name);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if( itemstack.getItem() == Items.TEST_MODULAR_WEAPON ) {
			WIPTech.logger.info(itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null).getModules());
			WIPTech.logger.info(itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null).getModuleList());
			int circuit = itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null).getCircuit().getID();
			circuit++;
			if(circuit>2)
				circuit=0;
			itemstack.getCapability(Capabilities.MODULAR_WEAPON_CAPABILITY, null).setCircuit(Circuits.byID(circuit));
		}

		playerIn.setActiveHand(handIn);
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public ICapabilityProvider initCapabilities( ItemStack item, NBTTagCompound nbt ) {
		if( item.getItem() == Items.TEST_MODULAR_WEAPON ) {
			return new ModularWeaponProvider();
		}
		return null;
	}

}
