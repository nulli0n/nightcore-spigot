package su.nightexpress.nightcore.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EntityUtil {

    public static final EquipmentSlot[] EQUIPMENT_SLOTS = {
        EquipmentSlot.HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public static int nextEntityId() {
        return Engine.software().nextEntityId();
    }


    public static void setCustomName(@NotNull Entity entity, @NotNull String name) {
        Engine.software().setCustomName(entity, NightMessage.parse(name));
    }

    @NotNull
    public static String getNameSerialized(@NotNull Entity entity) {
        String customName = Engine.software().getEntityName(entity);
        if (customName != null) return customName;

        return LangUtil.getSerializedName(entity.getType());
    }



    @Deprecated
    public static double getAttribute(@NotNull LivingEntity entity, @NotNull Attribute attribute) {
        return getAttributeValue(entity, attribute);
    }

    @Deprecated
    public static double getAttributeBase(@NotNull LivingEntity entity, @NotNull Attribute attribute) {
        return getAttributeBaseValue(entity, attribute);
    }

    public static double getAttributeValue(@NotNull LivingEntity entity, @NotNull Attribute attribute) {
        AttributeInstance instance = entity.getAttribute(attribute);
        return instance == null ? 0D : instance.getValue();
    }

    public static double getAttributeBaseValue(@NotNull LivingEntity entity, @NotNull Attribute attribute) {
        AttributeInstance instance = entity.getAttribute(attribute);
        return instance == null ? 0D : instance.getBaseValue();
    }

    public static void modifyAttribute(@NotNull LivingEntity entity, @NotNull Attribute attribute, @NotNull Consumer<AttributeInstance> consumer) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) return;

        consumer.accept(instance);
    }

    public static double getMaxHealth(@NotNull LivingEntity entity) {
        return getAttributeValue(entity, Attribute.MAX_HEALTH);
    }

    public static void addHealth(@NotNull LivingEntity entity, double amount) {
        setHealth(entity, entity.getHealth() + Math.abs(amount));
    }

    public static void removeHealth(@NotNull LivingEntity entity, double amount) {
        setHealth(entity, entity.getHealth() - Math.abs(amount));
    }

    public static void setHealth(@NotNull LivingEntity entity, double value) {
        double maxHealth = getMaxHealth(entity);
        double health = Math.clamp(value, 0, maxHealth);

        entity.setHealth(health);
    }

    @Nullable
    public static ItemStack getItemInSlot(@NotNull LivingEntity entity, @NotNull EquipmentSlot slot) {
        if (entity instanceof Player player) {
            return player.getInventory().getItem(slot);
        }

        EntityEquipment equipment = entity.getEquipment();
        return equipment == null ? null : equipment.getItem(slot);
    }

    @NotNull
    public static Map<EquipmentSlot, ItemStack> getEquippedItems(@NotNull LivingEntity entity) {
        return getEquippedItems(entity, EQUIPMENT_SLOTS);
    }

    @NotNull
    public static Map<EquipmentSlot, ItemStack> getEquippedItems(@NotNull LivingEntity entity, @NotNull EquipmentSlot... slots) {
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) return Collections.emptyMap();

        Map<EquipmentSlot, ItemStack> map = new HashMap<>();
        for (EquipmentSlot slot : slots) {
            if (slot.name().equalsIgnoreCase("BODY")) continue; // from 1.20.6

            map.put(slot, equipment.getItem(slot));
        }
        return map;
    }

    @NotNull
    public static Map<EquipmentSlot, ItemStack> getEquippedHands(@NotNull LivingEntity entity) {
        return getEquippedItems(entity, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
    }

    @NotNull
    public static Map<EquipmentSlot, ItemStack> getEquippedArmor(@NotNull LivingEntity entity) {
        return getEquippedItems(entity, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD);
    }

    @Nullable
    public static BlockFace getDirection(@NotNull Entity entity) {
        float yaw = Math.round(entity.getLocation().getYaw() / 90F);

        if ((yaw == -4.0F) || (yaw == 0.0F) || (yaw == 4.0F)) {
            return BlockFace.SOUTH;
        }
        if ((yaw == -1.0F) || (yaw == 3.0F)) {
            return BlockFace.EAST;
        }
        if ((yaw == -2.0F) || (yaw == 2.0F)) {
            return BlockFace.NORTH;
        }
        if ((yaw == -3.0F) || (yaw == 1.0F)) {
            return BlockFace.WEST;
        }
        return null;
    }
}
