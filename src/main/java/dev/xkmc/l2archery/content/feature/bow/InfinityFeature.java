package dev.xkmc.l2archery.content.feature.bow;

import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.feature.FeatureList;
import dev.xkmc.l2archery.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public record InfinityFeature(int level) implements BowArrowFeature {

	public static int getLevel(FeatureList list) {
		int infLevel = 0;
		for (var f : list.all()) {
			if (f instanceof InfinityFeature inf) {
				infLevel = Math.max(inf.level(), infLevel);
			}
		}
		return infLevel;
	}

	@Override
	public void addTooltip(List<MutableComponent> list) {
		list.add(LangData.FEATURE_INFINITY_ADV_BOW.get());
	}

}