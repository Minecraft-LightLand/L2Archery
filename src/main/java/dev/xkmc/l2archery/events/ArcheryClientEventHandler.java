package dev.xkmc.l2archery.events;

import dev.xkmc.l2archery.content.item.BowData;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.L2Archery;
import dev.xkmc.l2archery.init.registrate.ArcheryEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = L2Archery.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ArcheryClientEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void fov(ComputeFovModifierEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return;
		ItemStack stack = player.getMainHandItem();
		if (stack.getItem() instanceof GenericBowItem bow) {
			float fov = event.getFovModifier();
			float i = player.getTicksUsingItem();
			MobEffectInstance ins = player.getEffect(ArcheryEffects.QUICK_PULL);
			if (ins != null) {
				i *= 1.5f + 0.5f * ins.getAmplifier();
			}
			BowData data = BowData.of(bow, stack);
			float time = data.getConfig().fovTime();
			float factor = 1 - Math.min(1, i / time) * data.getConfig().fov();
			double scale = Minecraft.getInstance().options.fovEffectScale().get();
			event.setNewFovModifier((float) Mth.lerp(scale, 1, fov * factor));
		}
	}

}
