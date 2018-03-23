package cadiboo.wiptech.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.concurrent.Callable;

public class CapabilityModuleHandler
{
	@CapabilityInject(IModuleHandler.class)
	public static Capability<IModuleHandler> MODULE_HANDLER_CAPABILITY = null;

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IModuleHandler.class, new Capability.IStorage<IModuleHandler>()
		{
			@Override
			public NBTBase writeNBT(Capability<IModuleHandler> capability, IModuleHandler instance, EnumFacing side)
			{
				NBTTagList nbtTagList = new NBTTagList();
				for (int i = 0; i < size; i++)
				{
					ItemStack stack = instance.getStackInSlot(i);
					if (!stack.isEmpty())
					{
						NBTTagCompound itemTag = new NBTTagCompound();
						itemTag.setInteger("Slot", i);
						stack.writeToNBT(itemTag);
						nbtTagList.appendTag(itemTag);
					}
				}
				return nbtTagList;
			}

			@Override
			public void readNBT(Capability<IModuleHandler> capability, IModuleHandler instance, EnumFacing side, NBTBase base)
			{
				NBTTagList tagList = (NBTTagList) base;
				for (int i = 0; i < tagList.tagCount(); i++)
				{
					NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
					int j = itemTags.getInteger("Slot");

					if (j >= 0 && j < instance.getSlots())
					{
						itemHandlerModifiable.setStackInSlot(j, new ItemStack(itemTags));
					}
				}
			}
		}, ItemStackHandler::new);
	}

}