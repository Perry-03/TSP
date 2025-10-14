import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

public class ClassLoadingAgent {
    private static final Set<String> loadedClasses = new HashSet<>();

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println(">>> ClassLoadingAgent initialized.");

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(Module module, ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (className != null) {
                    loadedClasses.add(className.replace('/', '.'));
                }
                return null;
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            long systemCount = loadedClasses.stream()
                    .filter(name -> name.startsWith("java."))
                    .count();

            long userCount = loadedClasses.size() - systemCount;

            System.out.println("\n=== CLASS LOADING SUMMARY ===");
            System.out.println("System classes loaded: " + systemCount);
            System.out.println("User-defined classes loaded: " + userCount);
            System.out.println("Total classes loaded: " + loadedClasses.size());
            System.out.println("\nLoaded class names:");
            loadedClasses.forEach(System.out::println);
        }));
    }
}
