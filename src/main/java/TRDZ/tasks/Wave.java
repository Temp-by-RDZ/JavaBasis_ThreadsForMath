package TRDZ.tasks;

class Wave extends Thread{
	private final Float[] math;
	private int i;

	/**
	 * Обьявление
	 * @param math информация о сегменте
	 * @param i начальный индекс элемнта сегмента относительно его места в основе
	 */
	public Wave(Float[] math, int i) {
		this.math=math;
		this.i=i;
		}

	@Override
	public void run() {
		for (int j = 0; j< math.length; j++) { //Вычисление значений
			math[j] = (float) (math[j] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
			i++;
			}
		}
	}
