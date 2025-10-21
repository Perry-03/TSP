public class MainLoader {
    public static void main(String... args) {
        try {
            HelloClassLoader cl = new HelloClassLoader("/Users/Luca/Documents/UNI/MAGISTRALE/TSP/CLASS\\ LOADING/Base\\ ClassLoader");


            Class<?> helloClass = cl.loadClass("Hello");

            Object helloObj = helloClass.getDeclaredConstructor().newInstance();
            helloClass.getMethod("sayHello").invoke(helloObj);
        } catch (Exception e) { e.printStackTrace(); }
    }
}