package kerpow.Skitter.AI;

import kerpow.Skitter.Skitter;
import kerpow.Skitter.Entities.EntitySkitterWarrior;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.AxisAlignedBB;

public class EntityAISiegeWalls extends EntityAIBase {
	private int breakingTime;
	/*
	 * private int lastX = 0; private int lastY = 0; private int lastZ = 0;
	 */
	private int targetX = 0;
	private int targetY = 0;
	private int targetZ = 0;

	private int targetBlockId = 0;
	private float timeToBreak = 240;

	protected EntitySkitterWarrior entity;

	private int field_75358_j = -1;

	public EntityAISiegeWalls(EntityLiving par1EntityLiving) {
		this.entity = (EntitySkitterWarrior) par1EntityLiving;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {

		// if i haven't moved much
		if (this.entity.getAttackTarget() != null && this.entity.worldObj.rand.nextFloat() < .1 ) {

			if (Math.abs( this.entity.posY - this.entity.getAttackTarget().posY ) < 2)
			{
				return this.siegeWall();
			}
		}

		return false;
	}

	private boolean siegeWall() {

		return setBreakTargetInArea((int) this.entity.posX - 2,
				(int) this.entity.posY, (int) this.entity.posZ - 2,
				(int) this.entity.posX + 2, (int) this.entity.posY + 1,
				(int) this.entity.posZ + 2);

	}

	private boolean setBreakTargetInArea(int minX, int minY, int minZ,
			int maxX, int maxY, int maxZ) {

		for (int x = minX; x < maxX; x++)
			for (int y = minY; y < maxY; y++)
				for (int z = minZ; z < maxZ; z++) {
					if (!this.entity.worldObj.isAirBlock(x, y, z)) {
						targetX = x;
						targetY = y;
						targetZ = z;
						targetBlockId = this.entity.worldObj
								.getBlockId(x, y, z);
						timeToBreak = 120 * Block.blocksList[targetBlockId].blockHardness;

						return true;
					}
				}

		return false;

	}

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

		double distanceToTarget = this.entity.getDistanceSq(
				(double) this.targetX, (double) this.targetY,
				(double) this.targetZ);
		return this.breakingTime <= timeToBreak
				&& !this.entity.worldObj.isAirBlock(this.targetX, this.targetY,
						this.targetZ) && distanceToTarget < 4.0D;
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		super.resetTask();
		this.entity.worldObj.destroyBlockInWorldPartially(this.entity.entityId,
				this.targetX, this.targetY, this.targetZ, -1);
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		super.updateTask();

		if (this.entity.getRNG().nextInt(20) == 0) {
			this.entity.worldObj.playAuxSFX(1010, this.targetX, this.targetY,
					this.targetZ, 0);
		}

		++this.breakingTime;
		int i = (int) ((float) this.breakingTime / timeToBreak * 10.0F);

		if (i != this.field_75358_j) {
			this.entity.worldObj.destroyBlockInWorldPartially(
					this.entity.entityId, this.targetX, this.targetY,
					this.targetZ, i);
			this.field_75358_j = i;
		}

		if (this.breakingTime == timeToBreak) {
			this.entity.worldObj.setBlockToAir(this.targetX, this.targetY,
					this.targetZ);
			this.entity.worldObj.playAuxSFX(1012, this.targetX, this.targetY,
					this.targetZ, 0);
			this.entity.worldObj.playAuxSFX(2001, this.targetX, this.targetY,
					this.targetZ, this.targetBlockId);
		}
	}
}
