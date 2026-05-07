package com.example.oppickaxe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class OpPickaxeMod implements ModInitializer {
    public static final String MOD_ID = "oppickaxe";

    /** UUIDs of players currently in fart-launch flight — gets continuous smoke. */
    public static final Set<UUID> LAUNCHED_PLAYERS = new HashSet<>();

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
                    .food(new FoodComponent.Builder()
                            .nutrition(6).saturationModifier(0.8f).alwaysEdible()
                            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 2), 1.0f)
                            .statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 1), 1.0f)
                            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1), 1.0f)
                            .statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0), 1.0f)
                            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0), 1.0f)
                            .build()))
    );

    public static final EntityType<UnicornEntity> UNICORN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, "unicorn"),
            EntityType.Builder.<UnicornEntity>create(UnicornEntity::new, SpawnGroup.CREATURE)
                    .dimensions(1.4f, 1.6f)
                    .build()
    );

    public static final EntityType<FartCloudEntity> FART_CLOUD_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, "fart_cloud_entity"),
            EntityType.Builder.<FartCloudEntity>create(FartCloudEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f, 0.25f)
                    .build()
    );

    public static final Item UNICORN_SPAWN_EGG = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "unicorn_spawn_egg"),
            new SpawnEggItem(UNICORN, 0xFFFFFF, 0xFFD700, new Item.Settings().maxCount(64))
    );

    @Override
    public void onInitialize() {
        CandyBlocks.register();
        CandyFeatures.register();
        FabricDefaultAttributeRegistry.register(UNICORN, UnicornEntity.createUnicornAttributes());

        // Continuous smoke for launched players
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Iterator<UUID> iter = LAUNCHED_PLAYERS.iterator();
            while (iter.hasNext()) {
                UUID uuid = iter.next();
                ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
                if (player == null || player.isOnGround()) {
                    iter.remove();
                    continue;
                }
                // Spawn white smoke particles beneath the player each tick
                for (ServerWorld world : server.getWorlds()) {
                    if (world.getPlayers().contains(player)) {
                        world.spawnParticles(ParticleTypes.LARGE_SMOKE,
                                player.getX(), player.getY(), player.getZ(),
                                3, 0.25, 0.1, 0.25, 0.02);
                        break;
                    }
                }
            }
        });

        // Tools tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(SUPER_OP_PICKAXE));

        // Combat tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(SUPER_OP_PICKAXE);
            entries.add(FART_CLOUD);
        });

        // Food tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(POOPY_FARTY);
            entries.add(UNICORN_POOP);
        });

        // Building blocks tab for candy blocks
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(CandyBlocks.CANDY_GRASS.asItem());
            entries.add(CandyBlocks.SUGAR_SAND.asItem());
        });

        // Spawn eggs tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries ->
                entries.add(UNICORN_SPAWN_EGG));
    }
}
