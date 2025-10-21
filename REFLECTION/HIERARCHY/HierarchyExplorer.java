import java.lang.reflect.Method;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class HierarchyExplorer {
    public static void main(String... args) {
        try {
            Class<?> clazz = Class.forName(args[0]);
            System.out.println("### Class: " + clazz.getName() + " ###");
            Method[] ms = clazz.getMethods();
            for (int i = 0; i < ms.length; i++) {
                Method m = ms[i];
                final Method superm = clazz.getMethod(m.getName(), m.getParameterTypes());
                System.out.println(printSig(superm));
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static String printSig(Method m) {
        String params = String.join(",",
            Arrays.stream(m.getParameterTypes())
                .map((Class<?> c) -> c.getSimpleName())
                .toArray(String[]::new));

        return String.format(
            "---> from %s signature: %s %s %s(%s)",
            m.getDeclaringClass(), 
            Modifier.toString(m.getModifiers()), 
            m.getReturnType().getSimpleName(), 
            m.getName(), 
            params
        );
    }
}
