package com.thatoneperson.forgeorigin.client.events;

import com.thatoneperson.forgeorigin.ForgeOrigins;

import net.minecraft.tags.FluidTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = ForgeOrigins.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
	
	@SubscribeEvent
	public static void getUnderwaterVisibility(final FogDensity event) {
		if(event.getRenderer().getActiveRenderInfo().getRenderViewEntity().areEyesInFluid(FluidTags.WATER)) {
			event.setDensity(0.0f);
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void waterFloat(final Event event) {
		
	}
}
