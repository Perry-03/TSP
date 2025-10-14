public class NestedCalls implements NestedCallsI {
    private int i = 0;

    public int a() { return b(i++); }
    public int b(int a) { return i < 42 ? c(b(a())) : 1; }
    public int c(int a) { return --a; }

    public static void main(String... args) {
        NestedCalls nc = new NestedCalls();

        System.out.println("a() :- " + nc.a());
        System.out.println("b() :- " + nc.b(nc.a()));
        System.out.println("b() :- " + nc.c(nc.b(nc.a())));
    }
}