package com.github.quillmc.tinymcp.field;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FieldCache {
    protected final Map<String, Map<String, Field>> cache = new HashMap<>();
    private final FileSystem fs;

    public FieldCache(Path jar) throws IOException {
        fs = FileSystems.newFileSystem(jar);
    }

    private CompletableFuture<Map<String, Field>> loadClass(String path) throws IOException {
        ClassReader reader = new ClassReader(Files.newInputStream(fs.getPath(!path.endsWith(".class") ? path + ".class" : path)));
        FieldVisitor visitor = new FieldVisitor();
        CompletableFuture.runAsync(() -> {
            reader.accept(visitor, 0);
        });
        return visitor.future();
    }

    public Optional<Field> getField(String path, String fieldName) {
        if (cache.containsKey(path)) return Optional.ofNullable(cache.get(path).get(fieldName));
        else { // not in cache, load class
            try {
                loadClass(path).whenComplete((map, throwable) -> {
                    cache.put(path, map);
                }).join();
                return getField(path, fieldName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    class FieldVisitor extends ClassVisitor {
        private final HashMap<String, Field> fields;
        private final CompletableFuture<Map<String, Field>> future;
        protected FieldVisitor() {
            super(Opcodes.ASM4);
            fields = new HashMap<>();
            this.future = new CompletableFuture<>();
        }

        @Override
        public org.objectweb.asm.FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            Field f = new Field(name, descriptor);
            fields.put(name, f);
            return super.visitField(access, name, descriptor, signature, value);
        }

        public CompletableFuture<Map<String, Field>> future() {
            return future;
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            future.complete(fields);
        }
    }
}


