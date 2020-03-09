package me.jellysquid.mods.lithium.asm;

import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.BiFunction;

public class NameUtil {
    // Used whenever there is no mapping function for the current environment (i.e. in a developer workspace)
    private static final BiFunction<INameMappingService.Domain, String, String> DEV_NAME_FUNC = (domain, name) -> name;

    private static final BiFunction<INameMappingService.Domain, String, String> SRG_NAME_FUNC = FMLLoader.getNameFunction("srg")
            .orElse(DEV_NAME_FUNC);

    public static String deobfName(INameMappingService.Domain domain, String name) {
        return SRG_NAME_FUNC.apply(domain, name);
    }
}
