import java.lang.reflect.Method;

public record Edge(Class<?> from, Class<?> to, Method m) {
    
    public Class<?> getFrom() { return from; }
    public Class<?> getTo()   { return to; }


    @Override
    public String toString() { return String.format("%s |> %s --> %s", from, m.getName(), to); }

}
