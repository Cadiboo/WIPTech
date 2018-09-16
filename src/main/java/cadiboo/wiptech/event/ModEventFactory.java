package cadiboo.wiptech.event;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class ModEventFactory {

	public static int getItemSmeltTime(@Nonnull final ItemStack itemStack) {
		final ModFurnaceItemSmeltTimeEvent event = new ModFurnaceItemSmeltTimeEvent(itemStack, 200);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getSmeltTime() < 0) {
			return 200;
		}
		return event.getSmeltTime();
	}

	public static int getItemAssembleTime(@Nonnull final ItemStack itemStack, final List<ItemStack> attachments) {
		final AssemblyTableStartAssembleEvent event = new AssemblyTableStartAssembleEvent(itemStack, Collections.unmodifiableList(attachments), 100);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getAssembleTime() < 0) {
			return 100;
		}
		return event.getAssembleTime();
	}

}
