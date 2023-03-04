package com.peterfab.serversidesorting.mixin;

import java.util.Collections;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.peterfab.serversidesorting.IntegerRange;
import com.peterfab.serversidesorting.Sortable;

import net.minecraft.world.Clearable;
import net.minecraft.world.Container;

@Mixin(Container.class)
public interface ContainerMixin extends Clearable, Sortable {
    @Shadow
    int getContainerSize();

    @Override
    default Iterable<Integer> getSortSlots() {
        return new IntegerRange(0, getContainerSize());
    }

    @Override
    default Iterable<Integer> getRefillSlots() {
        return Collections.emptyList();
    }
}
