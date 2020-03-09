package me.jellysquid.mods.lithium.asm;

import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;
import java.util.function.Function;

public class ASMUtil {
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * Matches all of the class fields to their references.
     */
    public static Collection<FieldNode> matchFields(ClassNode classNode, Collection<FieldRef> refs) {
        return matchRefs(classNode.fields, refs,
                (method) -> new FieldRef(classNode.name, method.name, method.desc));
    }

    /**
     * Matches all of the class methods to their references.
     */
    public static Collection<MethodNode> matchMethods(ClassNode classNode, Collection<MethodRef> refs) {
        return matchRefs(classNode.methods, refs,
                (method) -> new MethodRef(method.name, method.desc));
    }

    /**
     * Converts a dot-delimited intermediary name to a forward slash-delimited path notation.
     */
    public static String getPathNotation(String intermediary) {
        return intermediary.replace('.', '/');
    }

    /**
     * Matches a collection of nodes to their reference types. If not all of the references can be matched, the function
     * throws an error.
     *
     * @param nodes The collection of nodes to search through
     * @param refs The references to find in {@param nodes}
     * @param nameFunction The name function responsible for creating comparable references ({@param <T>}) out of the
     *                    node type ({@param <K>})
     * @param <K> The node type
     * @param <T> The reference type
     * @return The collection of located nodes with undefined ordering
     * @throws RuntimeException If not all the references could be mapped to nodes
     */
    private static <K, T> Collection<K> matchRefs(Collection<K> nodes, Collection<T> refs, Function<K, T> nameFunction) {
        final HashSet<T> missing = new HashSet<>(refs);
        final List<K> matched = new ArrayList<>();

        Iterator<K> nodeIterator = nodes.iterator();

        // Early exit if we have found everything
        while (nodeIterator.hasNext() && !missing.isEmpty()) {
            K node = nodeIterator.next();
            T ref = nameFunction.apply(node);

            if (missing.remove(ref)) {
                matched.add(node);
            }
        }

        if (!missing.isEmpty()) {
            throw new RuntimeException("Could not locate: " + ArrayUtils.toString(missing));
        }

        return matched;
    }
}
