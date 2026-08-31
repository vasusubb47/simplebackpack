package com.agent47.simple_backpack.util;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public final class ResourceHandlerUtils {
    private ResourceHandlerUtils() {}

    public static <T extends Resource> void copyAllSlots(ResourceHandler<T> from, ResourceHandler<T> to) {
        try (Transaction tx = Transaction.openRoot()) {
            int slots = Math.min(from.size(), to.size());
            for (int i = 0; i < slots; i++) {
                T existing = to.getResource(i);
                int existingAmount = to.getAmountAsInt(i);
                if (existingAmount > 0 && !existing.isEmpty()) {
                    to.extract(i, existing, existingAmount, tx);
                }
                T resource = from.getResource(i);
                int amount = from.getAmountAsInt(i);
                if (amount > 0 && !resource.isEmpty()) {
                    to.insert(i, resource, amount, tx);
                }
            }
            tx.commit();
        }
    }
}