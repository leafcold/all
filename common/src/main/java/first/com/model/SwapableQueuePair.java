package first.com.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class SwapableQueuePair<T> {

    private List<Queue<T>> queuePair;

    private int[] indexs;

    public SwapableQueuePair() {
        super();
        ArrayList<Queue<T>> shortList = new ArrayList<>();
        shortList.add(new ConcurrentLinkedQueue<T>());
        shortList.add(new ConcurrentLinkedQueue<T>());
        this.queuePair = shortList;
        indexs = new int[]{0, 1};
    }

    //    private ReadWriteLock lock = new ReentrantReadWriteLock();
    public synchronized Queue<T> get() {
        return queuePair.get(indexs[0]);
    }

    public synchronized void add(T t) {
        queuePair.get(indexs[1]).add(t);
    }

    public synchronized void swap() {
        int temp = indexs[0];
        indexs[0] = indexs[1];
        indexs[1] = temp;
    }

    @Override
    public String toString() {
        return "SwapableQueuePair{" +
                "queuePair=" + queuePair +
                ", indexs=" + Arrays.toString(indexs) +
                '}';
    }

    public static void main(String[] args) throws Exception {
        long now = System.currentTimeMillis();
        SwapableQueuePair<Integer> queuePair = new SwapableQueuePair<Integer>();
        int count = 50000000;
        List<Integer> list = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int start = 0;
                do {
                    queuePair.add(start++);
                } while (list.size() < count);
                countDownLatch.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (list.size() < count) {
                    queuePair.swap();
                    Queue<Integer> data = queuePair.get();
                    while (!data.isEmpty()) {
                        Integer poll = data.poll();
                        if (poll != null) {
                            list.add(poll);
                        }
                    }
                }
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await();


        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) - list.get(i - 1) < 0) {
                System.out.println("看到这行说明代码有问题");
                System.out.println(list.get(i - 1) + "___" + list.get(i));
            }
        }
        System.out.println(System.currentTimeMillis() - now);
    }
}
