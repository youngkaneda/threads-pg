package ag.threads;

public class BlockingQueue {
	private int currentIndex = -1;
	private Object[] locks;
	
	private void init(int c){
		for (int i = 0; i < c; i++) {
			locks[i] = new Object();
		}
	}
	
	public BlockingQueue(int capacity) {
		locks = new Object[capacity];
		init(capacity);
	}
	
	public void attend() throws InterruptedException{
		synchronized (locks) {
			//fila vazia, aguardar a entrada de um elemento
			if (currentIndex == -1){
				locks.wait();
			} 
			//remove um elemento da fila
			currentIndex = currentIndex - 1;
		}
	}
	
	public void enqueue() throws InterruptedException{
		synchronized (locks) {
			//coloca um elemento na fila
			currentIndex = currentIndex + 1;
			//notifica a chegada
			locks.notify();
		}
	}
}
