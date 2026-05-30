package su.nightexpress.nightcore.ui.inventory.condition;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Deprecated
public class ConditionRegistry {

    private final Map<String, ItemStateCondition> conditionMap;

    public ConditionRegistry() {
        this.conditionMap = new HashMap<>();
    }

    public void register(@NonNull String name, @NonNull ItemStateCondition condition) {
        String id = LowerCase.INTERNAL.apply(name);
        if (this.conditionMap.containsKey(id)) {
            // TODO Warn or exception
            return;
        }
        this.conditionMap.put(id, condition);
    }

    @Nullable
    public ItemStateCondition getById(@NonNull String id) {
        return this.conditionMap.get(LowerCase.INTERNAL.apply(id));
    }

    @NonNull
    public Optional<ItemStateCondition> byId(@NonNull String id) {
        return Optional.ofNullable(this.getById(id));
    }
}
