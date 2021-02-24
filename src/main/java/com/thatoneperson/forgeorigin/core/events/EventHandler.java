package com.thatoneperson.forgeorigin.core.events;

import java.util.Random;

import com.thatoneperson.forgeorigin.ForgeOrigins;

import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;

@EventBusSubscriber(modid = ForgeOrigins.MOD_ID, bus = Bus.FORGE)
public class EventHandler {

static Random random = new Random();

	@SubscribeEvent
	public static void waterBreathe(final LivingEvent.LivingUpdateEvent event) { // Reverses the process of breathing air on land/water, credits to origin for most of this code
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			if(!player.areEyesInFluid(FluidTags.WATER) && !player.isPotionActive(Effects.WATER_BREATHING) && !player.isPotionActive(Effects.CONDUIT_POWER)) {
                if(!player.getEntityWorld().isRainingAt(player.getPosition())) {
                    int landGain = getNextAirOnLand(player, 0);
                    player.setAir(getNextAirUnderwater(player, player.getAir()) - (landGain / 2));
                    if (player.getAir() <= -20) {
                        player.setAir(0);
                        
                        
                        Vector3d vector3d = player.getMotion();
                        
                        for(int i = 0; i < 8; ++i) {
                            double f = random.nextDouble() - random.nextDouble();
                            double g = random.nextDouble() - random.nextDouble();
                            double h = random.nextDouble() - random.nextDouble();
                            player.world.addParticle(ParticleTypes.BUBBLE, player.getPosX() + f, player.getPosY() + g, player.getPosZ() + h, vector3d.x, vector3d.y, vector3d.z);
                        }

                        player.attackEntityFrom(DamageSource.DRYOUT, 2.0F);
                    }
                } else {
                    int landGain = getNextAirOnLand(player, 0);
                    player.setAir(player.getAir() - landGain);
                }
            } else if(player.getAir() < player.getMaxAir()){
                player.setAir(getNextAirOnLand(player, player.getAir()));
            }
		}
	}
	protected static int getNextAirUnderwater(PlayerEntity player, int air) {
	      int i = EnchantmentHelper.getRespirationModifier(player);
	      return i > 0 && random.nextInt(i + 1) > 0 ? air : air - 1;
	}
	
	protected static int getNextAirOnLand(PlayerEntity player, int currentAir) {
	      return Math.min(currentAir + 8, player.getMaxAir());
	}
	
	@SubscribeEvent
	public static void waterFloat(final LivingEvent.LivingUpdateEvent event) { // I... honestly do not know what this does. I assume it freezes y movement in water when y movement is not too large, but this mostly came directly from origins
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			if(player.isInWater()) {
				Vector3d vec3d = player.getMotion();
				double d = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get()).getValue();
				boolean flag = player.getMotion().y <= 0.0D;
				Vector3d oldReturn = player.func_233626_a_(d, flag, vec3d);
				Vector3d mot = oldReturn;
				if (Math.abs(vec3d.y - d / 16.0D) < 0.025D) {
	                mot = new Vector3d(oldReturn.x, 0, oldReturn.z);
	            }
				player.setMotion(mot);
			}
		}
	}
	public Vector3d func_233626_a_(double p_233626_1_, boolean p_233626_3_, Vector3d p_233626_4_) { // This comes straight from minecraft code. I am not really sure what it does.
         double d0;
         if (p_233626_3_ && Math.abs(p_233626_4_.y - 0.005D) >= 0.003D && Math.abs(p_233626_4_.y - p_233626_1_ / 16.0D) < 0.003D) {
            d0 = -0.003D;
         } else {
            d0 = p_233626_4_.y - p_233626_1_ / 16.0D;
         }

         return new Vector3d(p_233626_4_.x, d0, p_233626_4_.z);
	}
	
	@SubscribeEvent
	public static void increaseSwimSpeed(final LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			player.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(2.0); // Set speed underwater to normal speed
		}
	}
	
	@SubscribeEvent
	public static void underwaterBreaking(final PlayerEvent.BreakSpeed event) {
		PlayerEntity player = event.getPlayer();
		if(player.areEyesInFluid(FluidTags.WATER)) {
			float modifier = event.getOriginalSpeed();
			if(!EnchantmentHelper.hasAquaAffinity(player)) // If they don't have aqua infinity, cancel slowness from water
				modifier *= 5.0F;
			if(!player.isOnGround()) // If they aren't on ground, compensate
				modifier *= 5.0F;
			event.setNewSpeed(modifier);
		}
	}
}
