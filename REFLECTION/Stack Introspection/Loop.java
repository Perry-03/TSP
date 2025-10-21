public class Loop implements ILoop {

    public void a() { b(); }
    public void b() { c(); }
    public void c() { a(); }

    public void d(int a) { System.out.println("print: " + a); }
    public void e(int a) { for (int i = 0; i < a; i++) d(--a); }
}