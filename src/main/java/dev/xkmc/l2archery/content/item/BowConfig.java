package dev.xkmc.l2archery.content.item;

import dev.xkmc.l2archery.content.config.BowArrowStatConfig;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.stats.BowArrowStatType;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public record BowConfig(ResourceLocation id, int rank, List<BowArrowFeature> feature) implements IBowConfig {

	private double getValue(BowArrowStatType type) {
		var map = BowArrowStatConfig.get().bow_stats.get(id);
		if (map == null) return type.getDefault();
		return map.getOrDefault(type, type.getDefault());
	}

	public List<MobEffectInstance> getEffects() {
		var map = BowArrowStatConfig.get().bow_effects.get(id);
		if (map == null) return List.of();
		return map.entrySet().stream().map(e -> new MobEffectInstance(e.getKey(), e.getValue().duration(), e.getValue().amplifier())).toList();
	}

	public float damage() {
		return (float) getValue(ArcheryRegister.DAMAGE.get());
	}

	public int punch() {
		return (int) getValue(ArcheryRegister.PUNCH.get());
	}

	public int pull_time() {
		return (int) getValue(ArcheryRegister.PULL_TIME.get());
	}

	public float speed() {
		return (float) getValue(ArcheryRegister.SPEED.get());
	}

	public int fov_time() {
		return (int) getValue(ArcheryRegister.FOV_TIME.get());
	}

	public float fov() {
		return (float) getValue(ArcheryRegister.FOV.get());
	}

}
