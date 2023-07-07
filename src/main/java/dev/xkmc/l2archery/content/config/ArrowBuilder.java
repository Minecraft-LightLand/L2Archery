package dev.xkmc.l2archery.content.config;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2archery.content.item.GenericArrowItem;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import net.minecraft.world.item.Item;

@DataGenOnly
public class ArrowBuilder extends BaseStatBuilder<ArrowBuilder, GenericArrowItem, Item> {

	ArrowBuilder(BowArrowStatConfig config, RegistryEntry<GenericArrowItem> arrow) {
		super(config, config.arrow_effects, config.arrow_stats, arrow);
	}

	public ArrowBuilder damage(double val) {
		return putStat(ArcheryRegister.DAMAGE.get(), val);
	}

	public ArrowBuilder punch(double val) {
		return putStat(ArcheryRegister.PUNCH.get(), val);
	}

}