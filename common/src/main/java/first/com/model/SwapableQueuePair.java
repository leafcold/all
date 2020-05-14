package first.com.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SwapableQueuePair<T> {

	private volatile List<Queue<T>> queuePair;
	
	private int[] indexs;

	public SwapableQueuePair() {
		super();
		ArrayList<Queue<T>> shortList = new ArrayList<>();
		shortList.add(new ConcurrentLinkedQueue<T>());
		shortList.add(new ConcurrentLinkedQueue<T>());
		this.queuePair = shortList;
		indexs = new int[]{0, 1};
	}

	public Queue<T> first() {
		return queuePair.get(indexs[0]);
	}

	public Queue<T> second() {
		return queuePair.get(indexs[1]);
	}

	public void swap() {
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
}
