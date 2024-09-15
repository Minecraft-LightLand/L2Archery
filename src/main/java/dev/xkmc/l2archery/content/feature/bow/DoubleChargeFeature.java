package dev.xkmc.l2archery.content.feature.bow;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.types.OnPullFeature;
import dev.xkmc.l2archery.content.feature.types.OnShootFeature;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public record DoubleChargeFeature(double rate) implements OnShootFeature, OnPullFeature {

	@Override
	public boolean onShoot(LivingEntity player, Consumer<Consumer<GenericArrowEntity>> entity) {
		var stack = player.getUseItem();
		if (!(stack.getItem() instanceof GenericBowItem bow)) return true;
		int pullTime = bow.getUseDuration(stack) - player.getUseItemRemainingTicks();
		var power = bow.getRawPowerForTime(player, pullTime);
		if (power < 2) return true;
		entity.accept(e -> e.setBaseDamage(e.getBaseDamage() * rate));
		return true;
	}

	@Override
	public void tickAim(LivingEntity player, GenericItemStack<GenericBowItem> bow) {
		int pullTime = bow.item().getUseDuration(bow.stack()) - player.getUseItemRemainingTicks();
		float p0 = bow.item().getRawPowerForTime(player, pullTime - 1);
		float p1 = bow.item().getRawPowerForTime(player, pullTime);
		if (p0 < 2 && p1 >= 2) {
			player.playSound(SoundEvents.AMETHYST_BLOCK_BREAK);
		}
	}

	@Override
	public void addTooltip(List<MutableComponent> list) {
		list.add(LangData.FEATURE_DOUBLE_CHARGE.get(Math.round(rate * 100) + "%"));
	}

}
