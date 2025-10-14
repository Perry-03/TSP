public class TestProxy {
    public static void main(String... args) {
        NestedCalls c = new DeeplyHandler();

       System.out.println("a() :- " + c.a());
       System.out.println("b() :- " + c.b(c.a()));
       System.out.println("c() :- " + c.c(c.b(c.a())));
    }
}
