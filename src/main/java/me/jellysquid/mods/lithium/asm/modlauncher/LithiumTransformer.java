package me.jellysquid.mods.lithium.asm.modlauncher;

import com.google.common.collect.Sets;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import me.jellysquid.mods.lithium.asm.patches.DevirtualizeBlockPosTransformer;
import me.jellysquid.mods.lithium.common.config.LithiumConfig;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LithiumTransformer implements ITransformer<ClassNode> {
    private final HashMap<String, Consumer<ClassNode>> transformers = new HashMap<>();

    public LithiumTransformer(LithiumConfig config) {
        if (config.general.useBlockPosOptimizations) {
            DevirtualizeBlockPosTransformer.install(this.transformers);
        }
    }

    @Nonnull
    @Override
    public ClassNode transform(ClassNode input, ITransformerVotingContext context) {
        Consumer<ClassNode> transformer = this.transformers.get(input.name);

        if (transformer == null) {
            throw new IllegalArgumentException("Do not know how to transform " + input.name);
        }

        transformer.accept(input);

        return input;
    }

    @Nonnull
    @Override
    public TransformerVoteResult castVote(ITransformerVotingContext context) {
        return TransformerVoteResult.YES;
    }

    @Nonnull
    @Override
    public Set<Target> targets() {
        return this.transformers.keySet()
                .stream()
                .map(Target::targetClass)
                .collect(Collectors.toSet());
    }
}
