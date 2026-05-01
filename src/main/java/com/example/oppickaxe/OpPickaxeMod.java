package com.example.oppickaxe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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

    @Override
    public void onInitialize() {
        CandyBlocks.register();
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(SUPER_OP_PICKAXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(SUPER_OP_PICKAXE));
    }
}
