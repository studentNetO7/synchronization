import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int numOfThreads = 1000;
        int lengthOfRoute = 100;
        String letters = "RLRFR";
        List<Thread> threads = new ArrayList<>();
        // Создаем потоки и их логику
        for (int i = 0; i < numOfThreads; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    String route = generateRoute(letters, lengthOfRoute); // генерируем маршрут
                    int letterR = counter(route); // считаем кол-во букв 'R'
                    synchronized (sizeToFreq) { // синхронизируем доступ к map
                        sizeToFreq.put(letterR, sizeToFreq.getOrDefault(letterR, 0) + 1);//кладем в map ключ(кол-во букв R)-значение(кол-во строк с этим кол-вом букв R), увеличивая на 1
                    }
                }
            });
            threads.add(thread); // добавляем поток в список потоков
            thread.start(); // запускаем поток
        }
        // Ожидаем завершения всех потоков
        for (Thread thread : threads) {
            thread.join();
        }
        // Анализируем и изымаем макс.значение map
        Map.Entry<Integer, Integer> maxEntry = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()) // метод max возвращает Optional
                .orElseThrow(() -> new NoSuchElementException("Мапа пуста")); // поэтому используем orElseThrow(), чтобы безопасно извлечь результат.
        System.out.println("Самое частое количество повторений: " + maxEntry.getKey() + " (встретилось " + maxEntry.getValue() + " раз(а)");

        // Выводим на печать частоты других значений
        System.out.println("Другие размеры:");
        sizeToFreq.entrySet()
                .stream()
                .filter(entry -> !entry.equals(maxEntry)) // исключаем запись с максимальным значением
                .sorted(Map.Entry.comparingByKey()) // сортируем по ключу
                .forEach(entry -> System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз(а))"));

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int counter(String route) {
        int count = 0;
        for (char c : route.toCharArray()) {
            if (c == 'R') {
                count++;
            }
        }
        return count;
    }
}
