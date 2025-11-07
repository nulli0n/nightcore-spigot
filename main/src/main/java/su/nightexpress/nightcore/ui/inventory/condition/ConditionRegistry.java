package su.nightexpress.nightcore.ui.inventory.condition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class ConditionRegistry {

    private final Map<String, ItemStateCondition> conditionMap;

    public ConditionRegistry() {
        this.conditionMap = new HashMap<>();
    }

    public void register(@NotNull String name, @NotNull ItemStateCondition condition) {
        String id = LowerCase.INTERNAL.apply(name);
        if (this.conditionMap.containsKey(id)) {
            // TODO Warn or exception
            return;
        }
        this.conditionMap.put(id, condition);
    }

    @Nullable
    public ItemStateCondition getById(@NotNull String id) {
        return this.conditionMap.get(LowerCase.INTERNAL.apply(id));
    }

    @NotNull
    public Optional<ItemStateCondition> byId(@NotNull String id) {
        return Optional.ofNullable(this.getById(id));
    }
}
