package test.exe;
import nested.exe.*;

public class ProxyTest {
    public static void main(String... args) {
        NestedCalls c = new DeepHandler();

        System.out.println("a() :- " + c.a());
        System.out.println("b() :- " + c.b(c.a()));
        System.out.println("c() :- " + c.c(c.b(c.a())));
    }
}