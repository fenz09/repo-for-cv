
import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public synchronized int findBookIndex(String isbn) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                return i;
            }
        }
        return -1;
    }

    public synchronized Book loanBook(int index) {
        if (index >= 0 && index < books.size()) {
            return books.remove(index);
        }
        return null;
    }

    public synchronized void returnBook(Book book) {
        books.add(book);
    }

    public static void main(String[] args) {
        Library library = new Library();
        Book book = new Book("John", "Doe", "Ab11228", 2023);
        library.returnBook(book);

        Member member1 = new Member(library, "Member1");
        Member member2 = new Member(library, "Member2");

        Thread thread1 = new Thread(member1);
        Thread thread2 = new Thread(member2);

        thread1.start();
        thread2.start();
    }
}

class Member implements Runnable {
    private Library library;
    private String name;

    public Member(Library library, String name) {
        this.library = library;
        this.name = name;
    }

    @Override
    public void run() {
        // Example action: find and loan a book
        int index = library.findBookIndex("Ab11228");
        if (index != -1) {
            Book loanedBook = library.loanBook(index);
            System.out.println(name + " loaned " + loanedBook);
            library.returnBook(loanedBook);
            System.out.println(name + " returned " + loanedBook);
        } else {
            System.out.println(name + " could not find the book.");
        }
    }
}
