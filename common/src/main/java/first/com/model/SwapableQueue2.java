package first.com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class SwapableQueue2<T> {

    private int maxIndex;
    private AtomicLong curIndex;
//    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public int getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    public AtomicLong getCurIndex() {
        return curIndex;
    }

    public void setCurIndex(AtomicLong curIndex) {
        this.curIndex = curIndex;
    }

    public HashMap<Long, ConcurrentLinkedQueue<T>> getTest() {
        return test;
    }

    public void setTest(HashMap<Long, ConcurrentLinkedQueue<T>> test) {
        this.test = test;
    }


    private  HashMap<Long, ConcurrentLinkedQueue<T>> test;


    public SwapableQueue2(int maxIndex) {
        test = new HashMap<>();
        this.curIndex = new AtomicLong(0);
        this.maxIndex = maxIndex;
        for (int i = 0; i < maxIndex; i++) {
            test.put((long) i, new ConcurrentLinkedQueue<T>());
        }
    }

    public  synchronized   void add(T t) {
        System.out.println("加"+curIndex.get() + "___" + t);
        test.get((curIndex.get()+1) % maxIndex).add(t);
    }

    public synchronized   ConcurrentLinkedQueue<T> getData() {
        curIndex.incrementAndGet();
        ConcurrentLinkedQueue<T> ts = test.get(curIndex.get() % maxIndex);
        if(!ts.isEmpty()){
            System.out.println(curIndex.get() +"data = " + ts);

        }

        return ts;
    }


    public static void main(String[] args) throws Exception {
        long now = System.currentTimeMillis();
        SwapableQueue2<Integer> swapableQueue2 = new SwapableQueue2<Integer>(50);
        int count = 5000;
        List<Integer> list = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int start = 0;
                do {
                    swapableQueue2.add(start++);
                } while (start < count);
                countDownLatch.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (list.size() < count) {
                    ConcurrentLinkedQueue<Integer> data = swapableQueue2.getData();

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
        System.out.println(list.get(list.size() - 1));

    }

}
