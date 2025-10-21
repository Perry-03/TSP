import java.lang.reflect.Proxy;
public class Main {
    public static void main(String... args) {
        IBankAccount b = new BankAccount("Luca", 500.0);

        IBankAccount ib = (IBankAccount) Proxy.newProxyInstance(
            b.getClass().getClassLoader(),
            new Class[]{IBankAccount.class},
            new TraceHandler(b)
        );

        ib.deposit(1000.0);
        ib.withdraw(100.0);
        ib.deposit(1.75);
        ib.withdraw(476.99);
    }    
}
