package cadiboo.wiptech.item;

import java.util.Set;

import com.google.common.collect.Sets;

import cadiboo.wiptech.handler.EnumHandler;
import cadiboo.wiptech.handler.EnumHandler.ToolTypes;
import cadiboo.wiptech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class ItemTool extends net.minecraft.item.ItemTool {

	public static Item.ToolMaterial	COPPER		= EnumHelper.addToolMaterial("WIPTECH_COPPER", 2, 750, 6.0F, 2.0F, 14);
	public static Item.ToolMaterial	TIN			= EnumHelper.addToolMaterial("WIPTECH_TIN", 1, 250, 6.0F, 2.0F, 14);
	public static Item.ToolMaterial	ALUMINIUM	= EnumHelper.addToolMaterial("WIPTECH_ALUMINIUM", 2, 500, 6.0F, 2.0F, 14);
	public static Item.ToolMaterial	SILVER		= EnumHelper.addToolMaterial("WIPTECH_SILVER", 2, 1000, 10.0F, 3.0F, 10);
	public static Item.ToolMaterial	OSMIUM		= EnumHelper.addToolMaterial("WIPTECH_OSMIUM", 3, 2000, 6.0F, 3.0F, 6);
	public static Item.ToolMaterial	TUNGSTEN	= EnumHelper.addToolMaterial("WIPTECH_TUNGSTEN", 3, 3000, 6.0F, 5.0F, 2);
	public static Item.ToolMaterial	TITANIUM	= EnumHelper.addToolMaterial("WIPTECH_TITANIUM", 3, 2500, 16.0F, 5.0F, 10);

	public static final Set<Block> SWORD_EFFECTIVE_ON = Sets.newHashSet(Blocks.WEB);

	public static final Set<Block> HOE_EFFECTIVE_ON = Sets.newHashSet();

	public static final Set<Block> SHEARS_EFFECTIVE_ON = Sets.newHashSet();

	public static final Set<Block> SPADE_EFFECTIVE_ON = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW,
			Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER);

	public static final Set<Block> AXE_EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK,
			Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);

	public static final Set<Block> PICKAXE_EFFECTIVE_ON = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE,
			Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE,
			Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE,
			Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE);

	private Set<Block>				effectiveOn;
	private EnumHandler.ToolTypes	tool;

	public ItemTool(String name, Item.ToolMaterial material, Set effectiveBlocks, EnumHandler.ToolTypes toolIn) {
		super(material.getAttackDamage(), material.getAttackDamage(), material, effectiveBlocks);
		setRegistryName(Reference.ID, name);
		setUnlocalizedName(name);
		effectiveOn = effectiveBlocks;
		tool = toolIn;
	}

	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
		return tool != ToolTypes.SWORD;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		switch (tool) {
			case PICKAXE:
				return pickaxeCanHarvestBlock(blockIn);
			case SHOVEL:
				return shovelCanHarvestBlock(blockIn);
			default:
				return effectiveOn.contains(blockIn.getBlock());
		}
	}

	public boolean shovelCanHarvestBlock(IBlockState blockIn) {
		Block block = blockIn.getBlock();

		if (block == Blocks.SNOW_LAYER) {
			return true;
		} else {
			return block == Blocks.SNOW;
		}
	}

	public boolean pickaxeCanHarvestBlock(IBlockState blockIn) {
		Block block = blockIn.getBlock();

		if (block == Blocks.OBSIDIAN) {
			return this.toolMaterial.getHarvestLevel() == 3;
		} else if (block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE) {
			if (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK) {
				if (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE) {
					if (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE) {
						if (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE) {
							if (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE) {
								Material material = blockIn.getMaterial();

								if (material == Material.ROCK) {
									return true;
								} else if (material == Material.IRON) {
									return true;
								} else {
									return material == Material.ANVIL;
								}
							} else {
								return this.toolMaterial.getHarvestLevel() >= 2;
							}
						} else {
							return this.toolMaterial.getHarvestLevel() >= 1;
						}
					} else {
						return this.toolMaterial.getHarvestLevel() >= 1;
					}
				} else {
					return this.toolMaterial.getHarvestLevel() >= 2;
				}
			} else {
				return this.toolMaterial.getHarvestLevel() >= 2;
			}
		} else {
			return this.toolMaterial.getHarvestLevel() >= 2;
		}
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		switch (tool) {
			case SWORD:
				return swordGetDestroySpeed(stack, state);
			case AXE:
				return axeGetDestroySpeed(stack, state);
			case PICKAXE:
				return pickaxeGetDestroySpeed(stack, state);
			default:
				return super.getDestroySpeed(stack, state);
		}
	}

	public float pickaxeGetDestroySpeed(ItemStack stack, IBlockState state) {
		Material material = state.getMaterial();
		return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? super.getDestroySpeed(stack, state) : this.efficiency;
	}

	public float swordGetDestroySpeed(ItemStack stack, IBlockState state) {
		Block block = state.getBlock();

		if (block == Blocks.WEB) {
			return 15.0F;
		} else {
			Material material = state.getMaterial();
			return material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
		}
	}

	public float axeGetDestroySpeed(ItemStack stack, IBlockState state) {
		Material material = state.getMaterial();
		return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		switch (tool) {
			case HOE:
				return hoeOnItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
			case SHOVEL:
				return shovelOnItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
			default:
				return EnumActionResult.PASS;
		}
	}

	@SuppressWarnings("incomplete-switch")
	public EnumActionResult hoeOnItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);

		if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
			return EnumActionResult.FAIL;
		} else {
			int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
			if (hook != 0)
				return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
				if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
					this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
					return EnumActionResult.SUCCESS;
				}

				if (block == Blocks.DIRT) {
					switch (iblockstate.getValue(BlockDirt.VARIANT)) {
						case DIRT:
							this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
							return EnumActionResult.SUCCESS;
						case COARSE_DIRT:
							this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
							return EnumActionResult.SUCCESS;
					}
				}
			}

			return EnumActionResult.PASS;
		}
	}

	public EnumActionResult shovelOnItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);

		if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (facing != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && block == Blocks.GRASS) {
				IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
				worldIn.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				if (!worldIn.isRemote) {
					worldIn.setBlockState(pos, iblockstate1, 11);
					itemstack.damageItem(1, player);
				}

				return EnumActionResult.SUCCESS;
			} else {
				return EnumActionResult.PASS;
			}
		}
	}

	protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
		worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

		if (!worldIn.isRemote) {
			worldIn.setBlockState(pos, state, 11);
			stack.damageItem(1, player);
		}
	}

}
