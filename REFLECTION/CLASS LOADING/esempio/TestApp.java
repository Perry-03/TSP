import java.util.ArrayList;
import java.util.HashMap;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("Running TestApp...");

        ArrayList<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");

        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "Java");
        map.put(2, "Agent");

        CustomClass custom = new CustomClass();
        custom.sayHello();

        System.out.println("Done.");
    }
}

class CustomClass {
    void sayHello() {
        System.out.println("Hello from CustomClass!");
    }
}
