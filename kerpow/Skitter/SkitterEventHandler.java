package kerpow.Skitter;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class SkitterEventHandler {

	private int tickCount = 0;
	private final int SPAWN_RANGE_SAFE_ZONE = 20;
	private final int SPAWN_RANGE = 40;

	// In your TutEventHandler class - the name of the method doesn't matter
	// Only the Event type parameter is what's important (see below for
	// explanations of some types)
	@ForgeSubscribe
	public void onLivingUpdateEvent(LivingUpdateEvent event) {
		// This event has an Entity variable, access it like this:
		// event.entity;

		// don't do anything during the day
		
		return;
		/*
		if (event.entity.worldObj.isDaytime())
			return;

		tickCount++;
		if (tickCount > 20 * 5) {
			tickCount = 0;
			// do something to player every update tick:
			if (event.entity instanceof EntityPlayer && !event.entity.worldObj.isRemote) {

				
				//keep the number to spawn reasonable
				//if(event.entity.worldObj.getEntitiesWithinAABB(EntitySiegeZombie.class, event.entity.boundingBox.expand(40,5,40)).size() < 10)
				//	return;
				
				double x = (double) event.entity.posX
						+ (event.entity.worldObj.rand.nextDouble() - event.entity.worldObj.rand.nextDouble())
						* (double) this.SPAWN_RANGE;

				double z = (double) event.entity.posZ
						+ (event.entity.worldObj.rand.nextDouble() - event.entity.worldObj.rand.nextDouble())
						* (double) this.SPAWN_RANGE;

				EntitySiegeZombie zomie = new EntitySiegeZombie(event.entity.worldObj);
				zomie.setLocationAndAngles(x, y, z, event.entity.rotationYaw, 0.0F);
				
				if (event.entity.worldObj.getLightBrightness((int) x, (int) y, (int) z) < .5F
						&& event.entity.getDistance(x, y, z) > SPAWN_RANGE_SAFE_ZONE) {

					zomie.getCanSpawnHere()
					event.entity.worldObj.spawnEntityInWorld(zomie);
				}

			}
		}
		*/
	}

}
