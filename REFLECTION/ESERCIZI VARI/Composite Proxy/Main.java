import java.lang.reflect.Proxy;

public class Main {
    public static void main(String... args) {
        IBankAccount b = new BankAccount("Luca", 6000.0);

        CompositeProxy compositeProxy = 
            new LoggingStateHandler(
                b,
                new TimeCounterHandler(
                    b,
                    CompositeProxy.NULL_HANDLER
                )
            );

        IBankAccount ib = (IBankAccount) Proxy.newProxyInstance(
            b.getClass().getClassLoader(),
            new Class[]{IBankAccount.class},
            compositeProxy
        );


        ib.withdraw(30);
    }
}