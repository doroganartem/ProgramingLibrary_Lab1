package library;

import library.model.Book;
import library.model.Reader;
import library.service.LibraryService;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        LibraryService service = new LibraryService();

        try {
            service.importFromFile("library.csv");
            service.importReaders("readers.csv");
        } catch (IOException e) {
            System.out.println("Файл даних не знайдено, починаємо з чистого аркуша.");
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Показати книги\n2. Взяти книгу\n3. Повернути книгу\n4. Додати кингу\n5. Додати читача\n0. Вихід");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                service.getBooks().forEach(System.out::println);
            } else if (choice == 2) {
                System.out.print("Введіть ваше ім'я: ");
                String readerName = sc.nextLine();
                Reader currentReader = service.findReader(readerName);

                if (currentReader != null) {
                    System.out.print("Назва книги: ");
                    String title = sc.nextLine();
                    if (service.lendBook(currentReader, title)) {
                        System.out.println("Успішно! Книгу видано читачу " + currentReader.getName());
                    } else {
                        System.out.println("Неможливо взяти книгу");
                    }
                } else {
                    System.out.println("Читача з таким ім'ям не знайдено! Спочатку зареєструйтесь");
                }
            }
            else if(choice == 3){
                System.out.print("Введіть ваше ім'я: ");
                String readerName = sc.nextLine();
                Reader currentReader = service.findReader(readerName);

                if (currentReader != null) {
                    System.out.print("Яку книгу повертаємо? ");
                    String title = sc.nextLine();
                    if (service.returnBook(currentReader, title)) {
                        System.out.println("Книгу повернуто успішно.");
                    } else {
                        System.out.println("У вас немає такої книги.");
                    }
                } else {
                    System.out.println("Читача не знайдено.");
                }
            } else if (choice == 4) {
                System.out.print("Назва: ");
                String title = sc.nextLine();
                System.out.print("Автор: ");
                String author = sc.nextLine();
                service.addBook(new Book(title, author));
                System.out.println("Книгу додано!");

                try {
                    service.exportToFile("library.csv");
                }catch (Exception e){
                    System.out.println("Помилка збереження.");
                }

            } else if (choice == 5) {
                System.out.print("Введіть ім'я нового читача: ");
                String name = sc.nextLine();
                service.addReader(new Reader(name));
                System.out.println("Читача " + name + " успішно додано!");
                try {
                    service.exportReaders("readers.csv");
                }catch (Exception e){
                    System.out.println("Помилка збереження.");
                }

            } else break;
        }

    }
}