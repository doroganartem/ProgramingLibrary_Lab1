package library.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reader {
    private String name;
    private List<Book> borrowedBooks = new ArrayList<>();

    public Reader(String name) {
        this.name = name;
    }

    public boolean canBorrow() {
        return borrowedBooks.size() < 3;
    }

    public String getName() { return name; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return Objects.equals(name, reader.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
