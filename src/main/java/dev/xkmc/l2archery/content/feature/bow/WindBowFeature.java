package dev.xkmc.l2archery.content.feature.bow;

import dev.xkmc.l2archery.content.feature.types.OnPullFeature;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class WindBowFeature implements OnPullFeature {

	@Override
	public void onPull(Player player, GenericItemStack<GenericBowItem> bow) {
		if (player instanceof ServerPlayer) {
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
		}
	}

	@Override
	public void tickAim(Player player, GenericItemStack<GenericBowItem> bow) {

	}

	@Override
	public void stopAim(Player player, GenericItemStack<GenericBowItem> bow) {

	}

}
