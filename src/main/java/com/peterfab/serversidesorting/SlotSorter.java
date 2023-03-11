package com.peterfab.serversidesorting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotSorter {
    Player player;
    List<Slot> sortSlots;
    List<Slot> refillSlots;

    public SlotSorter(Player player) {
        this.player = player;
        this.sortSlots = new ArrayList<>();
        this.refillSlots = new ArrayList<>();
    }

    public void addSortSlot(Slot slot) {
        sortSlots.add(slot);
    }

    public void addRefillSlot(Slot slot) {
        refillSlots.add(slot);
    }

    public void sort() {
        // TODO ordering without using ID is hard
        PriorityQueue<ItemStack> pendingItems = new PriorityQueue<>(new ItemStackComparator());

        // pull items from sorted slots
        for (Slot slot : sortSlots) {
            if (slot.hasItem()) {
                pendingItems.add(slot.remove(slot.getMaxStackSize()));
            }
        }

        // refill other slots in the given order
        for (Slot slot : refillSlots) {
            if (slot.hasItem()) {
                // note, accessing the queue this way isn't ordered
                for (Iterator<ItemStack> iter = pendingItems.iterator(); iter.hasNext(); ) {
                    ItemStack stack = iter.next();
                    slot.safeInsert(stack);
                    if (stack.isEmpty()) {
                        iter.remove();
                    }
                }
            }
        }

        // fill sorted slots in the given order
        ItemStack currentStack = ItemStack.EMPTY;
        for (Slot slot : sortSlots) {
            while (true) {
                if (currentStack.isEmpty()) {
                    currentStack = pendingItems.poll();
                }
                if (currentStack == null) break; // no items left
                slot.safeInsert(currentStack);
                if (!currentStack.isEmpty()) break; // move to next slot
            }
            if (currentStack == null) break; // no items left
        }

        // don't void any extras
        if (pendingItems.size() > 0) {
            for (ItemStack stack : pendingItems) {
                player.drop(stack, false);
            }
        }
    }
}
