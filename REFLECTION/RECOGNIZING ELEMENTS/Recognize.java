import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Recognize {

    private static final String[] classNames = {
        "java.lang.String",
        "java.lang.Integer",
        "java.lang.Math",
        "java.util.ArrayList"
    };


    public static String[] members = {
        "value",          // field in String (private final char[] value)
        "length",         // method in String
        "charAt",         // method in String
        "CASE_INSENSITIVE_ORDER", // static field in String
        "parseInt",       // static method in Integer
        "MAX_VALUE",      // static field in Integer
        "toString",       // method in Object (inherited by many)
        "PI",             // static field in Math
        "sqrt",           // static method in Math
        "E",              // static field in Math
        "add",            // method in ArrayList
        "size",           // method in ArrayList
        //"capacity",     // does not exist (ArrayList has none)
        "clone",          // method from Object (inherited)
        //"stream",         // method in Collection interface
        //"foobar"        // does not exist anywhere
    };


    public static void main(String... args) {
        Class<?>[] sig = Arrays.stream(classNames)
            .map(name -> {
                try {
                    return Class.forName(name);
                } catch (ClassNotFoundException e) { throw new RuntimeException(e); }
            })
            .toArray(Class<?>[] :: new);

        boolean check = allDeclared(sig);

        System.out.println("All the names are declared in those classes? " + check);

    }

    private static boolean allDeclared(Class<?>[] sig) {
        Set<String> toSet = new HashSet<>(Arrays.asList(members));
        
        for (Class<?> c : sig) {
            System.out.println("######################");
            System.out.println("Class: " + c.getName());
            for (Method m : c.getDeclaredMethods()) {
                if (toSet.contains(m.getName())) {
                    String params = Arrays.stream(m.getParameterTypes())
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(", "));
                    String signature = String.format("%s %s %s(%s)",
                        Modifier.toString(m.getModifiers()),
                        m.getReturnType().getSimpleName(),
                        m.getName(),
                        params
                    );
                    System.out.println(signature);
                    toSet.remove(m.getName());
                }
            }
            
            for (Field f : c.getDeclaredFields()) {
                if (toSet.contains(f.getName())) {
                    String fieldSig = String.format("%s %s %s",
                        Modifier.toString(f.getModifiers()),
                        f.getType().getSimpleName(),
                        f.getName()
                    );
                    
                    System.out.println(fieldSig);
                    toSet.remove(f.getName());
                }
            }
        }
        System.out.println("set: " + toSet);

        return toSet.isEmpty();
    }


   


}