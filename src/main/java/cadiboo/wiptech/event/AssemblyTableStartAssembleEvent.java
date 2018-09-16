package cadiboo.wiptech.event;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class AssemblyTableStartAssembleEvent extends Event {

	@Nonnull
	private final ItemStack			itemStack;
	private final List<ItemStack>	attachments;
	private int						assembleTime;

	public AssemblyTableStartAssembleEvent(@Nonnull final ItemStack itemStack, final List<ItemStack> attachments, final int assembleTime) {
		this.itemStack = itemStack;
		this.attachments = Collections.unmodifiableList(attachments);
		this.assembleTime = assembleTime;
	}

	/**
	 * Get the ItemStack in question.
	 */
	@Nonnull
	public ItemStack getItemStack() {
		return this.itemStack;
	}

	/**
	 * Set the assemble time for the given ItemStack.
	 */
	public void setAssembleTime(final int assembleTime) {
		this.assembleTime = assembleTime;
		this.setCanceled(true);
	}

	public List<ItemStack> getAttachments() {
		return this.attachments;
	}

	/**
	 * The resulting value of this event, the assemble time for the ItemStack.
	 */
	public int getAssembleTime() {
		return this.assembleTime;
	}

}
