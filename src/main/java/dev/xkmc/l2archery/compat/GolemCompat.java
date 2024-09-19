package dev.xkmc.l2archery.compat;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.modulargolems.events.event.GolemBowAttackEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

public class GolemCompat {

	public static void register() {
		NeoForge.EVENT_BUS.register(GolemCompat.class);
	}

	@SubscribeEvent
	public static void onEquip(GolemBowAttackEvent event) {
		if (event.getArrow() instanceof GenericArrowEntity e) {
			event.setParams(e.data.bow().getConfig().speed(), e.features.flight().gravity);
		}
	}

}
