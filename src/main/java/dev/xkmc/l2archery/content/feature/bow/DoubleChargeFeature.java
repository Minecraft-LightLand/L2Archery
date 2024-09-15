package dev.xkmc.l2archery.content.feature.bow;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.types.OnShootFeature;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Consumer;

public record DoubleChargeFeature(double rate) implements OnShootFeature {

	@Override
	public boolean onShoot(LivingEntity player, Consumer<Consumer<GenericArrowEntity>> entity) {
		var stack = player.getUseItem();
		if (!(stack.getItem() instanceof GenericBowItem bow)) return false;
		int pullTime = bow.getUseDuration(stack) - player.getUseItemRemainingTicks();
		var power = bow.getRawPowerForTime(player, pullTime);
		if (power < 2) return false;
		entity.accept(e -> e.setBaseDamage(e.getBaseDamage() * rate));
		return true;
	}

	@Override
	public void addTooltip(List<MutableComponent> list) {
		list.add(LangData.FEATURE_DOUBLE_CHARGE.get(Math.round(rate * 100) + "%"));
	}

}
