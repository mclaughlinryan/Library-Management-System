# Library-Management-System

This application allows users to check out books for a certain period of time from a library catalog. Information such as book title, author, publisher, genre, and number of pages is shown, and the user can search for books based on title, author name, and genre. There's also administrative functionality for editing the checkout details for books.

#### Setup

The program uses Java Database Connectivity to connect to a MySQL database containing the book information. The book data is in the `books.csv` file in the `resources` folder which can be imported into a MySQL server for hosting the database. Once imported, the Java program can connect to the server to retrieve the book data.

#### Application

Application interface:

<img width="700" alt="library management system" src="https://github.com/mclaughlinryan/Library-Management-System/assets/150348966/0521e767-1ff1-4167-9746-58c8dc243193">

&nbsp;
Administrator view:

<img width="700" alt="library management system 3" src="https://github.com/mclaughlinryan/Library-Management-System/assets/150348966/766a4725-9301-49cc-b27b-a49f4d68c969">

Admin login:
Username: admin
Password: admin

As an administrator, the book checkout date and time of checkout can be edited for each book.
