package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.constant.BookStoreConstants;
import com.bittercode.constant.db.BooksDBConstants;
import com.bittercode.model.Book;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class AddBookServlet extends HttpServlet {
    BookService bookService = new BookServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);

        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }

        String bName = req.getParameter(BooksDBConstants.COLUMN_NAME);
        RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
        rd.include(req, res);
        StoreUtil.setActiveTab(pw, "addbook");
        pw.println("<div class='container my-2'>");
        if(bName == null || bName.isBlank()) {
            //render the add book form;
            showAddBookForm(pw);
            return;
        } //else process the add book
        
 
        try {
            //String uniqueID = UUID.randomUUID().toString();
            String bCode = req.getParameter(BooksDBConstants.COLUMN_BARCODE);
            String bAuthor = req.getParameter(BooksDBConstants.COLUMN_AUTHOR);
            double bPrice = Integer.parseInt(req.getParameter(BooksDBConstants.COLUMN_PRICE));
            int bQty = Integer.parseInt(req.getParameter(BooksDBConstants.COLUMN_QUANTITY));

            Book book = new Book(bCode, bName, bAuthor, bPrice, bQty);
            String message = bookService.addBook(book);
            if ("SUCCESS".equalsIgnoreCase(message)) {
                pw.println(
                        "<table class=\"tab\"><tr><td>Book Detail Updated Successfully!<br/>Add More Books</td></tr></table>");
            } else {
                pw.println("<table class=\"tab\"><tr><td>Failed to Add Books! Fill up CareFully</td></tr></table>");
                //rd.include(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<table class=\"tab\"><tr><td>Failed to Add Books! Fill up CareFully</td></tr></table>");
        }
    }
    private static void showAddBookForm(PrintWriter pw) {
        String form = "<table class=\"tab my-5\" style=\"width:40%;\">\r\n"
                + "        <tr>\r\n"
                + "            <td>\r\n"
                + "                <form action=\"addbook\" id=\"addbookForm\" method=\"post\" onsubmit=\"return validateForm()\">\r\n"
                + "                    <label for=\"bookCode\">Book Code : </label><input type=\"text\" name=\"barcode\" id=\"bookCode\" placeholder=\"Enter Book Code\" required><br/>\r\n"
                + "                    <span id=\"errorBookCode\" class=\"error\"></span><br/>\r\n"
                + "                    \r\n"
                + "                    <label for=\"bookName\">Book Name : </label> <input type=\"text\" name=\"name\" id=\"bookName\" placeholder=\"Enter Book's name\" required><br/>\r\n"
                + "                    <span id=\"errorBookName\" class=\"error\"></span><br/>\r\n"
                + "                    \r\n"
                + "                    <label for=\"bookAuthor\">Book Author : </label><input type=\"text\" name=\"author\" id=\"bookAuthor\" placeholder=\"Enter Author's Name\" required><br/>\r\n"
                + "                    <span id=\"errorBookAuthor\" class=\"error\"></span><br/>\r\n"
                + "                    \r\n"
                + "                    <label for=\"bookPrice\">Book Price : </label><input type=\"number\" name=\"price\" id=\"bookPrice\" placeholder=\"Enter the Price\" required><br/>\r\n"
                + "                    <span id=\"errorBookPrice\" class=\"error\"></span><br/>\r\n"
                + "                    \r\n"
                + "                    <label for=\"bookQuantity\">Book Quantity : </label><input type=\"number\" name=\"quantity\" id=\"bookQuantity\" placeholder=\"Enter the quantity\" required><br/>\r\n"
                + "                    <span id=\"errorBookQuantity\" class=\"error\"></span><br/>\r\n"
                + "                    \r\n"
                + "                    <input class=\"btn btn-success my-2\" type=\"submit\" value=\" Add Book \">\r\n"
                + "                </form>\r\n"
                + "            </td>\r\n"
                + "        </tr>  \r\n"
                + "        <tr>\r\n"
                + "            <td>\r\n"
                + "                <div id=\"errorMessages\" class=\"error\"></div>\r\n"
                + "            </td>\r\n"
                + "        </tr>\r\n"
                + "        <tr>\r\n"
                + "            <td><a href=\"index.html\">Go Back To Home Page</a></td>\r\n"
                + "        </tr> \r\n"
                + "    </table>\r\n"
                + "<style>\r\n"
                + ".error {\r\n"
                + "    color: red;\r\n"
                + "    font-size: 14px;\r\n"
                + "    margin-top: 5px;\r\n"
                + "}\r\n"
                + "</style>\r\n"
                + "<script>\r\n"
                + "function validateForm() {\r\n"
                + "    var bookCode = document.getElementById('bookCode').value;\r\n"
                + "    var bookName = document.getElementById('bookName').value;\r\n"
                + "    var bookAuthor = document.getElementById('bookAuthor').value;\r\n"
                + "    var bookPrice = document.getElementById('bookPrice').value;\r\n"
                + "    var bookQuantity = document.getElementById('bookQuantity').value;\r\n"
                + "    var errorMessages = '';\r\n"
                + "    \r\n"
                + "    // Clear previous error messages\r\n"
                + "    document.getElementById('errorMessages').innerText = '';\r\n"
                + "    document.getElementById('errorBookCode').innerText = '';\r\n"
                + "    document.getElementById('errorBookName').innerText = '';\r\n"
                + "    document.getElementById('errorBookAuthor').innerText = '';\r\n"
                + "    document.getElementById('errorBookPrice').innerText = '';\r\n"
                + "    document.getElementById('errorBookQuantity').innerText = '';\r\n"
                + "    \r\n"
                + "    // Validate Book Code (only numbers)\r\n"
                + "    if (!/^[0-9]+$/.test(bookCode)) {\r\n"
                + "        errorMessages += 'Please enter a valid book code (numbers only).\\n';\r\n"
                + "        document.getElementById('errorBookCode').innerText = 'Please enter a valid book code (numbers only).';\r\n"
                + "    }\r\n"
                + "    \r\n"
                + "    // Validate Book Name (only characters and special characters)\r\n"
                + "    if (!/^[a-zA-Z\\s@#$%^&*()_+-.,!]+$/.test(bookName)) {\r\n"
                + "        errorMessages += 'Please enter a valid book name (characters and special characters only).\\n';\r\n"
                + "        document.getElementById('errorBookName').innerText = 'Please enter a valid book name (characters and special characters only).';\r\n"
                + "    }\r\n"
                + "    \r\n"
                + "    // Validate Book Author (only characters)\r\n"
                + "    if (!/^[a-zA-Z\\s]+$/.test(bookAuthor)) {\r\n"
                + "        errorMessages += 'Please enter a valid book author (characters only).\\n';\r\n"
                + "        document.getElementById('errorBookAuthor').innerText = 'Please enter a valid book author (characters only).';\r\n"
                + "    }\r\n"
                + "    \r\n"
                + "    // Validate Book Price (positive integer greater than 0)\r\n"
                + "    if (!/^[1-9]\\d*$/.test(bookPrice)) {\r\n"
                + "        errorMessages += 'Please enter a valid positive integer for the book price (greater than 0).\\n';\r\n"
                + "        document.getElementById('errorBookPrice').innerText = 'Please enter a valid positive integer for the book price (greater than 0).';\r\n"
                + "    }\r\n"
                + "    \r\n"
                + "    // Validate Book Quantity (positive integer greater than 0)\r\n"
                + "    if (!/^[1-9]\\d*$/.test(bookQuantity)) {\r\n"
                + "        errorMessages += 'Please enter a valid positive integer for the book quantity (greater than 0).\\n';\r\n"
                + "        document.getElementById('errorBookQuantity').innerText = 'Please enter a valid positive integer for the book quantity (greater than 0).';\r\n"
                + "    }\r\n"
                + "    \r\n"
                + "    // Display error messages\r\n"
                + "    if (errorMessages !== '') {\r\n"
                + "        document.getElementById('errorMessages').innerText = errorMessages;\r\n"
                + "        return false;\r\n"
                + "    }\r\n"
                + "    \r\n"
                + "    return true;\r\n"
                + "}\r\n"
                + "</script>";

        pw.println(form);
    }



}
