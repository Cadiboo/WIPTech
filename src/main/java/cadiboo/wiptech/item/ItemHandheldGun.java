package cadiboo.wiptech.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cadiboo.wiptech.capability.attachments.AttachmentList;
import cadiboo.wiptech.capability.attachments.CapabilityAttachmentList;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.capability.inventory.ModItemStackHandler;
import cadiboo.wiptech.util.ModEnums.AttachmentPoints;
import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class ItemHandheldGun extends Item implements IModItem {

	private final AttachmentList attachments;

	public ItemHandheldGun(final String name, final AttachmentPoints... attachmentPoints) {
		super();
		ModUtil.setRegistryNames(this, name);
		this.attachments = new AttachmentList(attachmentPoints);
		this.setMaxDamage(100);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumAction getItemUseAction(final ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack) {
		return 72000;
	}

	@Override
	public boolean showDurabilityBar(final ItemStack stack) {
		return true;
	}

	public abstract void shoot(final World world, final EntityPlayer player);

	/* when player starts right clicking */
	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {

		final ItemStack itemstack = player.getHeldItem(hand);

		final ItemStack circuitStack = this.getAttachmentList().getCircuit();
		final Item circuitItem = circuitStack.getItem();

		if (circuitStack.isEmpty() || (circuitItem == null)) {
			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		}

		if (circuitItem instanceof ItemCircuit) {
			final CircuitTypes circuit = ((ItemCircuit) circuitItem).getType();
			switch (circuit) {
			case AUTO:
			case BURST3:
			case BURST5:
			case MANUAL:
				return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
			default:
				return new ActionResult<>(EnumActionResult.FAIL, itemstack);
			}
		}

		return new ActionResult<>(EnumActionResult.PASS, itemstack);

	}

	/* when player stops right clicking */
	@Override
	public void onPlayerStoppedUsing(final ItemStack stack, final World world, final EntityLivingBase entityLiving, final int timeLeft) {

		if (!(entityLiving instanceof EntityPlayer)) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) entityLiving;
		final EnumHand hand = player.getActiveHand();

		final ItemStack itemstack = player.getHeldItem(hand);

		final ItemStack circuitStack = this.getAttachmentList().getCircuit();
		final Item circuitItem = circuitStack.getItem();

		if (circuitStack.isEmpty() || (circuitItem == null)) {
			return;
		}

		if (circuitItem instanceof ItemCircuit) {
			final CircuitTypes circuit = ((ItemCircuit) circuitItem).getType();
			switch (circuit) {
			case AUTO:
			case BURST3:
			case BURST5:
			case MANUAL:
				this.shoot(world, player);
			default:

				return;
			}
		}

		return;

	}

	/* called every tick right click is held down */
	@Override
	public void onUsingTick(final ItemStack stack, final EntityLivingBase entityLiving, final int count) {

		if (!(entityLiving instanceof EntityPlayer)) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) entityLiving;
		final World world = player.world;
		final EnumHand hand = player.getActiveHand();

		final ItemStack itemstack = player.getHeldItem(hand);

		final ItemStack circuitStack = this.getAttachmentList().getCircuit();
		final Item circuitItem = circuitStack.getItem();

		if (circuitStack.isEmpty() || (circuitItem == null)) {
			return;
		}

		if (circuitItem instanceof ItemCircuit) {
			final CircuitTypes circuit = ((ItemCircuit) circuitItem).getType();
			switch (circuit) {
			case AUTO:
			case BURST3:
			case BURST5:
				this.shoot(world, player);
			default:
			case MANUAL:

				return;
			}
		}

		return;

	}

	/* when the item is FINISHED being used (I.e. when getMaxItemUseDuration is exceded) */
	@Override
	public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving) {
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	/* block right clicked */
	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	/* block right clicked, before block is notifed */
	@Override
	public EnumActionResult onItemUseFirst(final EntityPlayer player, final World world, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ, final EnumHand hand) {
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	public ModEnergyStorage getEnergy() {
		return null;
	}

	public ModItemStackHandler getInventory() {
		return null;
	}

	public AttachmentList getAttachmentList() {
		return this.attachments;
	}

	@Override
	public final ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
		return new ICapabilityProvider() {

			@Override
			public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
				return this.getCapability(capability, facing) != null;
			}

			@Nullable
			@Override
			public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
				if (capability == CapabilityEnergy.ENERGY) {
					return (T) ItemHandheldGun.this.getEnergy();
				}

				if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
					return (T) ItemHandheldGun.this.getInventory();
				}

				if (capability == CapabilityAttachmentList.ATTACHMENT_LIST) {
					return (T) ItemHandheldGun.this.getAttachmentList();
				}

				return null;
			}
		};
	}

}
