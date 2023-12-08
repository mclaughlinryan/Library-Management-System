# Library-Management-System

This application allows users to check out books for a certain period of time from a library catalog. Information such as book title, author, publisher, genre, and number of pages is shown and the user can search for books based on title, author name, and genre. There's also administrative functionality for editing the checkout details for books.

#### Setup

The program uses Java Database Connectivity to connect to a MySQL database containing the book information. The book data is in the `books.csv` file in the `resources` folder which can be imported into a MySQL server for hosting the database. Once imported, the Java program can be run to connect to the server and retrieve the book data.
