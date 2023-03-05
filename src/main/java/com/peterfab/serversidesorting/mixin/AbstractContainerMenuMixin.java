package com.peterfab.serversidesorting.mixin;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.peterfab.serversidesorting.SlotSorter;
import com.peterfab.serversidesorting.Sortable;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Shadow
    NonNullList<Slot> slots;

    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void onDoClick(int slotNum, int buttonNum, ClickType clickType, Player player, CallbackInfo ci) {
        if (!player.getLevel().isClientSide()) {
            if (slotNum > 0 && slotNum < slots.size()) {
                // ClickType.CLONE possibilities (1.19.3 vanilla, Forge changes it):
                // 1. Creative only, use pick block *mouse button* on any slot (from cloning slot stack)
                // 2. Any gamemode, have an item held and use pick block *mouse button* on any slot (from drag-cloning held stack)
                // 3. Any gamemode, hover over an item and use pick block *key* (from cloning slot stack)
                if ((clickType == ClickType.CLONE && !player.getAbilities().instabuild) || (clickType == ClickType.QUICK_MOVE && buttonNum == 1)) {
                    ci.cancel();

                    // get the slots in the appropriate container
                    Container clickedContainer = slots.get(slotNum).container;
                    Map<Integer, Slot> containerSlots = new HashMap<>();
                    for (Slot slot : slots) {
                        if (slot.container == clickedContainer) {
                            containerSlots.put(slot.getContainerSlot(), slot);
                        }
                    }

                    // sort the slots in order defined by container
                    SlotSorter sorter = new SlotSorter(player);
                    for (Integer i : ((Sortable) clickedContainer).getSortSlots()) {
                        if (containerSlots.containsKey(i)) {
                            sorter.addSortSlot(containerSlots.get(i));
                        }
                    }
                    for (Integer i : ((Sortable) clickedContainer).getRefillSlots()) {
                        if (containerSlots.containsKey(i)) {
                            sorter.addRefillSlot(containerSlots.get(i));
                        }
                    }
                    sorter.sort();
                }
            }
        }
    }
}
