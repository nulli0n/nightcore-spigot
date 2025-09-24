package su.nightexpress.nightcore.util.nbt;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Reflex;
import su.nightexpress.nightcore.util.Version;

import java.lang.reflect.Method;

public class DataFixerUtil {

    private static final Class<?> CLS_DATA_FIXERS = Reflex.safeClass("net.minecraft.util.datafix", "DataFixers", "DataConverterRegistry");
    private static final Class<?> CLS_REFERENCES  = Reflex.safeClass("net.minecraft.util.datafix.fixes", "References", "DataConverterTypes");

    private static final Method GET_DATA_FIXER = Reflex.safeMethod(CLS_DATA_FIXERS, "getDataFixer", "a");

    private static final DataFixer         DATA_FIXER           = (DataFixer) Reflex.invokeMethod(GET_DATA_FIXER, CLS_DATA_FIXERS);
    private static final DSL.TypeReference ITEM_STACK_REFERENCE = (DSL.TypeReference) Reflex.getFieldValue(CLS_REFERENCES, "ITEM_STACK", Version.isAtLeast(Version.MC_1_21_6) ? "u" : "t");

    @SuppressWarnings({"rawtypes", "unchecked"})
    @NotNull
    public static Object updateItemStack(@NotNull Object compoundTag, int sourceVersion) {
        if (DATA_FIXER == null) throw new IllegalStateException("DataFixer is null!");

        int targetVersion = Version.getCurrent().getDataVersion();
        if (targetVersion <= 0) return compoundTag;
        if (sourceVersion > targetVersion || sourceVersion <= 0) return compoundTag;

        // Not necessary, but they used it for a reason probably.
        /*if (Version.isPaper()) {
            Class<?> mcDataTypeClass = Reflex.getClass("ca.spottedleaf.dataconverter.minecraft.datatypes", "MCDataType");
            Class<?> mcDataConvertedClass = Reflex.getClass("ca.spottedleaf.dataconverter.minecraft", "MCDataConverter");
            Class<?> mcTypeRegistryClass = Reflex.getClass("ca.spottedleaf.dataconverter.minecraft.datatypes", "MCTypeRegistry");

            Object itemStackRegistry = Reflex.getFieldValue(mcTypeRegistryClass, "ITEM_STACK");

            Method convert = Reflex.getMethod(mcDataConvertedClass, "convertTag", mcDataTypeClass, CLS_COMPOUND_TAG, Integer.TYPE, Integer.TYPE);

            return Reflex.invokeMethod(convert, null, itemStackRegistry, compoundTag, sourceVersion, targetVersion);
        }*/

        Dynamic<?> dynamic = new Dynamic<>((DynamicOps) NbtOps.INSTANCE, compoundTag);
        return DATA_FIXER.update(ITEM_STACK_REFERENCE, dynamic, sourceVersion, targetVersion).getValue();
    }
}
