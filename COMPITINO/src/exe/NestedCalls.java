package exe;

public class NestedCalls implements NestedCallsI {
	private int i = 1;
	
	public static void main(String[] args) {
		NestedCalls nc = new NestedCalls();
		
		System.out.println("a() :- " + nc.a());
		System.out.println("b(a()) :- " + nc.b(nc.a()));
		System.out.println("c(b(a())) :- " + nc.c(nc.b(nc.a())));	
	}
	
	@Override
	public int a() { return b(i++); }

	@Override
	public int b(int a) { return (i < 42) ? c(b(a())) : 1; }

	@Override
	public int c(int a) { return --a; }	
}
