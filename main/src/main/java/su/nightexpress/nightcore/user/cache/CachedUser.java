package su.nightexpress.nightcore.user.cache;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.user.UserTemplate;
import su.nightexpress.nightcore.util.TimeUtil;

public record CachedUser<U extends UserTemplate>(@NonNull U user, long expireDate) {

    public boolean isExpired() {
        return this.expireDate >= 0L && TimeUtil.isPassed(this.expireDate);
    }
}
