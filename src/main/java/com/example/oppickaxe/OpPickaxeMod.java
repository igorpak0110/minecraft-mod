package com.example.oppickaxe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class OpPickaxeMod implements ModInitializer {
    public static final String MOD_ID = "oppickaxe";

    public static final Item SUPER_OP_PICKAXE = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "super_op_pickaxe"),
            new SuperOpPickaxeItem(new Item.Settings().maxCount(1).fireproof())
    );

    public static final Item POOPY_FARTY = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "poopy_farty"),
            new PoopyFartyItem(new Item.Settings().maxCount(16)
                    .food(new FoodComponent.Builder().nutrition(2).saturationModifier(0.1f).alwaysEdible().build()))
    );

    @Override
    public void onInitialize() {
        CandyBlocks.register();
        CandyFeatures.register();
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(SUPER_OP_PICKAXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(SUPER_OP_PICKAXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(POOPY_FARTY));
    }
}
