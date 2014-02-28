package kerpow.Skitter.Blocks;

import java.util.ArrayList;
import java.util.Random;

import kerpow.Skitter.Skitter;
import kerpow.Skitter.Entities.EntitySkitterBase;
import kerpow.Skitter.Entities.EntitySkitterWarrior;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSkitteringPlague extends Block {
	public BlockSkitteringPlague(int par1) {
		super(par1, Material.materialCarpet);
		// this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabDecorations);

		this.setTextureName("skitter:plague");
		this.setLightOpacity(1);
		this.setHardness(.1F);
	}

	public boolean canDestroy(World world, int x, int y, int z) {
		final int blockID = world.getBlockId(x, y, z);
		final Block block = Block.blocksList[blockID];

		// air
		if (blockID == 0)
			return true;

		if (blockID == Skitter.blockSkitterPlague.blockID || blockID == Skitter.blockSkitterWeb.blockID)
			return false;

		if (world.getBlockMaterial(x, y, z).isLiquid())
			return false;

		if (!block.isOpaqueCube())
			return true;

		if (block.isWood(world, x, y, z))
			return true;

		if (block.isLeaves(world, x, y, z))
			return true;

		if (block.isBlockReplaceable(world, x, y, z))
			return true;

		// flowers
		if (blockID == Block.plantRed.blockID)
			return true;
		if (blockID == Block.plantYellow.blockID)
			return true;

		return false;
	}

	/**
	 * calls setBlockBounds based on the depth of the snow. Int is any values
	 * 0x0-0x7, usually this blocks metadata.
	 */
	/*
	 * protected void setBlockBoundsForSnowDepth(int par1) { int j = par1 & 7;
	 * float f = (float) (2 * (1 + j)) / 16.0F; this.setBlockBounds(0.0F, 0.0F,
	 * 0.0F, 1.0F, f, 1.0F); }
	 */
	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {

		// if this block is air and it's adjacent to a non-air block
		if (this.canDestroy(world, x, y, z) && this.hasAdjacentFullCube(world, x, y, z))
			return true;

		return false;
	}

	/**
	 * Return true if a player with Silk Touch can harvest this block directly,
	 * and not its normal drops.
	 */
	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	/**
	 * This gets the job done but i'd like a cooler effect for it
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	private void destroyTreeAbove(World world, int x, int y, int z) {
		for (int y1 = y; y1 < y + 50; y1++) {

			final int l = world.getBlockId(x, y1, z);
			final Block block = Block.blocksList[l];
			if (block != null && (block.isWood(world, x, y1, z) || block.isLeaves(world, x, y1, z)))
				world.setBlockToAir(x, y1, z);

		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */

	private void doPlagueSpread(World world, int xpos, int ypos, int zpos) {

		for (int x = xpos - 1; x <= xpos + 1; x++)
			for (int y = ypos - 1; y <= ypos + 1; y++)
				for (int z = zpos - 1; z <= zpos + 1; z++)
					this.spreadHere(world, x, y, z);
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {

		return new ArrayList<ItemStack>();
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	@Override
	public int getRenderType() {
		return Skitter.rendererSkitterPlague;
	}

	private boolean hasAdjacentFullCube(World world, int x, int y, int z) {
		// make sure an adjacent block is not destroyable

		int b = 0;
		b = world.getBlockId(x + 1, y, z);
		if (b != 0 && b != Skitter.blockSkitterPlague.blockID && b != Skitter.blockSkitterWeb.blockID)
			return true;

		b = world.getBlockId(x - 1, y, z);
		if (b != 0 && b != Skitter.blockSkitterPlague.blockID && b != Skitter.blockSkitterWeb.blockID)
			return true;

		b = world.getBlockId(x, y + 1, z);
		if (b != 0 && b != Skitter.blockSkitterPlague.blockID && b != Skitter.blockSkitterWeb.blockID)
			return true;

		b = world.getBlockId(x, y - 1, z);
		if (b != 0 && b != Skitter.blockSkitterPlague.blockID && b != Skitter.blockSkitterWeb.blockID)
			return true;

		b = world.getBlockId(x, y, z + 1);
		if (b != 0 && b != Skitter.blockSkitterPlague.blockID && b != Skitter.blockSkitterWeb.blockID)
			return true;

		b = world.getBlockId(x, y, z - 1);
		if (b != 0 && b != Skitter.blockSkitterPlague.blockID && b != Skitter.blockSkitterWeb.blockID)
			return true;

		return false;
	}

	@Override
	public boolean isBlockFoliage(World world, int x, int y, int z) {
		return true;
	}

	/**
	 * Determines if a new block can be replace the space occupied by this one,
	 * Used in the player's placement code to make the block act like water, and
	 * lava.
	 * 
	 * @param world
	 *            The current world
	 * @param x
	 *            X Position
	 * @param y
	 *            Y position
	 * @param z
	 *            Z position
	 * @return True if the block is replaceable by another block
	 */
	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
		return true;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		if (!par1World.isRemote)
			this.doPlagueSpread(par1World, par2, par3, par4);
		return true;
	}

	@Override
	public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		super.onEntityWalking(par1World, par2, par3, par4, par5Entity);

		
		
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		super.onNeighborBlockChange(par1World, par2, par3, par4, par5);

		if (!this.hasAdjacentFullCube(par1World, par2, par3, par4)) {
			par1World.setBlockToAir(par2, par3, par4);
			return;
		}

	}

	public int removeNearbyPlague(World world, int xpos, int ypos, int zpos) {
		final int plagueCount = 0;

		// i'm to lazy to make a proper radius algorithm so here's this instead
		for (int y = ypos - 1; y <= ypos + 1; y++) {
			for (int x = xpos - 1; x <= xpos + 1; x++)
				for (int z = zpos - 1; z <= zpos + 1; z++)
					this.removePlague(world, x, y, z);

			// remove a little extra so it's a perfect square
			this.removePlague(world, xpos + 2, y, zpos);
			this.removePlague(world, xpos - 1, y, zpos);
			this.removePlague(world, xpos, y, zpos + 2);
			this.removePlague(world, xpos, y, zpos - 2);
		}

		return plagueCount;
	}

	public boolean removePlague(World world, int x, int y, int z) {
		if (world.getBlockId(x, y, z) == Skitter.blockSkitterPlague.blockID) {

			world.setBlockToAir(x, y, z);
			return true;

		}
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: blockAccess, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return par5 == 1 ? true : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
	}

	/**
	 * Checks if the block is destroyable
	 * 
	 * @param world
	 * @param xpos
	 * @param ypos
	 * @param zpos
	 */
	private void spreadHere(World world, int x, int y, int z) {
		// only a chance to spread

		// throttle the spread a bit
		if (world.rand.nextFloat() < .8F && this.canPlaceBlockAt(world, x, y, z)) {
			world.setBlock(x, y, z, Skitter.blockSkitterPlague.blockID);
			this.destroyTreeAbove(world, x, y, z);
		}
	}

	@Override
	public void updateTick(World world, int xpos, int ypos, int zpos, Random par5Random) {

		if (!this.hasAdjacentFullCube(world, xpos, ypos, zpos)) {
			world.setBlockToAir(xpos, ypos, zpos);
			return;
		}

		if (world.getBlockLightValue(xpos, ypos, zpos) < 4)
			// check spread
			this.doPlagueSpread(world, xpos, ypos, zpos);
		else
			world.setBlockToAir(xpos, ypos, zpos);

		// check spawn
		if (world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xpos - 10, ypos - 3, zpos - 10, xpos + 10, ypos + 3, zpos + 10)).size() > 0
				&& world.getEntitiesWithinAABB(EntitySkitterBase.class, AxisAlignedBB.getBoundingBox(xpos - 10, ypos - 3, zpos - 10, xpos + 10, ypos + 3, zpos + 10)).size() < 10) {
			// spawn skitter
			final EntitySkitterWarrior skitter = new EntitySkitterWarrior(world);
			skitter.setLocationAndAngles(xpos, ypos, zpos, 0, 0.0F);
			if (skitter.getCanSpawnHere())
				// this.removeNearbyPlague(world, xpos, ypos, zpos);
				world.spawnEntityInWorld(skitter);
		}
	}
}
