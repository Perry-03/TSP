import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;

public class AnalyzeApp {
    private static Graph graph = new Graph();

    public static void main(String... args) {
        try {

            Class<?>[] clazz = Arrays.stream(args)
                .map(x -> {
                    try {
                        return Class.forName(x);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e); // o gestisci come vuoi
                    }
                })
                .toArray(Class<?>[]::new);
                      

                for (Class<?> cls : clazz) {
                    Method[] ms = cls.getDeclaredMethods();
                    for (Method m : ms) {
                        Annotation[] anns = m.getAnnotations();
                        System.out.println("Method: " + m.getName());
                        for (Annotation a : anns) parseAnnotation(cls, a.toString(), m);
                                         
                    }

                }
               
                
                graph.printGraph();
                     
            } catch (Exception e) { System.out.println("Errore: " + e.getMessage()); }
    }

    private static void parseAnnotation(final Class<?> clazz, final String ann, final Method m) {
        try {
            String regexCall = "@Call\\(className=\"([A-Za-z]+)\",\\s*methodName=\"([A-Za-z0-9_]+)\"\\)";
            Pattern pattern = Pattern.compile(regexCall);
            Matcher matcher = pattern.matcher(ann);

            while (matcher.find()) {
                String className = matcher.group(1);                
                String methodName = matcher.group(2);

               // System.out.println("Prova: " + className);
               // System.out.println("Prova2: " + methodName);

                graph.addEdge(new Edge(clazz, Class.forName(className), m));
            }

        } catch (ClassNotFoundException e) { System.out.println("classe: " + clazz); }
    }
}

