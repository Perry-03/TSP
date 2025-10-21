import java.lang.reflect.Proxy;
public class Main {
    public static void main(String... args) {
        ITask t = new HeavyTask();

        ITask it = (ITask) Proxy.newProxyInstance(
            t.getClass().getClassLoader(),
            new Class[]{ITask.class},
            new TimeHandler(t)
        );
        it.compute(Integer.MAX_VALUE);
        try { it.sleep(1000); } catch (Exception ignored) { }
   
    }    
}
