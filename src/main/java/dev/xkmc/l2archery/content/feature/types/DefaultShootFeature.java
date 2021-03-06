package dev.xkmc.l2archery.content.feature.types;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.function.Consumer;

public class DefaultShootFeature implements OnShootFeature {

	public static final DefaultShootFeature INSTANCE = new DefaultShootFeature();

	@Override
	public boolean onShoot(Player player, Consumer<Consumer<GenericArrowEntity>> consumer) {
		consumer.accept(entity -> {
			entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F,
					entity.data.power() * entity.data.bow().item().config.speed(), 1.0F);
			if (entity.data.power() == 1.0F) {
				entity.setCritArrow(true);
			}
			ItemStack bow = entity.data.bow().stack();
			int power = bow.getEnchantmentLevel(Enchantments.POWER_ARROWS);
			if (power > 0) {
				entity.setBaseDamage(entity.getBaseDamage() + (double) power * 0.5D + 0.5D);
			}
			int punch = bow.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
			if (punch > 0) {
				entity.setKnockback(punch);
			}
			if (bow.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) {
				entity.setSecondsOnFire(100);
			}
			if (entity.data.no_consume()) {
				entity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
			}
			int knock = entity.getKnockback() + entity.data.bow().item().config.punch() + entity.data.arrow().item().config.punch();
			double damage = entity.getBaseDamage() + entity.data.bow().item().config.damage() + entity.data.arrow().item().config.damage();
			entity.setKnockback(Math.max(0, knock));
			entity.setBaseDamage(Math.max(damage, 0.5));
		});
		return true;
	}
}
