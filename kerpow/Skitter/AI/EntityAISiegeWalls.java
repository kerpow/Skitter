package kerpow.Skitter.AI;

import kerpow.Skitter.Skitter;
import kerpow.Skitter.Entities.EntitySkitterWarrior;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.Vec3;

public class EntityAISiegeWalls extends EntityAIBase {
	private int breakingTime;
	/*
	 * private int lastX = 0; private int lastY = 0; private int lastZ = 0;
	 */
	private int breakX = 0;
	private int breakY = 0;
	private int breakZ = 0;

	private int lastX = 0;
	private int lastY = 0;
	private int lastZ = 0;

	private int targetBlockId = 0;
	private float timeToBreak = 240;

	private byte tickCount = 0;

	protected EntitySkitterWarrior entity;

	private int field_75358_j = -1;

	public EntityAISiegeWalls(EntityLiving par1EntityLiving) {
		this.entity = (EntitySkitterWarrior) par1EntityLiving;
		this.setMutexBits(3);
	}

	private boolean findBreakTarget() {
		int closestX = 0, closestY = 0, closestZ = 0;
		double closestDistance = 9999;

		boolean foundTarget = false;

		Vec3 targetLoc = Vec3.createVectorHelper(this.entity.getAttackTarget().posX, this.entity.getAttackTarget().posY, this.entity.getAttackTarget().posZ);
		Vec3 myLoc = Vec3.createVectorHelper(this.entity.posX, this.entity.posY, this.entity.posZ);

		double minimumDistance = myLoc.distanceTo(targetLoc);
		// of the available targets find the one closest to the target and set
		// the target
		for (int x = (int) this.entity.posX - 2; x <= this.entity.posX + 1; x++) {
			for (int y = (int) this.entity.boundingBox.minY - 2; y <= this.entity.boundingBox.maxY + 1; y++) {
				for (int z = (int) this.entity.posZ - 2; z <= this.entity.posZ + 1; z++) {
					double distance = targetLoc.distanceTo(Vec3.createVectorHelper(x, y, z));
					// Skitter.l("Checking " + x + " " + y + " " + z + " - " +
					// distance);
					// this.entity.worldObj.setBlock(x, y, z,
					// Block.glowStone.blockID);
					int blockId = this.entity.worldObj.getBlockId(x, y, z);

					if (!this.entity.worldObj.isAirBlock(x, y, z) && blockId != Skitter.blockSkitterWeb.blockID && blockId != Skitter.blockSkitterPlague.blockID && distance < closestDistance
							&& distance < minimumDistance) {
						closestDistance = distance;
						breakX = x;
						breakY = y;
						breakZ = z;
						foundTarget = true;
					}
				}
			}
		}
		return foundTarget;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		tickCount++;

		if (tickCount > 20) {
			tickCount = 0;

			// if i haven't moved much
			if (this.entity.getAttackTarget() != null && this.isStationary())
				if (this.findBreakTarget()) // found something to break break it
					return true;
				else
					this.placeWeb();

			lastX = (int) this.entity.posX;
			lastY = (int) this.entity.posY;
			lastZ = (int) this.entity.posZ;

		}
		return false;
	}

	private void placeWeb() {

		int x = (int) this.entity.posX;
		int y = (int) this.entity.posY;
		int z = (int) this.entity.posZ;

		this.entity.worldObj.setBlock(x, y, z, Skitter.blockSkitterWeb.blockID);
		this.entity.motionY += .5;
	}

	private boolean isStationary() {
		if (lastX != (int) this.entity.posX)
			return false;

		if (lastZ != (int) this.entity.posZ)
			return false;

		return true;
	}

	/*
	 * private boolean siegeWall() {
	 * 
	 * return setBreakTargetInArea((int) this.entity.posX - 2, (int)
	 * this.entity.posY, (int) this.entity.posZ - 2, (int) this.entity.posX + 2,
	 * (int) this.entity.posY + 1, (int) this.entity.posZ + 2);
	 * 
	 * }
	 * 
	 * private boolean setBreakTargetInArea(int minX, int minY, int minZ, int
	 * maxX, int maxY, int maxZ) {
	 * 
	 * // find a block between me and target for (int x = minX; x < maxX; x++)
	 * for (int y = minY; y < maxY; y++) for (int z = minZ; z < maxZ; z++) { if
	 * (!this.entity.worldObj.isAirBlock(x, y, z)) { targetX = x; targetY = y;
	 * targetZ = z; targetBlockId = this.entity.worldObj.getBlockId(x, y, z); if
	 * (targetBlockId != Skitter.blockSkitterPlague.blockID && targetBlockId !=
	 * Skitter.blockSkitterWeb.blockID) { timeToBreak = 120 *
	 * Block.blocksList[targetBlockId].blockHardness; return true; } } }
	 * 
	 * return false; }
	 */
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		super.startExecuting();
		this.breakingTime = 0;
		this.field_75358_j = -1;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {

		double distanceToTarget = this.entity.getDistanceSq((double) this.breakX, (double) this.breakY, (double) this.breakZ);
		return this.breakingTime <= timeToBreak && !this.entity.worldObj.isAirBlock(this.breakX, this.breakY, this.breakZ) && distanceToTarget < 10.0D;
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		super.resetTask();
		this.entity.worldObj.destroyBlockInWorldPartially(this.entity.entityId, this.breakX, this.breakY, this.breakZ, -1);
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		super.updateTask();

		if (this.entity.getRNG().nextInt(20) == 0) {
			this.entity.worldObj.playAuxSFX(1010, this.breakX, this.breakY, this.breakZ, 0);
		}

		++this.breakingTime;
		int i = (int) ((float) this.breakingTime / timeToBreak * 10.0F);

		if (i != this.field_75358_j) {
			this.entity.worldObj.destroyBlockInWorldPartially(this.entity.entityId, this.breakX, this.breakY, this.breakZ, i);
			this.field_75358_j = i;
		}

		if (this.breakingTime == timeToBreak) {
			this.entity.worldObj.setBlockToAir(this.breakX, this.breakY, this.breakZ);
			this.entity.worldObj.playAuxSFX(1012, this.breakX, this.breakY, this.breakZ, 0);
			this.entity.worldObj.playAuxSFX(2001, this.breakX, this.breakY, this.breakZ, this.targetBlockId);
		}
	}
}
