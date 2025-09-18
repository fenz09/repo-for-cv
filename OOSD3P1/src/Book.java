
import java.util.ArrayList;
import java.util.List;

public class Book {

    private String authorFirstName;
    private String authorSurname;
    private String isbn;
    private int publishYear;

    public Book(String authorFirstName, String authorSurname, String isbn, int publishYear) {
        this.authorFirstName = authorFirstName;
        this.authorSurname = authorSurname;
        this.isbn = isbn;
        this.publishYear = publishYear;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public void setAuthorSurname(String authorSurname) {
        this.authorSurname = authorSurname;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    @Override
    public String toString() {
        return "Book [authorFirstName=" + authorFirstName + ", authorSurname=" + authorSurname + ", isbn=" + isbn
                + ", publishYear=" + publishYear + "]";
    }

    public static class Library {
        private List<Book> books;

        public Library() {
            books = new ArrayList<>();
        }

        public int findBookIndex(String isbn) {
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getIsbn().equals(isbn)) {
                    return i;
                }
            }
            return -1;
        }

        public Book loanBook(int index) {
            if (index >= 0 && index < books.size()) {
                return books.remove(index);
            }
            return null;
        }

        public void returnBook(Book book) {
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

    public static class Member implements Runnable {
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
}
