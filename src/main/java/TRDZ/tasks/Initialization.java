package TRDZ.tasks;

import java.util.Arrays;

public class Initialization {
	public static final int Size_F = 10;// Количество элемнтов в масивах

	public static void main(String[] args) {
		System.out.println("Начало работы. Обработка массивов из "+Size_F+" элементов.");
		System.out.println("\nОбработка метода №1...");
		method_1(false);
		System.out.println("\nОбработка метода №2...");
		method_2(false,4);
		}

	/**
	 * Обработка первого массива с использованием главного потока
	 * @param show показывать ли результат проделаной работы?
	 */
	public static void method_1(boolean show) {
		Float[] ar1 = new Float[Size_F];
	//region Заполнение единицами
		long startTime = System.currentTimeMillis();
		made_of(ar1,(float)1);
		long endTime = System.currentTimeMillis();
		System.out.print("Заполнение масива через обобщеный метод выполнено за: ");
		System.out.println(endTime - startTime + "мс");
	//endregion
	//region Заполнение формулой через прямое действие
		startTime = System.currentTimeMillis();
		math_1(ar1);
		endTime = System.currentTimeMillis();
		System.out.print("Заполнение формулой через единственный главный поток выполнено за: ");
		System.out.println(endTime - startTime + "мс");
	//endregion
		if (show) {System.out.println("Результат: "+Arrays.toString(ar1));}
		}

	/**
	 * Обработка второго массива с использованием разбивки по потокам
	 * @param show показывать ли результат проделаной работы?
	 * @param wave количество потоков
	 */
	public static void method_2(boolean show, int wave) {
		Float[] ar2 = new Float[Size_F];
	//region Заполнение единицами
		long startTime = System.currentTimeMillis();
		made_of(ar2, (float) 1);
		long endTime = System.currentTimeMillis();
		System.out.print("Заполнение масива через обобщеный метод выполнено за: ");
		System.out.println(endTime - startTime + "мс");
	//endregion
	//region Заполнение формулой через х потоков
		startTime = System.currentTimeMillis();
		math_x(ar2, wave);
		endTime = System.currentTimeMillis();
		System.out.print("Заполнение формулой через регулируемое число потоков выполнено за: ");
		System.out.println(endTime - startTime + "мс");
	//endregion
		if (show) {System.out.println("Результат: "+Arrays.toString(ar2));}
		}

	/**
	 * Заполнение некоего масива некоеми одинаковыми элементами
	 * @param arr массив
	 * @param x элемент
	 * @param <Type> тип масива и элемента
	 * @return обновленный массив
	 */
	public static <Type> Type[] made_of(Type[] arr, Type x) {
		for (int i = 0; i < arr.length; i++) arr[i] = x;
		return arr;
		}

	/**
	 * Заполнение массива через формулу используя главный поток
	 * @param arr массив
	 * @return заполненый массив
	 */
	public static Float[] math_1(Float[] arr) {
		for (int i = 0; i < Size_F; i++) {
			arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
			}
		return arr;
		}

	/**
	 * Заполнение массива через формулу используя разбивку на потоки
	 * @param arr массив
	 * @param wave количество потоков
	 * @return заполненый массив
	 */
	public static Float[] math_x (Float[] arr, int wave) {
	//region Обьявление рабочих переменных
		wave=Math.min(wave,arr.length); 			//Отлаживаем количество потоков
		Thread[] threads = new Thread[wave];
		int part = arr.length/wave;
		int start = part + arr.length % wave;
		Float[] first = new Float[start];
		Float[][] segment = new Float[wave][part];
	//endregion
	//region Разбивка масива и отправка частей на обработку в потоки
		System.arraycopy(arr, 0, first, 0, start);
		threads[0] = new Thread(new Wave(first, 0),"Сегмент №"+1);
		threads[0].start();
		for (int j = 1; j < wave; j++) {
			System.arraycopy(arr, start, segment[j], 0, part);
			threads[j] = new Thread(new Wave(segment[j], start),"Сегмент №"+(j+1));
			threads[j].start();
			start+=part;
			}
	//endregion
	//region Ожидание завершения
		for (Thread Wave : threads) {
			try	{Wave.join();}
			catch (InterruptedException e) {e.printStackTrace();}
			}
	//endregion
	//region Склейка частей масива
		System.arraycopy(first, 0, arr, 0, first.length);
		start = first.length;
		for (int j = 1; j < wave; j++) {
			System.arraycopy(segment[j], 0, arr, start, part);
			start+=part;
			}
	//endregion
		return arr;
		}

	}
