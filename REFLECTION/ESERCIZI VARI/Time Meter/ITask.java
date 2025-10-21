public interface ITask {
    void compute(int n);
    void sleep(long ms) throws InterruptedException;
}