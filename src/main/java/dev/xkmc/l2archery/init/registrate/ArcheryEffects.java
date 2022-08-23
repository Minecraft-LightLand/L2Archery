package dev.xkmc.l2archery.init.registrate;

import dev.xkmc.l2archery.foundation.effect.*;
import dev.xkmc.l2archery.init.L2Archery;
import dev.xkmc.l2foundation.init.L2Foundation;
import dev.xkmc.l2library.repack.registrate.builders.NoConfigBuilder;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ArcheryEffects {

	public static final RegistryEntry<FlameEffect> FLAME = genEffect("flame", () -> new FlameEffect(MobEffectCategory.HARMFUL, 0xFF0000));
	public static final RegistryEntry<IceEffect> ICE = genEffect("frozen", () -> new IceEffect(MobEffectCategory.HARMFUL, 0x7f7fff));
	public static final RegistryEntry<ArmorReduceEffect> ARMOR_REDUCE = genEffect("armor_reduce", () -> new ArmorReduceEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));
	public static final RegistryEntry<MobEffect> RUN_BOW = genEffect("run_bow", () -> new RunBowEffect(MobEffectCategory.BENEFICIAL, 0xffffff));
	public static final RegistryEntry<QuickPullEffect> QUICK_PULL = genEffect("quick_pull", () -> new QuickPullEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));
	public static final RegistryEntry<StoneCageEffect> STONE_CAGE = genEffect("stone_cage", () -> new StoneCageEffect(MobEffectCategory.HARMFUL, 0x000000));

	public static final List<RegistryEntry<? extends Potion>> POTION_LIST = new ArrayList<>();

	public static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup) {
		return L2Archery.REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(L2Archery.REGISTRATE, L2Archery.REGISTRATE, name, cb, ForgeRegistries.Keys.MOB_EFFECTS, sup))
				.lang(MobEffect::getDescriptionId).register();
	}

	private static final List<Runnable> TEMP = new ArrayList<>();

	public static void registerBrewingRecipe() {
		TEMP.forEach(Runnable::run);
	}

	public static void register() {
		regPotion3("flame", FLAME::get, ArcheryItems.SOUL_FLAME::get, 400, 600, 1200, 0, 1);
		regPotion2("frozen", ICE::get, ArcheryItems.HARD_ICE, 3600, 9600);
		regPotion2("stone_cage", STONE_CAGE::get, ArcheryItems.BLACKSTONE_CORE, 1200, 3600);
		regPotion3("run_bow", RUN_BOW, ArcheryItems.CAPTURED_WIND, 600, 1200, 3600, 0, 1);
		regPotion3("quick_pull", QUICK_PULL::get, ArcheryItems.STORM_CORE, 600, 1200, 3600, 0, 1);
		regPotion2("levitation", () -> MobEffects.LEVITATION, ArcheryItems.CAPTURED_BULLET, 200, 600);
		regPotion3("resistance", () -> MobEffects.DAMAGE_RESISTANCE, ArcheryItems.EXPLOSION_SHARD, 400, 600, 1200, 1, 2);
	}

	private static <T extends Potion> RegistryEntry<T> genPotion(String name, NonNullSupplier<T> sup) {
		RegistryEntry<T> ans = L2Foundation.REGISTRATE.entry(name, (cb) -> new NoConfigBuilder<>(L2Archery.REGISTRATE, L2Archery.REGISTRATE, name, cb, ForgeRegistries.Keys.POTIONS, sup)).register();
		POTION_LIST.add(ans);
		return ans;
	}

	private static void regPotion2(String id, Supplier<MobEffect> sup, Supplier<Item> item, int dur, int durLong) {
		var potion = genPotion(id, () -> new Potion(new MobEffectInstance(sup.get(), dur)));
		var longPotion = genPotion("long_" + id, () -> new Potion(new MobEffectInstance(sup.get(), durLong)));
		TEMP.add(() -> {
			PotionBrewing.addMix(Potions.AWKWARD, item.get(), potion.get());
			PotionBrewing.addMix(potion.get(), Items.REDSTONE, longPotion.get());
		});
	}

	private static void regPotion3(String id, Supplier<MobEffect> sup, Supplier<Item> item, int durStrong, int dur, int durLong, int amp, int ampStrong) {
		var potion = genPotion(id, () -> new Potion(new MobEffectInstance(sup.get(), dur, amp)));
		var longPotion = genPotion("long_" + id, () -> new Potion(new MobEffectInstance(sup.get(), durLong, amp)));
		var strongPotion = genPotion("strong_" + id, () -> new Potion(new MobEffectInstance(sup.get(), durStrong, ampStrong)));
		TEMP.add(() -> {
			PotionBrewing.addMix(Potions.AWKWARD, item.get(), potion.get());
			PotionBrewing.addMix(potion.get(), Items.REDSTONE, longPotion.get());
			PotionBrewing.addMix(potion.get(), Items.GLOWSTONE_DUST, strongPotion.get());
		});
	}

}