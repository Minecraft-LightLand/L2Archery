package dev.xkmc.l2archery.events;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.explosion.BaseExplosion;
import dev.xkmc.l2archery.content.feature.FeatureList;
import dev.xkmc.l2archery.content.feature.core.StatFeature;
import dev.xkmc.l2archery.content.item.BowData;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.content.upgrade.StatHolder;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import dev.xkmc.l2archery.content.upgrade.UpgradeItem;
import dev.xkmc.l2archery.init.registrate.ArcheryEffects;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("unused")
public class GenericEventHandler {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void fov(ComputeFovModifierEvent event) {
		Player player = Proxy.getClientPlayer();
		if (player == null)
			return;
		ItemStack stack = player.getMainHandItem();
		if (stack.getItem() instanceof GenericBowItem bow) {
			float f = event.getFovModifier();
			float i = player.getTicksUsingItem();
			MobEffectInstance ins = player.getEffect(ArcheryEffects.QUICK_PULL.get());
			if (ins != null) {
				i *= 1.5 + 0.5 * ins.getAmplifier();
			}
			BowData data = BowData.of(bow, stack);
			float p = data.getConfig().fov_time();
			event.setNewFovModifier(f * (1 - Math.min(1, i / p) * data.getConfig().fov()));
		}
	}

	@SubscribeEvent
	public static void onDetonate(ExplosionEvent.Detonate event) {
		if (event.getExplosion() instanceof BaseExplosion exp) {
			event.getAffectedEntities().removeIf(e -> !exp.hurtEntity(e));
		}
	}

	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		if (left.getItem() instanceof GenericBowItem bow && right.getItem() instanceof UpgradeItem) {
			Upgrade upgrade = UpgradeItem.getUpgrade(right);
			FeatureList list = bow.getFeatures(left);
			if (upgrade == null) return;
			if (!allowUpgrade(bow, left, upgrade)) return;
			int count = GenericBowItem.getUpgrades(left).size();
			ItemStack result = left.copy();
			GenericBowItem.addUpgrade(result, upgrade);
			GenericBowItem.remakeEnergy(result);
			event.setOutput(result);
			event.setMaterialCost(1);
			event.setCost(8 << (count));

		}
	}

	public static boolean allowUpgrade(GenericBowItem bow, ItemStack bowStack, Upgrade upgrade) {
		FeatureList list = bow.getFeatures(bowStack);
		if (!upgrade.allow(bow)) return false;
		int remain = bow.getUpgradeSlot(bowStack);
		if (remain <= 0) return false;
		if (!list.allow(upgrade.getFeature())) return false;
		if (upgrade.getFeature() instanceof StatFeature stat) {
			var ups = GenericBowItem.getUpgrades(bowStack);
			Set<StatHolder> set = new TreeSet<>();
			for (var up : ups) {
				if (up.getFeature() instanceof StatFeature f) {
					f.addStatHolder(set);
				}
			}
			return stat.addStatHolder(set);
		}
		return true;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onArrowHit(LivingAttackEvent event) {
		if (event.getSource() instanceof IndirectEntityDamageSource ind) {
			if (ind.getDirectEntity() instanceof GenericArrowEntity arrow) {
				arrow.onHurtEntity(ind);
			}
		}
	}

	@SubscribeEvent
	public static void onGrind(GrindstoneEvent.OnplaceItem event) {
		if (event.getTopItem().getItem() instanceof GenericBowItem bow) {
			ItemStack copy = event.getTopItem().copy();
			if (GenericBowItem.getUpgrades(copy).size() > 0) {
				copy.getOrCreateTag().remove(GenericBowItem.KEY);
				GenericBowItem.remakeEnergy(copy);
				event.setOutput(copy);
				event.setXp(0);
			}
		}
	}

}
