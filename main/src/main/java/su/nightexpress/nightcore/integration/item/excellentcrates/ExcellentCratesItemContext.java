package su.nightexpress.nightcore.integration.item.excellentcrates;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public record ExcellentCratesItemContext(ExcellentCratesItemType type, Identifier itemId) {

}
