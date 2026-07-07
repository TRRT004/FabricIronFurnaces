package ironfurnaces.augments;

import ironfurnaces.items.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import ironfurnaces.init.Reference;

public class IronFurnacesAugments implements ModInitializer {
    public static final String MOD_ID = "ironfurnaces_augments";

    public static final Item BLASTING_AUGMENT = new ItemAugmentBlasting();
    public static final Item SMOKING_AUGMENT = new ItemAugmentSmoking();
    public static final Item SPEED_AUGMENT = new ItemAugmentSpeed();
    public static final Item FUEL_AUGMENT = new ItemAugmentFuel();

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "augment_blasting"), BLASTING_AUGMENT);
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "augment_smoking"), SMOKING_AUGMENT);
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "augment_speed"), SPEED_AUGMENT);
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "augment_fuel"), FUEL_AUGMENT);

        // Add to the base mod's Creative Tab
        ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(
            net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(Reference.MOD_ID, "general")
        );
        CreativeModeTabEvents.modifyOutputEvent(tabKey).register(content -> {
            content.accept(BLASTING_AUGMENT);
            content.accept(SMOKING_AUGMENT);
            content.accept(SPEED_AUGMENT);
            content.accept(FUEL_AUGMENT);
        });
    }
}
