
public class Main {
    public static void main(String... args) {
        NestedCalls c = new NestedCalls();

       System.out.println("a() :- " + c.a());
       System.out.println("b() :- " + c.b(c.a()));
       System.out.println("c() :- " + c.c(c.b(c.a())));
    }
}
