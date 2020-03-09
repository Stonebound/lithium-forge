package me.jellysquid.mods.lithium.asm.modlauncher;

import com.google.common.collect.Lists;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import me.jellysquid.mods.lithium.common.config.LithiumConfig;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class LithiumTransformationService implements ITransformationService {
    private LithiumConfig config;

    @Nonnull
    @Override
    public String name() {
        return "lithium";
    }

    @Override
    public void initialize(IEnvironment environment) {
        this.config = LithiumConfig.instance();
    }

    @Override
    public void beginScanning(IEnvironment environment) {

    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {

    }

    @Nonnull
    @Override
    public List<ITransformer> transformers() {
        return Lists.newArrayList(new LithiumTransformer(this.config));
    }
}
