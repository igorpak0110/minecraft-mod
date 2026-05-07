package com.example.oppickaxe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
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

    public static final Item FART_CLOUD = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "fart_cloud"),
            new FartCloudItem(new Item.Settings().maxCount(16))
    );

    public static final Item UNICORN_POOP = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "unicorn_poop"),
            new Item(new Item.Settings().maxCount(64)
                    .food(new FoodComponent.Builder().nutrition(4).saturationModifier(0.5f).alwaysEdible().build()))
    );

    public static final EntityType<UnicornEntity> UNICORN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, "unicorn"),
            EntityType.Builder.<UnicornEntity>create(UnicornEntity::new, SpawnGroup.CREATURE)
                    .dimensions(1.4f, 1.6f)
                    .build()
    );

    @Override
    public void onInitialize() {
        CandyBlocks.register();
        CandyFeatures.register();
        FabricDefaultAttributeRegistry.register(UNICORN, UnicornEntity.createUnicornAttributes());
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(SUPER_OP_PICKAXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(SUPER_OP_PICKAXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(POOPY_FARTY));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(UNICORN_POOP));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(FART_CLOUD));
    }
}
