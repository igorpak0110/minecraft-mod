package com.example.oppickaxe;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;

public class SuperOpToolMaterial implements ToolMaterial {
    public static final SuperOpToolMaterial INSTANCE = new SuperOpToolMaterial();

    @Override
    public int getDurability() {
        return 2_000_000_000;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 100.0F;
    }

    @Override
    public float getAttackDamage() {
        return 20.0F;
    }

    @Override
    public net.minecraft.registry.tag.TagKey<net.minecraft.block.Block> getInverseTag() {
        return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
    }

    @Override
    public int getEnchantability() {
        return 30;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.NETHERITE_BLOCK);
    }
}
