public class HeavyTask implements ITask {
    public void compute(int n) {
        for (int i = 0; i < n; i++) Math.sqrt(Math.random());
    }

    public void sleep(long ms) throws InterruptedException {
        Thread.sleep(ms);
    }
}
