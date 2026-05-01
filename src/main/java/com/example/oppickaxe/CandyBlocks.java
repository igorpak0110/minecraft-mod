package com.example.oppickaxe;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CandyBlocks {

    public static final Block CANDY_GRASS = Registry.register(
            Registries.BLOCK,
            Identifier.of(OpPickaxeMod.MOD_ID, "candy_grass"),
            new Block(AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK).mapColor(MapColor.PINK))
    );

    public static final Block SUGAR_SAND = Registry.register(
            Registries.BLOCK,
            Identifier.of(OpPickaxeMod.MOD_ID, "sugar_sand"),
            new Block(AbstractBlock.Settings.copy(Blocks.SAND).mapColor(MapColor.WHITE))
    );

    static {
        Registry.register(
                Registries.ITEM,
                Identifier.of(OpPickaxeMod.MOD_ID, "candy_grass"),
                new BlockItem(CANDY_GRASS, new Item.Settings())
        );
        Registry.register(
                Registries.ITEM,
                Identifier.of(OpPickaxeMod.MOD_ID, "sugar_sand"),
                new BlockItem(SUGAR_SAND, new Item.Settings())
        );
    }

    public static void register() {
        // Blocks and items are registered via static initializer above.
    }
}
