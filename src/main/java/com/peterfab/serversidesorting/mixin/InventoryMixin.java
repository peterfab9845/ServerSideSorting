package com.peterfab.serversidesorting.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.peterfab.serversidesorting.IntegerRange;
import com.peterfab.serversidesorting.Sortable;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

@Mixin(Inventory.class)
public abstract class InventoryMixin implements Container, Nameable, Sortable {
    @Shadow @Final
    public NonNullList<ItemStack> items;
    @Shadow
    public int selected;
    @Shadow
    public static int getSelectionSize() {
        throw new UnsupportedOperationException("Mixin function should never be called");
    }

    @Override
    public Iterable<Integer> getSortSlots() {
        return new IntegerRange(getSelectionSize(), items.size());
    }

    @Override
    public Iterable<Integer> getRefillSlots() {
        List<Integer> refillSlots = new ArrayList<>();
        refillSlots.add(selected);
        refillSlots.add(Inventory.SLOT_OFFHAND);
        for (int i = 0; i < getSelectionSize(); i++) {
            if (i != selected) {
                refillSlots.add(i);
            }
        }
        return refillSlots;
    }
}
