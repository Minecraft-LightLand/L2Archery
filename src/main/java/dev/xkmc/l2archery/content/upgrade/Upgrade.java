package dev.xkmc.l2archery.content.upgrade;

import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public class Upgrade extends NamedEntry<Upgrade> {

	private final Lazy<BowArrowFeature> feature;

	public Upgrade(Supplier<BowArrowFeature> feature) {
		super(ArcheryRegister.UPGRADE);
		this.feature = Lazy.of(feature);
	}

	public BowArrowFeature getFeature() {
		return feature.get();
	}

	public boolean allow(GenericBowItem bow) {
		return feature.get().allow(bow.config);
	}
}
