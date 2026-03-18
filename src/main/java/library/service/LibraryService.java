package library.service;

import library.model.Book;
import library.model.Reader;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {
    private List<Book> books = new ArrayList<>();
    private List<Reader> readers = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public boolean lendBook(Reader reader, String bookTitle) {
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(bookTitle) && b.isAvailable() && reader.canBorrow()) {
                b.setAvailable(false);
                reader.getBorrowedBooks().add(b);
                return true;
            }
        }
        return false;
    }

    public boolean returnBook(Reader reader, String bookTitle) {

        for (Book b : reader.getBorrowedBooks()) {
            if (b.getTitle().equalsIgnoreCase(bookTitle)) {
                b.setAvailable(true);
                reader.getBorrowedBooks().remove(b);
                return true;
            }
        }
        return false;
    }

    public void exportToFile(String filename) throws IOException {
        List<Book> sortedBooks = books.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Book b : sortedBooks) {
                writer.println(b.getTitle() + "," + b.getAuthor() + "," + b.isAvailable());
            }
        }
    }

    public List<Book> getBooks() { return books; }

    public void importFromFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Book book = new Book(parts[0], parts[1]);
                    book.setAvailable(Boolean.parseBoolean(parts[2]));
                    this.books.add(book);
                }
            }
        }
    }

    public void addReader(Reader reader) {
        if (!readers.contains(reader)) {
            readers.add(reader);
        }
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public Reader findReader(String name) {
        return readers.stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void exportReaders(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Reader r : readers) {

                writer.println(r.getName());
            }
        }
    }

    public void importReaders(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                if (!name.isEmpty()) {
                    addReader(new Reader(name));
                }
            }
        }
    }
}
