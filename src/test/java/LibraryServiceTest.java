import library.model.Book;
import library.model.Reader;
import library.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryServiceTest {

    private LibraryService service;

    @BeforeEach
    void setUp() {
        service = new LibraryService();
    }

    @Test
    void testAddBook() {
        Book book = new Book("Test Book", "Author");
        service.addBook(book);
        assertEquals(1, service.getBooks().size());
    }

    @Test
    void testAddReader() {
        Reader reader = new Reader("Ivan");
        service.addReader(reader);
        assertEquals(1, service.getReaders().size());
    }

    @Test
    void testLendBookSuccess() {
        Book book = new Book("Java", "Schildt");
        Reader reader = new Reader("Petro");
        service.addBook(book);
        service.addReader(reader);

        boolean result = service.lendBook(reader, "Java");

        assertTrue(result);
        assertFalse(book.isAvailable());
        assertEquals(1, reader.getBorrowedBooks().size());
    }

    @Test
    void testLendBookNotFound() {
        Reader reader = new Reader("Petro");
        boolean result = service.lendBook(reader, "Non Existent");
        assertFalse(result);
    }

    @Test
    void testLendBookLimit() {
        Reader reader = new Reader("Ivan");
        // Додаємо 3 книги вручну
        reader.getBorrowedBooks().add(new Book("B1", "A"));
        reader.getBorrowedBooks().add(new Book("B2", "A"));
        reader.getBorrowedBooks().add(new Book("B3", "A"));

        service.addBook(new Book("B4", "A"));

        boolean result = service.lendBook(reader, "B4");
        assertFalse(result, "Читач не повинен мати змогу взяти 4-ту книгу");
    }

    @Test
    void testReturnBook() {
        Book book = new Book("1984", "Orwell");
        Reader reader = new Reader("Ivan");
        book.setAvailable(false);
        reader.getBorrowedBooks().add(book);

        boolean result = service.returnBook(reader, "1984");

        assertTrue(result);
        assertTrue(book.isAvailable());
        assertTrue(reader.getBorrowedBooks().isEmpty());
    }

    @Test
    void testFindReader() {
        service.addReader(new Reader("Oleg"));
        Reader found = service.findReader("oleg");
        assertNotNull(found);
        assertEquals("Oleg", found.getName());
    }

    @Test
    void testImportFromMissingFile() {
        assertDoesNotThrow(() -> {
            service.importFromFile("non_existent.csv");
        });
    }

    @Test
    void testLendBookWithMockReader() {
        Reader mockReader = mock(Reader.class);

        when(mockReader.canBorrow()).thenReturn(false);

        service.addBook(new Book("Clean Code", "Robert Martin"));
        boolean result = service.lendBook(mockReader, "Clean Code");

        assertFalse(result);
        verify(mockReader, atLeastOnce()).canBorrow();
    }
}
