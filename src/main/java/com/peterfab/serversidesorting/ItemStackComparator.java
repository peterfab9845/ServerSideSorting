package com.peterfab.serversidesorting;

import java.util.Comparator;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// TODO ordering without using ID is hard
public class ItemStackComparator implements Comparator<ItemStack> {
    public int compare(ItemStack a, ItemStack b) {
        // return positive -> sort a after b
        int diff = Item.getId(a.getItem()) - Item.getId(b.getItem());
        if (diff == 0) {
            CompoundTag tagA = a.getTag();
            CompoundTag tagB = b.getTag();
            if (tagA != null && tagB != null) {
                // don't really care
                diff = tagA.hashCode() - tagB.hashCode();
            }
            else if (tagA != null) {
                // tagless first
                diff = 1;
            }
            else if (tagB != null) {
                // tagless first
                diff = -1;
            }
            else {
                // larger stack first
                diff = b.getCount() - a.getCount();
                if (diff == 0) {
                    // higher bar first
                    diff = b.getBarWidth() - a.getBarWidth();
                }
            }
        }
        return diff;
    }
}
