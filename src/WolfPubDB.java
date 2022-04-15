import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.sql.Types;

import javax.naming.spi.DirStateFactory.Result;
import javax.sound.midi.SysexMessage;

public class WolfPubDB {
        static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/sthota";

        private static Connection connection = null;
        private static Statement statement = null;

        private static ResultSet result = null;

        static Scanner scanner;

        private static final String HORIZONTAL_SEP = "-";
        private static String verticalSep = "|";
        private static String joinSep = " ";
        private static String[] headers;
        private static List<String[]> rows = new ArrayList<>();
        private static boolean rightAlign;

        private static PreparedStatement editorAssignmentQuery;
        private static PreparedStatement editorUnAssignmentQuery;

        private static PreparedStatement insertPublicationQuery;
        private static PreparedStatement updatePublicationTitleQuery;
        private static PreparedStatement updatePublicationTopicQuery;
        private static PreparedStatement updatePublicationTypeQuery;
        private static PreparedStatement updatePublicationPriceQuery;
        private static PreparedStatement deletePublicationQuery;
        private static PreparedStatement updateBookEditionISBNQuery;
        private static PreparedStatement getLastPublicationID;

        private static PreparedStatement insertBookQuery;
        private static PreparedStatement updateBookISBNQuery;
        private static PreparedStatement updateBookEditionQuery;
        private static PreparedStatement updateBookPublicationDateQuery;
        private static PreparedStatement deleteBookQuery;
        private static PreparedStatement deleteBookEditionQuery;
        private static PreparedStatement displayAllBooksQuery;

        private static PreparedStatement insertChapterQuery;
        private static PreparedStatement updateChapterTitleQuery;
        private static PreparedStatement updateChapterTextQuery;
        private static PreparedStatement deleteChaptersQuery;
        private static PreparedStatement displayChaptersQuery;

        private static PreparedStatement insertPeriodicalQuery;
        private static PreparedStatement updatePeriodicalIssueDateQuery;
        private static PreparedStatement updatePeriodicalPeriodicityQuery;
        private static PreparedStatement deletePeriodicalQuery;
        private static PreparedStatement displayAllPeriodicalsQuery;

        private static PreparedStatement insertDistributorPaymentQuery;
        private static PreparedStatement updateDistributorPaymentAmountPaidQuery;
        private static PreparedStatement deleteDistributorPaymentQuery;

        private static PreparedStatement insertArticleQuery;
        private static PreparedStatement updateArticleTitleQuery;
        private static PreparedStatement updateArticleCreationDateQuery;
        private static PreparedStatement updateArticleTextQuery;
        private static PreparedStatement deleteArticleQuery;
        private static PreparedStatement displayArticlesQuery;

        private static PreparedStatement insertDistributorQuery;
        private static PreparedStatement updateDistributorAccountNumberQuery;
        private static PreparedStatement updateDistributorPhoneQuery;
        private static PreparedStatement updateDistributorCityQuery;
        private static PreparedStatement updateDistributorStreetAddressQuery;
        private static PreparedStatement updateDistributorTypeQuery;
        private static PreparedStatement updateDistributorNameQuery;
        private static PreparedStatement updateDistributorBalanceQuery;
        private static PreparedStatement updateDistributorContactPersonQuery;
        private static PreparedStatement deleteDistributorQuery;
        private static PreparedStatement generateBillQuery;
        private static PreparedStatement deduceBalanceQuery;
        private static PreparedStatement showOrdersDistributorQuery;
        private static PreparedStatement showDistributorBalanceQuery;
        private static PreparedStatement showDistributorPaymentsQuery;
        private static PreparedStatement showArticlesPeriodicalQuery;
        private static PreparedStatement showAllDistributorsQuery;
        private static PreparedStatement generateBillDisplay;

        private static PreparedStatement insertOrderQuery;
        private static PreparedStatement updateOrderDateQuery;
        private static PreparedStatement updateOrderDeliveryDateQuery;
        private static PreparedStatement updateOrderNumberOfCopiesQuery;
        private static PreparedStatement updateOrderTotalCostQuery;
        private static PreparedStatement updateOrderShippingCostQuery;
        private static PreparedStatement deleteOrderQuery;
        private static PreparedStatement getOrderID;

        private static PreparedStatement insertPaymentQuery;
        private static PreparedStatement updatePaymentAmountQuery;
        private static PreparedStatement updatePaymentCollectionDateQuery;
        private static PreparedStatement deletePaymentQuery;

        private static PreparedStatement addAuthorQuery;
        private static PreparedStatement addStaffQuery;
        private static PreparedStatement addEditorQuery;
        private static PreparedStatement updateStaffQuery;
        private static PreparedStatement deleteStaffQuery;

        private static PreparedStatement addEditsQuery;
        private static PreparedStatement deleteEditsQuery;

        private static PreparedStatement addWritesArticlesQuery;
        private static PreparedStatement deleteWritesArticlesQuery;

        private static PreparedStatement addWritesBookQuery;
        private static PreparedStatement deleteWritesBookQuery;

        // private static PreparedStatement deletePaymentQuery;
        // private static PreparedStatement deletePaymentQuery;

        // reports

        private static PreparedStatement copiesPerDistributorPerMonthlyReportQuery;
        private static PreparedStatement monthlyTotalRevenueReportQuery;
        private static PreparedStatement monthlyTotalExpenseReportQuery;
        private static PreparedStatement currentTotalDistributorsReportQuery;
        private static PreparedStatement totalRevenuePerCityReportQuery;
        private static PreparedStatement totalRevenuePerDistibutorReportQuery;
        private static PreparedStatement totalRevenuePerLocationReportQuery;
        private static PreparedStatement totalStaffPaymentsPerPeriodPerWorkTypeReportQuery;

        // select statements

        private static PreparedStatement showPublications;
        private static PreparedStatement showStaff;
        private static PreparedStatement showBooks;
        private static PreparedStatement showOrders;
        private static PreparedStatement showEdits;
        private static PreparedStatement showPayments;
        private static PreparedStatement showDistributors;
        private static PreparedStatement showDistributorPayments;
        private static PreparedStatement showEditors;
        private static PreparedStatement showAuthors;
        private static PreparedStatement showPeriodicals;
        private static PreparedStatement showChapters;
        private static PreparedStatement showArticles;
        private static PreparedStatement showWritesBooks;
        private static PreparedStatement showWritesArticles;
        private static PreparedStatement findBookByPublicationIdQuery;
        private static PreparedStatement findBookByISBNdQuery;
        private static PreparedStatement findPeriodicalByPIDQuery;
        private static PreparedStatement showAuthorByID;
        private static PreparedStatement showEditorByID;
        private static PreparedStatement findPublicationByEditorQuery;
        private static PreparedStatement findBookByAuthorQuery;
        private static PreparedStatement findArticleByAuthorQuery;
        private static PreparedStatement showPaymentsForStaff;

        // generates Prepared Statements for all queries to perform operations.
        public static void generateDDLAndDMLStatements() {
                String query;
                try {
                        // Queries for Publication operations
                        query = "INSERT INTO `Publications` (`publication_ID`, `title`, `topic`, `type`, `price`) VALUES (?, ?, ?, ?, ?);";
                        insertPublicationQuery = connection.prepareStatement(query);
                        query = "UPDATE `Publications`" + " SET `price` = ? WHERE `publication_ID`= ?;";
                        updatePublicationPriceQuery = connection.prepareStatement(query);
                        query = "UPDATE `Publications`" + " SET `TOPIC` = ? WHERE `publication_ID`= ?;";
                        updatePublicationTopicQuery = connection.prepareStatement(query);
                        query = "UPDATE `Publications`" + " SET `title` = ? WHERE `publication_ID`= ?;";
                        updatePublicationTitleQuery = connection.prepareStatement(query);
                        query = "UPDATE `Publications`" + " SET `type` = ? WHERE `publication_ID`= ?;";
                        updatePublicationTypeQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Publications`" + " WHERE `publication_ID` = ?;";
                        deletePublicationQuery = connection.prepareStatement(query);
                        query = "SELECT publication_ID from `Publications`;";
                        getLastPublicationID = connection.prepareStatement(query);

                        // Edits Queries
                        query = "INSERT INTO `Edits` (`staff_ID`, `publication_ID`) VALUES (?, ?);";
                        editorAssignmentQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Edits`" + " WHERE `staff_ID` = ? AND `publication_ID`=?;";
                        editorUnAssignmentQuery = connection.prepareStatement(query);

                        // Queries for Book operations
                        query = "INSERT INTO `Books` (`publication_ID`, `ISBN`,`Edition`,`publication_date`) VALUES (?,?,?,?);";
                        insertBookQuery = connection.prepareStatement(query);
                        query = "UPDATE `Books`" + " SET `ISBN` = ? WHERE `publication_ID`= ?;";
                        updateBookISBNQuery = connection.prepareStatement(query);
                        query = "UPDATE `Books`" + " SET `Edition` = ? WHERE `publication_ID`= ?;";
                        updateBookEditionQuery = connection.prepareStatement(query);
                        query = "UPDATE `Books`" + " SET `publication_date` = ? WHERE `publication_ID`= ?;";
                        updateBookPublicationDateQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Publications`" + " WHERE `publication_ID` = ?;";
                        deleteBookQuery = connection.prepareStatement(query);
                        query = "SELECT * from Books natural join Publications;";
                        displayAllBooksQuery = connection.prepareStatement(query);
                        query = "SELECT * FROM `Books`WHERE `publication_ID` = ?;";
                        findBookByPublicationIdQuery = connection.prepareStatement(query);
                        query = "SELECT * FROM `Books` WHERE `ISBN` = ?;";
                        findBookByISBNdQuery = connection.prepareStatement(query);

                        // Queries for Chapters operations
                        query = "INSERT INTO `Chapters` (`publication_ID`, `title`, `text`) VALUES (?, ?, ?);";
                        insertChapterQuery = connection.prepareStatement(query);
                        query = "UPDATE `Chapters` SET `title` = ? WHERE `publication_ID`= ? AND `title`=?;";
                        updateChapterTitleQuery = connection.prepareStatement(query);
                        query = "UPDATE `Chapters` SET `text` = ? WHERE `publication_ID`= ? AND `title`=?;";
                        updateChapterTextQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Chapters`" + " WHERE `publication_ID` = ? and `title`= ?;";
                        deleteChaptersQuery = connection.prepareStatement(query);
                        query = "Select * From `Chapters` WHERE `publication_ID`= ?;";
                        displayChaptersQuery = connection.prepareStatement(query);

                        // Queries for Periodicals operations
                        query = "INSERT INTO `Periodicals` (`publication_ID`, `issue_date`,`periodicity`) VALUES (?,?,?);";
                        insertPeriodicalQuery = connection.prepareStatement(query);
                        query = "UPDATE `Periodicals`" + " SET `issue_date` = ? WHERE `publication_ID`= ?;";
                        updatePeriodicalIssueDateQuery = connection.prepareStatement(query);
                        query = "UPDATE `Periodicals`" + " SET `periodicity` = ? WHERE `publication_ID`= ?;";
                        updatePeriodicalPeriodicityQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Publications`" + " WHERE `publication_ID` = ?;";
                        deletePeriodicalQuery = connection.prepareStatement(query);
                        query = "SELECT * from Periodicals natural join Publications;";
                        displayAllPeriodicalsQuery = connection.prepareStatement(query);
                        query = "SELECT * FROM Periodicals WHERE publication_ID=?;";
                        findPeriodicalByPIDQuery = connection.prepareStatement(query);

                        // Queries for Distributor Payments
                        query = "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`, `amount_paid`) VALUES (?, ?, ?);";
                        insertDistributorPaymentQuery = connection.prepareStatement(query);
                        query = "UPDATE `DistributorPayments`"
                                        + " SET `amount_paid` = ? WHERE `account_number`= ? AND `payment_date`=?;";
                        updateDistributorPaymentAmountPaidQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `DistributorPayments`"
                                        + " WHERE `account_number`= ? AND `payment_date`=?;";
                        deleteDistributorPaymentQuery = connection.prepareStatement(query);
                        query = "select * from `Distributors`;";
                        showAllDistributorsQuery = connection.prepareStatement(query);

                        // Queries for Article operations
                        query = "INSERT INTO `Articles` (`publication_id`, `title`, `text`, `creation_date`) VALUES (?, ?, ?, ?);";
                        insertArticleQuery = connection.prepareStatement(query);
                        query = "UPDATE `Articles` SET `title` = ? WHERE `publication_ID`= ? AND `title`=?;";
                        updateArticleTitleQuery = connection.prepareStatement(query);
                        query = "UPDATE `Articles` SET `text` = ? WHERE `publication_ID`= ? AND `title`=?;";
                        updateArticleTextQuery = connection.prepareStatement(query);
                        query = "UPDATE `Articles` SET `creation_date` = ? WHERE `publication_ID`= ? AND `title`=?;";
                        updateArticleCreationDateQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Articles`" + " WHERE `publication_ID` = ? and `title`= ?;";
                        deleteArticleQuery = connection.prepareStatement(query);
                        query = "Select * From `Articles` WHERE `publication_ID`= ?;";
                        displayArticlesQuery = connection.prepareStatement(query);

                        // Queries for Distributor operations
                        query = "INSERT INTO Distributors(`account_number`, `phone`, `city`, `street_address`, `type`, `name`, `balance`, `contact_person`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
                        insertDistributorQuery = connection.prepareStatement(query);
                        query = "UPDATE `Distributors`" + " SET `account_number` = ? WHERE account_number= ?;";
                        updateDistributorAccountNumberQuery = connection.prepareStatement(query);
                        query = "UPDATE `Distributors`" + " SET `phone` = ? WHERE account_number = ?;";
                        updateDistributorPhoneQuery = connection.prepareStatement(query);
                        query = "UPDATE `Distributors`" + " SET `city` = ? WHERE account_number = ?;";
                        updateDistributorCityQuery = connection.prepareStatement(query);
                        query = "UPDATE `Distributors`" + " SET `street_address` = ? WHERE account_number = ?;";
                        updateDistributorStreetAddressQuery = connection.prepareStatement(query);
                        query = "UPDATE `Distributors`" + " SET `type` = ? WHERE account_number = ?;";
                        updateDistributorTypeQuery = connection.prepareStatement(query);
                        query = "UPDATE `Distributors`" + " SET `name` = ? WHERE account_number = ?;";
                        updateDistributorNameQuery = connection.prepareStatement(query);
                        query = "UPDATE `Distributors`" + " SET `balance` = ? WHERE account_number = ?;";
                        updateDistributorBalanceQuery = connection.prepareStatement(query);
                        query = "UPDATE `Distributors`" + " SET `contact_person` = ? WHERE account_number = ?;";
                        updateDistributorContactPersonQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Distributors`" + " WHERE `account_number` = ?;";
                        deleteDistributorQuery = connection.prepareStatement(query);
                        query = "UPDATE `Distributors` d join `Orders` o SET d.balance = d.balance + o.total_cost + o.shipping_cost  WHERE o.order_number = ?;";
                        generateBillQuery = connection.prepareStatement(query);
                        query = "UPDATE Distributors d join DistributorPayments dp ON dp.account_number = d.account_number  SET d.balance = d.balance - dp.amount_paid WHERE dp.account_number = ? AND dp.payment_date = ?;";
                        deduceBalanceQuery = connection.prepareStatement(query);
                        query = "SELECT * FROM `Orders` WHERE distributor_account_no = ?;";
                        showOrdersDistributorQuery = connection.prepareStatement(query);
                        query = "SELECT * FROM `Distributors` WHERE account_number=?;";
                        showDistributorBalanceQuery = connection.prepareStatement(query);
                        query = "SELECT * FROM `DistributorPayments` WHERE account_number=?;";
                        showDistributorPaymentsQuery = connection.prepareStatement(query);
                        query = "select * from `Articles` where publication_ID=?;";
                        showArticlesPeriodicalQuery = connection.prepareStatement(query);
                        query = "select * from `Distributors`;";
                        showAllDistributorsQuery = connection.prepareStatement(query);
                        query = "SELECT balance FROM `Distributors` WHERE account_number=?;";
                        generateBillDisplay = connection.prepareStatement(query);

                        // Queries for Order operations
                        query = "INSERT INTO `Orders` (`order_number`, `publication_ID`, `distributor_account_no`, `order_date`, `order_delivery_date`, `number_of_copies`, `total_cost`, `shipping_cost`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
                        insertOrderQuery = connection.prepareStatement(query);
                        query = "UPDATE `Orders`" + " SET `order_date` = ? WHERE order_number= ?;";
                        updateOrderDateQuery = connection.prepareStatement(query);
                        query = "UPDATE `Orders`" + " SET `order_delivery_date` = ? WHERE order_number = ?;";
                        updateOrderDeliveryDateQuery = connection.prepareStatement(query);
                        query = "UPDATE `Orders`" + " SET `number_of_copies` = ? WHERE order_number = ?;";
                        updateOrderNumberOfCopiesQuery = connection.prepareStatement(query);
                        query = "UPDATE `Orders`" + " SET `total_cost` = ? WHERE order_number = ?;";
                        updateOrderTotalCostQuery = connection.prepareStatement(query);
                        query = "UPDATE `Orders`" + " SET `shipping_cost` = ? WHERE order_number = ?;";
                        updateOrderShippingCostQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Orders`" + " WHERE `order_number` = ?;";
                        deleteOrderQuery = connection.prepareStatement(query);
                        query = "SELECT order_number FROM Orders";
                        getOrderID = connection.prepareStatement(query);

                        // Queries for Payment operations
                        query = "INSERT INTO `Payments` (`staff_ID`, `salary_date`, `payment_amount`, `collection_date`) VALUES (?, ?, ?, ?);";
                        insertPaymentQuery = connection.prepareStatement(query);
                        query = "UPDATE `Payments`"
                                        + " SET `payment_amount` = ? WHERE staff_ID = ? AND salary_date = ?;";
                        query = "UPDATE `Payments`" + " SET `payment_amount` = ? WHERE order_number = ?;";
                        updatePaymentAmountQuery = connection.prepareStatement(query);
                        query = "UPDATE `Payments`"
                                        + " SET `collection_date` = ? WHERE staff_ID = ? AND salary_date = ?;";
                        updatePaymentCollectionDateQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Payments`" + " WHERE `staff_ID` = ? AND salary_date = ?;";

                        deletePaymentQuery = connection.prepareStatement(query);

                        query = "INSERT INTO Authors" + " VALUES (?,?);";
                        addAuthorQuery = connection.prepareStatement(query);

                        query = "INSERT INTO Editors (staff_ID, type) VALUES (?,?);";
                        addEditorQuery = connection.prepareStatement(query);

                        query = "INSERT INTO Staff VALUES (?,?,?,?,?,?,?,?);";
                        addStaffQuery = connection.prepareStatement(query);

                        query = "UPDATE Staff SET name = ?, phone_number = ?, Age  = ?, Gender = ?,  Email  = ?, Address = ? WHERE staff_ID = ? ;";
                        updateStaffQuery = connection.prepareStatement(query);

                        query = "Delete FROM Staff WHERE staff_ID = ? ;";
                        deleteStaffQuery = connection.prepareStatement(query);

                        query = "INSERT INTO Edits Values (?,?) ;";
                        addEditsQuery = connection.prepareStatement(query);

                        query = "DELETE FROM Edits WHERE staff_ID = ? AND publication_ID = ? ;";
                        deleteEditsQuery = connection.prepareStatement(query);

                        query = "INSERT INTO WritesArticles Values (?,?,?) ;";
                        addWritesArticlesQuery = connection.prepareStatement(query);

                        query = "DELETE FROM WritesArticles WHERE staff_ID = ? AND publication_ID = ?, title = ? ;";
                        deleteWritesArticlesQuery = connection.prepareStatement(query);

                        query = "INSERT INTO WritesBook Values (?,?) ;";
                        addWritesBookQuery = connection.prepareStatement(query);

                        query = "DELETE FROM WritesBook WHERE staff_ID = ? AND publication_ID = ? ;";
                        deleteWritesBookQuery = connection.prepareStatement(query);

                        // report queries

                        query = "  SELECT   " +
                                        "  EXTRACT(YEAR FROM order_date) as year,   " +
                                        "  EXTRACT(MONTH FROM order_date) as month,distributor_account_no, publication_ID,   "
                                        +
                                        "  SUM(total_cost) AS order_value,   " +
                                        "  SUM(number_of_copies) AS total_copies  " +
                                        "  FROM Orders  " +
                                        "  GROUP BY EXTRACT(YEAR FROM order_date), EXTRACT(MONTH FROM order_date), distributor_account_no, publication_ID;  ";
                        copiesPerDistributorPerMonthlyReportQuery = connection.prepareStatement(query);

                        query = "  SELECT   " +
                                        "  EXTRACT(YEAR FROM payment_date) AS year,   " +
                                        "  EXTRACT(MONTH FROM payment_date) AS month,    " +
                                        "  SUM(amount_paid) AS revenue   " +
                                        "  FROM DistributorPayments  " +
                                        "  GROUP BY EXTRACT(YEAR FROM payment_date), EXTRACT(MONTH FROM payment_date); ";
                        monthlyTotalRevenueReportQuery = connection.prepareStatement(query);

                        query = "  SELECT  " +
                                        "  year,   " +
                                        "  month,    " +
                                        "  SUM(expenses) AS expenses   " +
                                        "  FROM(  " +
                                        "  SELECT   " +
                                        "  EXTRACT(YEAR FROM salary_date) AS year,   " +
                                        "  EXTRACT(MONTH FROM salary_date) AS month,  " +
                                        "  SUM(payment_amount) AS expenses   " +
                                        "  FROM Payments  " +
                                        "  GROUP BY EXTRACT(YEAR FROM salary_date),   " +
                                        "  EXTRACT(MONTH FROM salary_date)  " +
                                        "  UNION  " +
                                        "  SELECT   " +
                                        "  EXTRACT(YEAR FROM order_delivery_date) AS year,  " +
                                        "  EXTRACT(MONTH FROM order_delivery_date) AS month,  " +
                                        "  SUM(shipping_cost) AS expenses   " +
                                        "  FROM Orders  " +
                                        "  GROUP BY EXTRACT(YEAR FROM order_delivery_date),  " +
                                        "  EXTRACT(MONTH FROM order_delivery_date)  " +
                                        "  ) AS Expenses  " +
                                        "  GROUP BY year, month;";
                        monthlyTotalExpenseReportQuery = connection.prepareStatement(query);

                        query = "SELECT COUNT(account_number) AS total_distributors FROM Distributors;";
                        currentTotalDistributorsReportQuery = connection.prepareStatement(query);

                        query = "SELECT city AS distributor_city, sum(amount_paid) as revenue " +
                                        "FROM " +
                                        "Distributors D NATURAL JOIN DistributorPayments DP " +
                                        "GROUP BY city;";
                        totalRevenuePerCityReportQuery = connection.prepareStatement(query);

                        query = "  SELECT   " +
                                        "  account_number AS distributor_account_no,   " +
                                        "  sum(amount_paid) as revenue  " +
                                        "  FROM DistributorPayments  " +
                                        "  GROUP BY account_number;";
                        totalRevenuePerDistibutorReportQuery = connection.prepareStatement(query);

                        query = "  SELECT   " +
                                        "  city,   " +
                                        "  street_address,   " +
                                        "  sum(amount_paid) as revenue  " +
                                        "  FROM Distributors D NATURAL JOIN DistributorPayments DP  " +
                                        "  GROUP BY city, street_address;  ";
                        totalRevenuePerLocationReportQuery = connection.prepareStatement(query);

                        query = "  SELECT   " +
                                        "  S.role AS staff_role,   " +
                                        "  A.type AS staff_type,   " +
                                        "  SUM(payment_amount) AS total_payments  " +
                                        "  FROM Staff S NATURAL JOIN Authors A   " +
                                        "  NATURAL JOIN Payments SP   " +
                                        "  WHERE salary_date >= ? AND   " +
                                        "  salary_date <= ? " +
                                        "  GROUP BY S.role , A.type  " +
                                        "  UNION  " +
                                        "  SELECT   " +
                                        "  S.role AS staff_role,   " +
                                        "  E.type AS staff_type,   " +
                                        "  SUM(payment_amount) AS total_payments  " +
                                        "  FROM Staff S NATURAL JOIN Editors E   " +
                                        "  NATURAL JOIN Payments SP   " +
                                        "  WHERE salary_date >= ? AND   " +
                                        "  salary_date <= ?  " +
                                        "  GROUP BY S.role , E.type;  ";
                        totalStaffPaymentsPerPeriodPerWorkTypeReportQuery = connection.prepareStatement(query);

                        // Display Queries
                        query = "SELECT * FROM Publications;";
                        showPublications = connection.prepareStatement(query);

                        query = "SELECT * FROM Staff;";
                        showStaff = connection.prepareStatement(query);

                        query = "SELECT * FROM Books;";
                        showBooks = connection.prepareStatement(query);

                        query = "SELECT * FROM Orders;";
                        showOrders = connection.prepareStatement(query);

                        query = "SELECT * FROM Edits;";
                        showEdits = connection.prepareStatement(query);

                        query = "SELECT * FROM Payments;";
                        showPayments = connection.prepareStatement(query);

                        query = "SELECT * FROM Distributors;";
                        showDistributors = connection.prepareStatement(query);

                        query = "SELECT * FROM DistributorPayments;";
                        showDistributorPayments = connection.prepareStatement(query);

                        query = "SELECT * FROM Staff NATURAL JOIN Editors ;";
                        showEditors = connection.prepareStatement(query);

                        query = "SELECT * FROM Staff NATURAL JOIN Authors;";
                        showAuthors = connection.prepareStatement(query);

                        query = "SELECT * FROM Periodicals;";
                        showPeriodicals = connection.prepareStatement(query);

                        query = "SELECT * FROM Chapters;";
                        showChapters = connection.prepareStatement(query);

                        query = "SELECT * FROM Articles;";
                        showArticles = connection.prepareStatement(query);

                        query = "SELECT * FROM WritesBook;";
                        showWritesBooks = connection.prepareStatement(query);

                        query = "SELECT * FROM WritesArticles;";
                        showWritesArticles = connection.prepareStatement(query);

                        query = "SELECT * FROM Staff NATURAL JOIN Authors WHERE staff_ID = ?;";
                        showAuthorByID = connection.prepareStatement(query);

                        query = "SELECT * FROM Staff NATURAL JOIN Editors WHERE staff_ID = ?;";
                        showEditorByID = connection.prepareStatement(query);

                        query = "SELECT * FROM Edits NATURAL JOIN Publications WHERE staff_ID = ?;";
                        findPublicationByEditorQuery = connection.prepareStatement(query);

                        query = "SELECT * FROM WritesBook NATURAL JOIN Books WHERE staff_ID = ?;";
                        findBookByAuthorQuery = connection.prepareStatement(query);

                        query = "SELECT * FROM WritesArticles NATURAL JOIN Articles WHERE staff_ID = ?;";
                        findArticleByAuthorQuery = connection.prepareStatement(query);

                        query = "SELECT * FROM Staff NATURAL JOIN Payments WHERE staff_ID = ?;";
                        showPaymentsForStaff = connection.prepareStatement(query);

                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Error Executing Query");
                }
        }

        // Assign values to prepared statements of Delete Payment query.
        public static void deletePayment(int staff_ID, String salary_date) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deletePaymentQuery.setInt(1, staff_ID);
                                deletePaymentQuery.setString(2, salary_date);
                                deletePaymentQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        // Assign values to prepared statements of Delete Distributor query.
        public static void deleteDistributor(int account_number) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteDistributorQuery.setInt(1, account_number);
                                deleteDistributorQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        // Assign values to prepared statements of update queries of Distributors
        // details.
        public static void updateDistributor(int account_number, String newValue, String option) {
                try {

                        connection.setAutoCommit(false);
                        try {
                                switch (option) {
                                        case "1":
                                                updateDistributorAccountNumberQuery.setInt(1,
                                                                Integer.parseInt(newValue));
                                                updateDistributorAccountNumberQuery.setInt(2, account_number);
                                                updateDistributorAccountNumberQuery.executeUpdate();
                                                break;

                                        case "2":
                                                if (newValue.length() == 0) {
                                                        updateDistributorPhoneQuery.setNull(1, Types.NULL);
                                                } else {
                                                        updateDistributorPhoneQuery.setString(1, newValue);
                                                }
                                                updateDistributorPhoneQuery.setInt(2, account_number);
                                                updateDistributorPhoneQuery.executeUpdate();
                                                break;

                                        case "3":
                                                if (newValue.length() == 0) {
                                                        updateDistributorCityQuery.setNull(1, Types.NULL);
                                                } else {
                                                        updateDistributorCityQuery.setString(1, newValue);
                                                }
                                                updateDistributorCityQuery.setInt(2, account_number);
                                                updateDistributorCityQuery.executeUpdate();
                                                break;

                                        case "4":
                                                if (newValue.length() == 0) {
                                                        updateDistributorStreetAddressQuery.setNull(1, Types.NULL);
                                                } else {
                                                        updateDistributorStreetAddressQuery.setString(1, newValue);
                                                }
                                                updateDistributorStreetAddressQuery.setInt(2, account_number);
                                                updateDistributorStreetAddressQuery.executeUpdate();
                                                break;
                                        case "5":
                                                if (newValue.length() == 0) {
                                                        updateDistributorTypeQuery.setNull(1, Types.NULL);
                                                } else {
                                                        updateDistributorTypeQuery.setString(1, newValue);
                                                }
                                                updateDistributorTypeQuery.setInt(2, account_number);
                                                updateDistributorTypeQuery.executeUpdate();
                                                break;
                                        case "6":
                                                if (newValue.length() == 0) {
                                                        updateDistributorNameQuery.setNull(1, Types.NULL);
                                                } else {
                                                        updateDistributorNameQuery.setString(1, newValue);
                                                }
                                                updateDistributorNameQuery.setInt(2, account_number);
                                                updateDistributorNameQuery.executeUpdate();
                                                break;
                                        case "7":
                                                updateDistributorBalanceQuery.setInt(1, Integer.parseInt(newValue));
                                                updateDistributorBalanceQuery.setInt(2, account_number);
                                                updateDistributorBalanceQuery.executeUpdate();
                                                break;
                                        case "8":
                                                if (newValue.length() == 0) {
                                                        updateDistributorContactPersonQuery.setNull(1, Types.NULL);
                                                } else {
                                                        updateDistributorContactPersonQuery.setString(1, newValue);
                                                }
                                                updateDistributorContactPersonQuery.setInt(2, account_number);
                                                updateDistributorContactPersonQuery.executeUpdate();
                                                break;

                                        default:
                                                System.out.println("Cannot perform the update operation");
                                                break;

                                }

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        // Assign values to prepared statements of update queries of Distributors
        // details.
        public static void insertPublication(int publicationID, String title, String topic, String type, Double price) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertPublicationQuery.setInt(1, publicationID);
                                insertPublicationQuery.setString(2, title);
                                if (topic.length() == 0) {
                                        insertPublicationQuery.setNull(3, Types.NULL);
                                } else {
                                        insertPublicationQuery.setString(3, topic);

                                }

                                insertPublicationQuery.setString(3, topic);
                                insertPublicationQuery.setString(4, type);
                                insertPublicationQuery.setDouble(5, price);
                                insertPublicationQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        // Assign values to prepared statements of update queries of Publication
        // details.
        public static void updatePublication(int publicationID, String option, String newValue) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                switch (option) {
                                        case "1":
                                                updatePublicationTitleQuery.setString(1, newValue);
                                                updatePublicationTitleQuery.setInt(2, publicationID);
                                                updatePublicationTitleQuery.executeUpdate();

                                                break;

                                        case "2":
                                                updatePublicationTopicQuery.setString(1, newValue);
                                                updatePublicationTopicQuery.setInt(2, publicationID);
                                                updatePublicationTopicQuery.executeUpdate();
                                                break;

                                        case "3":
                                                updatePublicationTypeQuery.setString(1, newValue);
                                                updatePublicationTypeQuery.setInt(2, publicationID);
                                                updatePublicationTypeQuery.executeUpdate();
                                                break;

                                        case "4":
                                                updatePublicationPriceQuery.setString(1, newValue);
                                                updatePublicationPriceQuery.setInt(2, publicationID);
                                                updatePublicationPriceQuery.executeUpdate();
                                                break;

                                        default:
                                                System.out.println("Cannot perform the update operation");
                                                break;

                                }

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        // Take inputs for Addition of new book
        public static void insertBookMenu() {
                System.out.println("Enter publicationID");
                int pid = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter ISBN");
                int isbn = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter book edition");
                int edition = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter publication date in the format YYYY-DD-MM");
                String publication_date = scanner.nextLine();
                System.out.println("Enter title");
                String title = scanner.nextLine();
                System.out.println("Enter book price in double format");
                double price = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Enter topic");
                String topic = scanner.nextLine();
                System.out.println("Enter type");
                String type = scanner.nextLine();

                insertBook(pid, title, topic, type, price, isbn, edition, publication_date);
        }

        // Transaction to insert book into database
        public static void insertBook(int publicationID, String title, String topic, String type, Double price,
                        int isbn, int edition, String publicationDate) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                // first we insert a publication table
                                insertPublicationQuery.setInt(1, publicationID);

                                if (title.length() == 0) {
                                        insertPublicationQuery.setNull(2, Types.NULL);
                                } else {
                                        insertPublicationQuery.setString(2, title);
                                }
                                if (topic.length() == 0) {
                                        insertPublicationQuery.setNull(3, Types.NULL);
                                } else {
                                        insertPublicationQuery.setString(3, topic);
                                }
                                insertPublicationQuery.setString(4, type);
                                insertPublicationQuery.setDouble(5, price);
                                insertPublicationQuery.executeUpdate();

                                // then we insert into books table
                                insertBookQuery.setInt(1, publicationID);
                                insertBookQuery.setInt(2, isbn);
                                insertBookQuery.setInt(3, edition);
                                insertBookQuery.setString(4, publicationDate);
                                insertBookQuery.executeUpdate();

                                // we commit the transaction, if and only if both insertions are successful
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        // Transaction to delete book into database
        public static void deleteBook(int publicationID) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteBookQuery.setInt(1, publicationID);
                                deleteBookQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                connection.rollback();
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        // Menu to display update operations for a book
        public static void updateBookMenu() {
                System.out.println("1.  Update book edition");
                System.out.println("2.  Update book ISBN");
                System.out.println("3.  update the publication date of a book (Date format YYYY-DD-MM).");
                System.out.println("4.  update the text of a chapter");
                System.out.println("5.  update chapter title");
                String option = scanner.next();
                String value;
                int pid;
                String title;
                try {
                        connection.setAutoCommit(false);
                        try {
                                switch (option) {
                                        case "1":
                                                System.out.println("Enter publication ID of the book");
                                                pid = scanner.nextInt();
                                                scanner.nextLine();
                                                updateBookEditionQuery.setInt(2, pid);
                                                System.out.println("Enter new book edition");
                                                value = scanner.next();
                                                updateBookEditionQuery.setString(1, value);
                                                updateBookEditionQuery.executeUpdate();
                                                break;

                                        case "2":
                                                System.out.println("Enter the ISBN value");
                                                value = scanner.next();
                                                updateBookISBNQuery.setInt(1, Integer.parseInt(value));
                                                System.out.println("Enter publication ID of the book");
                                                pid = scanner.nextInt();
                                                scanner.nextLine();
                                                updateBookISBNQuery.setInt(2, pid);
                                                updateBookISBNQuery.executeUpdate();
                                                break;

                                        case "3":
                                                System.out.println("Enter the new publication date");
                                                value = scanner.nextLine();
                                                updateBookPublicationDateQuery.setString(1, value);
                                                System.out.println("Enter publication ID of the book");
                                                pid = scanner.nextInt();
                                                scanner.nextLine();
                                                updateBookPublicationDateQuery.setInt(2, pid);
                                                updateBookPublicationDateQuery.executeUpdate();
                                                break;
                                        case "4":
                                                System.out.println("Enter new text");
                                                value = scanner.next();
                                                updateChapterTextQuery.setString(1, value);
                                                System.out.println("Enter publication ID of the book");
                                                pid = scanner.nextInt();
                                                scanner.nextLine();
                                                updateChapterTextQuery.setInt(2, pid);
                                                System.out.println("Enter the title of the chapter");
                                                title = scanner.next();
                                                updateChapterTextQuery.setString(3, title);
                                                updateChapterTextQuery.executeUpdate();
                                                break;
                                        case "5":
                                                System.out.println("Enter new title");
                                                value = scanner.next();
                                                updateChapterTitleQuery.setString(1, value);
                                                System.out.println("Enter publication ID of the book");
                                                pid = scanner.nextInt();
                                                scanner.nextLine();
                                                updateChapterTitleQuery.setInt(2, pid);
                                                System.out.println("Enter the current title of the book");
                                                title = scanner.nextLine();
                                                updateChapterTitleQuery.setString(3, title);
                                                updateChapterTitleQuery.executeUpdate();
                                                break;

                                        default:
                                                System.out.println("Cannot perform the update operation");
                                                break;

                                }

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Give Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        // Transaction to insert into periodical into database
        public static void insertPeriodical(int publicationID, String issueDate, String topic, String periodicity,
                        String title, String type, Double price) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                // first insert into publication
                                insertPublicationQuery.setInt(1, publicationID);
                                insertPublicationQuery.setString(2, title);
                                insertPublicationQuery.setString(3, topic);
                                insertPublicationQuery.setString(4, type);
                                insertPublicationQuery.setDouble(5, price);
                                insertPublicationQuery.executeUpdate();

                                // then insert into periodical
                                insertPeriodicalQuery.setInt(1, publicationID);
                                insertPeriodicalQuery.setString(2, issueDate);
                                insertPeriodicalQuery.setString(3, periodicity);
                                insertPeriodicalQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        // Transaction to update periodical details
        public static void updatePeriodical(int publicationID, String option, String newValue) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                switch (option) {
                                        case "1":
                                                updatePeriodicalIssueDateQuery.setString(1, newValue);
                                                updatePeriodicalIssueDateQuery.setInt(2, publicationID);
                                                updatePeriodicalIssueDateQuery.executeUpdate();
                                                break;

                                        case "2":
                                                updatePeriodicalPeriodicityQuery.setString(1, newValue);
                                                updatePeriodicalPeriodicityQuery.setInt(2, publicationID);
                                                updatePeriodicalPeriodicityQuery.executeUpdate();
                                                break;

                                        default:
                                                System.out.println("Cannot perform the update operation");
                                                break;

                                }

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        // Transaction to delete a periodical from database
        public static void deletePeriodical(int publicationID) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deletePeriodicalQuery.setInt(1, publicationID);
                                deletePeriodicalQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        // Transaction to insert new distributor into database
        public static void insertDistributor(int accountNumber, String phone, String city, String streetAddress,
                        String type, String name, Double balance, String contactPerson) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertDistributorQuery.setInt(1, accountNumber);
                                insertDistributorQuery.setString(2, phone);
                                insertDistributorQuery.setString(3, city);
                                insertDistributorQuery.setString(4, streetAddress);
                                insertDistributorQuery.setString(5, type);
                                insertDistributorQuery.setString(6, name);
                                insertDistributorQuery.setDouble(7, balance);
                                insertDistributorQuery.setString(8, contactPerson);
                                insertDistributorQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        // Transaction to delete a chapter from database
        public static void deleteChapter(int publicationID, String title) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteChaptersQuery.setInt(1, publicationID);
                                deleteChaptersQuery.setString(2, title);
                                deleteChaptersQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        // Transaction to insert a chapter into database
        public static void insertChapter(int publicationID, String title, String text) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertChapterQuery.setInt(1, publicationID);
                                insertChapterQuery.setString(2, title);
                                insertChapterQuery.setString(3, text);
                                insertChapterQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        // Transaction to update a chapter into database
        public static void updateChapter(int publicationID, String title, String option, String newValue) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                switch (option) {
                                        case "1":
                                                updateChapterTitleQuery.setString(1, newValue);
                                                updateChapterTitleQuery.setInt(2, publicationID);
                                                updateChapterTitleQuery.setString(3, title);
                                                updateChapterTitleQuery.executeUpdate();
                                                break;

                                        case "2":
                                                updateChapterTextQuery.setString(1, newValue);
                                                updateChapterTextQuery.setInt(2, publicationID);
                                                updateChapterTextQuery.setString(3, title);
                                                updateChapterTextQuery.executeUpdate();
                                                break;

                                        default:
                                                System.out.println("Cannot perform the update operation");
                                                break;

                                }

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        // Transaction to insert an Article into database
        public static void insertArticle(int publicationID, String title, String text, String creationDate) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertArticleQuery.setInt(1, publicationID);
                                insertArticleQuery.setString(2, title);
                                insertArticleQuery.setString(3, text);
                                insertArticleQuery.setString(4, creationDate);
                                insertArticleQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        // Transaction to delete an Article into database
        public static void deleteArticle(int publicationID, String title) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteArticleQuery.setInt(1, publicationID);
                                deleteArticleQuery.setString(2, title);
                                deleteArticleQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        // Menu to display operations on Article Update

        public static void updateArticleMenu() {
                String title, text, creation_date, val;
                int pid;
                System.out.println("1. Update article title");
                System.out.println("2. Update article text");
                System.out.println("3. Update article creation date");
                String option = scanner.next();
                scanner.nextLine();
                switch (option) {
                        case "1":
                                System.out.println("Enter new article title");
                                val = scanner.nextLine();
                                System.out.println("Enter article publication ID");
                                pid = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Enter article title");
                                title = scanner.nextLine();
                                updateArticle(pid, title, "1", val);
                                break;
                        case "2":
                                System.out.println("Enter updated article text");
                                val = scanner.nextLine();
                                System.out.println("Enter article publication ID");
                                pid = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Enter article title");
                                title = scanner.nextLine();
                                updateArticle(pid, title, "2", val);
                                break;
                        case "3":
                                System.out.println("Enter creation date in YYYY-DD-MM");
                                val = scanner.nextLine();
                                System.out.println("Enter article publication ID");
                                pid = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Enter article title");
                                title = scanner.nextLine();
                                updateArticle(pid, title, "3", val);
                                break;
                        case "4":
                                return;
                        case "5":
                                System.exit(0);
                                break;
                        default:
                                System.out.println("Cannot perform the update operation");
                                break;

                }

        }

        // Transaction to update details of an Article into database
        public static void updateArticle(int publicationID, String title, String option, String newValue) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                switch (option) {

                                        case "1":
                                                updateArticleTitleQuery.setString(1, newValue);
                                                updateArticleTitleQuery.setInt(2, publicationID);
                                                updateArticleTitleQuery.setString(3, title);
                                                updateArticleTitleQuery.executeUpdate();
                                                break;

                                        case "2":
                                                updateArticleTextQuery.setString(1, newValue);
                                                updateArticleTextQuery.setInt(2, publicationID);
                                                updateArticleTextQuery.setString(3, title);
                                                updateArticleTextQuery.executeUpdate();
                                                break;

                                        case "3":
                                                updateArticleCreationDateQuery.setString(1, newValue);
                                                updateArticleCreationDateQuery.setInt(2, publicationID);
                                                updateArticleCreationDateQuery.setString(3, title);
                                                updateBookEditionQuery.executeUpdate();
                                                break;

                                        default:
                                                System.out.println("Cannot perform the update operation");
                                                break;

                                }

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        // Transaction to Assign an Editor to a publication
        public static void editorAssignment(int staffID, int publicationID) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                editorAssignmentQuery.setInt(1, staffID);
                                editorAssignmentQuery.setInt(2, publicationID);
                                editorAssignmentQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        // Transaction to add an order
        public static void insertOrder(int order_number, int publication_ID, int distributor_account_no,
                        String order_date, String order_delivery_date, int number_of_copies, double total_cost,
                        double shipping_cost) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertOrderQuery.setInt(1, order_number);
                                insertOrderQuery.setInt(2, publication_ID);
                                insertOrderQuery.setInt(3, distributor_account_no);
                                insertOrderQuery.setString(4, order_date);
                                insertOrderQuery.setString(5, order_delivery_date);
                                insertOrderQuery.setInt(6, number_of_copies);
                                insertOrderQuery.setDouble(7, total_cost);
                                insertOrderQuery.setDouble(8, shipping_cost);
                                insertOrderQuery.executeUpdate();

                                // Once each order is successfully placed, we need to generate the bill
                                // as the outstanding balance gets updated each time an order is placed
                                generateBillQuery.setInt(1, order_number);
                                generateBillQuery.executeUpdate();

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        // Transaction to update order details
        public void updateOrder(String newValue, String option, int order_number) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                switch (option) {

                                        case "1":
                                                updateOrderDateQuery.setString(1, newValue);
                                                updateOrderDateQuery.setInt(2, order_number);
                                                updateOrderDateQuery.executeUpdate();
                                                break;
                                        case "2":
                                                updateOrderDeliveryDateQuery.setString(1, newValue);
                                                updateOrderDeliveryDateQuery.setInt(2, order_number);
                                                updateOrderDeliveryDateQuery.executeUpdate();
                                                break;
                                        case "3":
                                                updateOrderNumberOfCopiesQuery.setInt(1, Integer.parseInt(newValue));
                                                updateOrderNumberOfCopiesQuery.setInt(2, order_number);
                                                updateOrderNumberOfCopiesQuery.executeUpdate();
                                                break;
                                        case "4":
                                                updateOrderTotalCostQuery.setDouble(1, Double.parseDouble(newValue));
                                                updateOrderTotalCostQuery.setInt(2, order_number);
                                                updateOrderTotalCostQuery.executeUpdate();
                                                break;
                                        case "5":
                                                updateOrderShippingCostQuery.setDouble(1, Double.parseDouble(newValue));
                                                updateOrderShippingCostQuery.setInt(2, order_number);
                                                updateOrderShippingCostQuery.executeUpdate();
                                                break;
                                        default:
                                                System.out.println("Cannot perform the update operation");
                                                break;
                                }
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        // Transaction to delete an order.

        public static void deleteOrder(int order_number) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteOrderQuery.setInt(1, order_number);
                                deleteOrderQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        // Transaction to insert payments
        public static void insertPayment_sql(int staff_ID, String salary_date, double payment_amount,
                        String collection_date) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertPaymentQuery.setInt(1, staff_ID);
                                insertPaymentQuery.setString(2, salary_date);
                                insertPaymentQuery.setDouble(3, payment_amount);
                                if (collection_date.length() == 0) {
                                        insertPaymentQuery.setNull(4, Types.NULL);
                                } else {
                                        insertPaymentQuery.setString(4, collection_date);
                                }
                                insertPaymentQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        // Input Payment details to add a payment to a staff
        public static void insertPayment() {
                try {
                        System.out.print("\n Enter the details of the Payment to staff\n");
                        System.out.print("\n Staff ID\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Enter Date the salary was posted (YYYY-MM-DD)\n");
                        String salary_date = scanner.nextLine();
                        System.out.print("\n Enter payment amount \n");
                        Double payment_amount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print(
                                        "\n Enter Date the salary was collected (press enter if unknown) (YYYY-MM-DD)\n");
                        String collection_date = scanner.nextLine();
                        insertPayment_sql(staff_ID, salary_date, payment_amount, collection_date);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        // Transaction to update collections of date of payment
        public static void updateCollectionDateOfPayment_sql(String collection_date, int staff_ID,
                        String salary_date) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                updatePaymentCollectionDateQuery.setString(1, collection_date);
                                updatePaymentCollectionDateQuery.setInt(2, staff_ID);
                                updatePaymentCollectionDateQuery.setString(3, salary_date);
                                updatePaymentCollectionDateQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        // Input to update collections of date of payment
        public static void updateCollectionDateOfPayment() {
                try {
                        System.out.print("\n Enter the details of the Payment to staff to be updated\n");
                        System.out.print("\n Staff ID\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Enter the Date the salary was posted (YYYY-MM-DD)\n");
                        String salary_date = scanner.nextLine();
                        System.out.print("\n Enter the Date the salary was collected to be updated (YYYY-MM-DD)\n");
                        String collection_date = scanner.nextLine();
                        updateCollectionDateOfPayment_sql(collection_date, staff_ID, salary_date);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void clearConsoleScreen() {
                try {
                        final String os = System.getProperty("os.name");

                        if (os.contains("Windows")) {
                                Runtime.getRuntime().exec("cls");
                        } else {
                                Runtime.getRuntime().exec("clear");
                        }
                } catch (final Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                        // Handle any exceptions.
                }
                // System.out.print("Everything on the console will cleared");
                // System.out.print("\033[H\033[2J");
                // System.out.flush();
        }

        // Displays Author's Table
        public static void displayAllAuthors() {
                try {
                        result = showAuthors.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Editors exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        // Displays Distributors Table
        public static void displayAllDistributors() {
                try {
                        result = showAllDistributorsQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Distributors exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        // Displays Editor's Table
        public static void displayAllEditors() {
                try {
                        result = showEditors.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Editors exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        // Display Staff Table
        public static void displayAllStaff() {
                try {
                        result = showStaff.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Editors exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        public static void addAuthor_sql(int staff_ID, String type, String name, String role, String phone_number,
                        int age, String gender, String email, String Address) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                addStaffQuery.setInt(1, staff_ID);
                                addStaffQuery.setString(2, name);
                                addStaffQuery.setString(3, role);
                                addStaffQuery.setString(4, phone_number);
                                addStaffQuery.setInt(5, age);
                                addStaffQuery.setString(6, gender);
                                addStaffQuery.setString(7, email);
                                addStaffQuery.setString(8, Address);
                                addStaffQuery.executeUpdate();

                                addAuthorQuery.setInt(1, staff_ID);
                                addAuthorQuery.setString(2, type);
                                addAuthorQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        public static void addEditor_sql(int staff_ID, String type, String name, String role, String phone_number,
                        int age, String gender, String email, String Address) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                addStaffQuery.setInt(1, staff_ID);
                                addStaffQuery.setString(2, name);
                                addStaffQuery.setString(3, role);
                                addStaffQuery.setString(4, phone_number);
                                addStaffQuery.setInt(5, age);
                                addStaffQuery.setString(6, gender);
                                addStaffQuery.setString(7, email);
                                addStaffQuery.setString(8, Address);
                                addStaffQuery.executeUpdate();

                                addEditorQuery.setInt(1, staff_ID);
                                addEditorQuery.setString(2, type);
                                addEditorQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        public static void addAuthor() {
                try {
                        System.out.print("\n Enter the details of the Author\n");
                        System.out.print("\n Staff ID of the Author\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Type of Author Staff/Invited \n");
                        String type = scanner.nextLine();
                        String role = "Author";
                        System.out.print("\n name of Author \n");
                        String name = scanner.nextLine();
                        System.out.print("\n phone_number of Author \n");
                        String phone_number = scanner.nextLine();
                        System.out.print("\n age of Author \n");
                        int age = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n gender of Author M/F \n");
                        String gender = scanner.nextLine();
                        System.out.print("\n email  of Author \n");
                        String email = scanner.nextLine();
                        System.out.print("\n Address  of Author \n");
                        String Address = scanner.nextLine();
                        addAuthor_sql(staff_ID, type, name, role, phone_number, age, gender, email, Address);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void addEdits_sql(int staff_ID, int publication_ID) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                addEditsQuery.setInt(1, staff_ID);
                                addEditsQuery.setInt(2, publication_ID);
                                addEditsQuery.executeUpdate();

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        public static void deleteEdits_sql(int staff_ID, int publication_ID) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteEditsQuery.setInt(1, staff_ID);
                                deleteEditsQuery.setInt(2, publication_ID);
                                deleteEditsQuery.executeUpdate();

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        public static void addEditor() {
                try {
                        System.out.print("\n Enter the details of the Editor\n");
                        System.out.print("\n Staff ID of the Editor\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Type of Editor Staff/Invited \n");
                        String type = scanner.nextLine();
                        String role = "Editor";
                        System.out.print("\n name of Editor \n");
                        String name = scanner.nextLine();
                        System.out.print("\n phone_number of Editor \n");
                        String phone_number = scanner.nextLine();
                        System.out.print("\n age of Editor \n");
                        int age = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n gender of Editor M/F \n");
                        String gender = scanner.nextLine();
                        System.out.print("\n email  of Editor \n");
                        String email = scanner.nextLine();
                        System.out.print("\n Address  of Editor \n");
                        String Address = scanner.nextLine();
                        addEditor_sql(staff_ID, type, name, role, phone_number, age, gender, email, Address);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void addEdits() {
                try {
                        System.out.print("\n Enter the details of the Editor and Publication that is being edited\n");
                        System.out.print("\n Staff ID of the Editor\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Publication ID of the Editor\n");
                        int publication_ID = scanner.nextInt();
                        scanner.nextLine();
                        addEdits_sql(staff_ID, publication_ID);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void deleteEdits() {
                try {
                        System.out.print(
                                        "\n Enter the details of the Editor and Publication that is being edited to be delete\n");
                        System.out.print("\n Staff ID of the Editor\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Publication ID of the Editor\n");
                        int publication_ID = scanner.nextInt();
                        scanner.nextLine();
                        deleteEdits_sql(staff_ID, publication_ID);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void addWritesBook_sql(int staff_ID, int publication_ID) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                addWritesBookQuery.setInt(1, staff_ID);
                                addWritesBookQuery.setInt(2, publication_ID);
                                addWritesBookQuery.executeUpdate();

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        public static void deleteWritesBook_sql(int staff_ID, int publication_ID) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteWritesBookQuery.setInt(1, staff_ID);
                                deleteWritesBookQuery.setInt(2, publication_ID);
                                deleteWritesBookQuery.executeUpdate();

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        public static void addWritesBook() {
                try {
                        System.out.print("\n Enter the details of the Author and Publication that is being written\n");
                        System.out.print("\n Staff ID of the Author\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Publication ID of the Author\n");
                        int publication_ID = scanner.nextInt();
                        scanner.nextLine();
                        addWritesBook_sql(staff_ID, publication_ID);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void deleteWritesBook() {
                try {
                        System.out.print(
                                        "\n Enter the details of the Author and Publication that is being writtn to be delete\n");
                        System.out.print("\n Staff ID of the Author\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Publication ID of the Author\n");
                        int publication_ID = scanner.nextInt();
                        scanner.nextLine();
                        deleteWritesBook_sql(staff_ID, publication_ID);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void addWritesArticles_sql(int staff_ID, int publication_ID, String title) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                addWritesArticlesQuery.setInt(1, staff_ID);
                                addWritesArticlesQuery.setInt(2, publication_ID);
                                addWritesArticlesQuery.setString(3, title);
                                addWritesArticlesQuery.executeUpdate();

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        public static void deleteWritesArticles_sql(int staff_ID, int publication_ID, String title) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteWritesArticlesQuery.setInt(1, staff_ID);
                                deleteWritesArticlesQuery.setInt(2, publication_ID);
                                deleteWritesArticlesQuery.setString(3, title);
                                deleteWritesArticlesQuery.executeUpdate();

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        public static void addWritesArticles() {
                try {
                        System.out.print("\n Enter the details of the Author and Publication that is being written\n");
                        System.out.print("\n Staff ID of the Author\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Publication ID of the Author\n");
                        int publication_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Title of the article\n");
                        String title = scanner.nextLine();
                        addWritesArticles_sql(staff_ID, publication_ID, title);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void deleteWritesArticles() {
                try {
                        System.out.print(
                                        "\n Enter the details of the Author and Publication that is being written to be delete\n");
                        System.out.print("\n Staff ID of the Author\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Publication ID of the Author\n");
                        int publication_ID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Title of the article\n");
                        String title = scanner.nextLine();
                        scanner.nextLine();
                        deleteWritesArticles_sql(staff_ID, publication_ID, title);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static ResultSet getEditorByID(int staff_ID) {
                try {
                        showEditorByID.setInt(1, staff_ID);
                        result = showEditorByID.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Editors exist");
                                return null;
                        }
                        result.first();
                        return result;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
                return null;
        }

        public static ResultSet getAuthorByID(int staff_ID) {
                try {
                        showAuthorByID.setInt(1, staff_ID);
                        result = showAuthorByID.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Authors exist");
                                return null;
                        }
                        result.first();
                        return result;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
                return null;
        }

        public static void showAuthorByID() {
                try {

                        System.out.print("\n Staff ID of the Author\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        result = getAuthorByID(staff_ID);
                        result.beforeFirst();
                        display_table(result);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void showEditorByID() {
                try {

                        System.out.print("\n Staff ID of the Editor\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        result = getEditorByID(staff_ID);
                        result.beforeFirst();
                        display_table(result);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static ResultSet findPublicationByEditor() {
                try {

                        System.out.print("\n Staff ID of the Editor\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();

                        findPublicationByEditorQuery.setInt(1, staff_ID);
                        result = findPublicationByEditorQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Publications is assigned to Editor");
                                return null;
                        }
                        result.beforeFirst();
                        display_table(result);
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
                return null;
        }

        public static ResultSet findBookByAuthor() {
                try {

                        System.out.print("\n Staff ID of the Author\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();

                        findBookByAuthorQuery.setInt(1, staff_ID);
                        result = findBookByAuthorQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No books for Authors exist");
                                return null;
                        }
                        result.beforeFirst();
                        display_table(result);
                } catch (Exception e) {
                        System.out.println("Failure");
                }
                return null;
        }

        public static ResultSet findArticleByAuthor() {
                try {

                        System.out.print("\n Staff ID of the Author\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();

                        findArticleByAuthorQuery.setInt(1, staff_ID);
                        result = findArticleByAuthorQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No articles for Authors exist");
                                return null;
                        }
                        result.beforeFirst();
                        display_table(result);
                } catch (Exception e) {
                        System.out.println("Failure");
                }
                return null;
        }

        public static void displayPaymentsForStaff() {
                try {

                        System.out.print("\n Enter the Staff ID for the payments to be displayed\n");
                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();

                        showPaymentsForStaff.setInt(1, staff_ID);
                        result = showPaymentsForStaff.executeQuery();
                        if (!result.next()) {
                                System.out.println("No payments exist for entered staff");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
                return;
        }

        public static void updateStaff_sql(int staff_ID, String name, String phone_number,
                        int age, String gender, String email, String Address) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                updateStaffQuery.setString(1, name);
                                updateStaffQuery.setString(2, phone_number);
                                updateStaffQuery.setInt(3, age);
                                updateStaffQuery.setString(4, gender);
                                updateStaffQuery.setString(5, email);
                                updateStaffQuery.setString(6, Address);
                                updateStaffQuery.setInt(7, staff_ID);
                                updateStaffQuery.executeUpdate();

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void getNewBookInputs() {
                try {
                        System.out.println("\n Enter PublicationID : ");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("\n Enter the details of the Book");
                        System.out.println("\n Book Title:");
                        String title = scanner.nextLine();
                        System.out.println("\n Book Topic:");
                        String topic = scanner.nextLine();
                        String type = "Book";
                        System.out.println("\n Book Price: ");
                        Double price = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("\n Book ISBN:");
                        int isbn = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("\n Book Edition:");
                        int edition = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("\n Book Publication Date in the format - YYYY-MM-DD ex:2022-03-02: ");
                        String publicationDate = scanner.nextLine();
                        insertBook(publicationID, title, topic, type, price, isbn, edition, publicationDate);
                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        public static void getNewPeriodicalInputs() {
                try {
                        int publicationID = generatePublicationID();
                        System.out.println("\n Enter the details of the Periodical");
                        System.out.println("\n Periodical Title :");
                        String title = scanner.nextLine();
                        System.out.println("\n Periodical Topic :");
                        String topic = scanner.nextLine();
                        String type = "Periodical";
                        System.out.println("\n Periodical Price :");
                        Double price = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("\n Periodical Issue Date in the format - YYYY-MM-DD ex:2022-03-02: ");
                        String issueDate = scanner.nextLine();
                        System.out.println("\n Periodical periodicity:");
                        String periodicity = scanner.nextLine();
                        insertPeriodical(publicationID, issueDate, topic, periodicity, title, type, price);

                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void getDeleteBookInputs() {
                try {
                        displayAllBooks();
                        System.out.println("\n Enter the Publication ID of the Book to be deleted:\n");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();

                        deleteBook(publicationID);
                        System.out.println("The Book is deleted successfully!");
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void getDeletePublicationInputs() {
                try {
                        System.out.println("\n Enter the Publication ID of the publication to be deleted:\n");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();

                        deleteBook(publicationID);
                        System.out.println("The Publication is deleted successfully!");
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void getDeleteChapterInputs() {
                try {
                        System.out.println("\n Enter the Publication ID of the Chapter to be deleted:\n");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("\n Enter the title of the chapter:\n");
                        String title = scanner.nextLine();
                        scanner.nextLine();

                        deleteChapter(publicationID, title);
                        System.out.println("The Chapter is deleted successfully!");
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void getDeleteArticleInputs() {
                try {
                        System.out.println("\n Enter the Publication ID of the Article to be deleted:\n");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("\n Enter the title of the Article:\n");
                        String title = scanner.nextLine();
                        scanner.nextLine();

                        deleteArticle(publicationID, title);
                        System.out.println("The Chapter is deleted successfully!");
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void getDeletePeriodicalInputs() {
                try {
                        displayAllPeriodicals();
                        System.out.println("\n Enter the Publication ID of the Periodical to be deleted:\n");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();

                        deletePeriodical(publicationID);
                        System.out.println("The Periodical is deleted successfully!");
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void displayAllBooks() {
                System.out.println("Books in the Database : ");
                try {
                        result = displayAllBooksQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Books exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();

                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void displayAllPeriodicals() {
                System.out.println("Periodicals in the Database : ");
                try {
                        result = displayAllPeriodicalsQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Periodicals exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();

                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void findByPID(int val) {
                try {
                        findBookByPublicationIdQuery.setInt(1, val);
                        result = findBookByPublicationIdQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No such books exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void displayChaptersInBook(int publicationID) {
                System.out.println("Chapters : ");
                try {
                        displayChaptersQuery.setInt(1, publicationID);
                        result = displayChaptersQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Chapters exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();

                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void findByISBN(int val) {
                try {

                        findBookByISBNdQuery.setInt(1, val);
                        result = findBookByISBNdQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No such books exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();

                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void displayArticlesInPeriodical(int publicationID) {
                System.out.println("Articles : ");
                try {
                        displayArticlesQuery.setInt(1, publicationID);
                        result = displayArticlesQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Articles exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();

                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void getNewChapterInputs() {
                try {
                        displayAllBooks();
                        System.out.print("\n Enter the Publication ID of the Book to add a new chapter\n");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Enter the Title for the Chapter \n");
                        String title = scanner.nextLine();

                        System.out.print("\n Enter the  Text to be added to the Chapter \n");
                        String text = scanner.nextLine();

                        insertChapter(publicationID, title, text);

                } catch (Exception e) {
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        public static void getNewArticleInputs() {
                try {
                        displayAllPeriodicals();
                        System.out.print("\n Enter the Publication ID of the Periodical to add a new Article:\n");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("\n Enter the Title for the Article : \n");
                        String title = scanner.nextLine();

                        System.out.print("\n Enter the  Text of the Article : \n");
                        String text = scanner.nextLine();

                        System.out.print("\n Enter the creation date in the format - YYYY-MM-DD ex:2022-03-02: \n");
                        String creationDate = scanner.nextLine();

                        insertArticle(publicationID, title, text, creationDate);

                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void getBookIDforChapters() {
                try {
                        displayAllBooks();
                        System.out.print("\n Enter the Publication ID of the Book to view chapters\n");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();

                        displayChaptersInBook(publicationID);

                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void getPeriodicalIDforArticles() {
                try {
                        displayAllPeriodicals();
                        System.out.print("\n Enter the Publication ID of the Periodical to view Articles\n");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();

                        displayArticlesInPeriodical(publicationID);

                }

                catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void findBooks() {
                System.out.println("1.  Find book by publication_ID");
                System.out.println("2.  Find book by ISBN");
                String response = scanner.next();
                int val;
                switch (response) {
                        case "1":
                                System.out.println("Enter publication ID");
                                val = scanner.nextInt();
                                scanner.nextLine();
                                findByPID(val);
                                break;
                        case "2":
                                System.out.println("Enter ISBN value");
                                val = scanner.nextInt();
                                scanner.nextLine();
                                findByISBN(val);
                                break;
                        case "3":
                                return;
                        case "4":
                                System.exit(0);
                                break;
                        default:
                                System.out.println("Please enter correct choice from above.");
                                break;
                }
        }

        public static void findPeriodicalByPID(int val) {
                try {
                        findPeriodicalByPIDQuery.setInt(1, val);
                        result = findPeriodicalByPIDQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No such periodical exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void findPeriodicals() {
                System.out.println("1.  Find periodical by publication_ID");
                String response = scanner.next();
                int val;
                switch (response) {
                        case "1":
                                System.out.println("Enter publication ID");
                                val = scanner.nextInt();
                                scanner.nextLine();
                                findPeriodicalByPID(val);
                                break;
                        case "2":
                                return;
                        case "3":
                                System.exit(0);
                                break;
                        default:
                                System.out.println("Please enter correct choice from above.");
                                break;
                }
        }

        public static void insertPeriodicalMenu() {
                try {
                        System.out.println("Enter publication ID");
                        int publicationID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter issue date");
                        String issue_date = scanner.nextLine();
                        System.out.println("Enter the title");
                        String title = scanner.nextLine();
                        System.out.println("Enter the type");
                        String type = scanner.nextLine();
                        System.out.println("Enter periodicity (weekly, monthly etc)");
                        String periodicity = scanner.nextLine();
                        System.out.println("Enter the price of the periodical");
                        Double price = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("Enter the topic");
                        String topic = scanner.nextLine();
                        insertPeriodical(publicationID, issue_date, topic, periodicity, title, type, price);
                } catch (Exception e) {
                        System.out.println("Error in input values");
                }

        }

        public static void updateStaff() {
                try {
                        System.out.print("\n Enter the details of the Author/Editor to be Updated\n");
                        System.out.print("\n Staff ID of the Author/Editor\n");

                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        result = getEditorByID(staff_ID);
                        if (result == null) {
                                System.out.println("Cant find Author/Editor");
                                return;
                        }
                        System.out.print("\n name of Author/Editor  (Press Enter If you dont want to change)\n");
                        String name = scanner.nextLine();
                        if (name.isEmpty()) {
                                name = result.getString("name");
                        }
                        System.out.print("\n phone_number of Author/Editor (Press Enter If you dont want to change)\n");
                        String phone_number = scanner.nextLine();
                        if (phone_number.isEmpty()) {
                                phone_number = result.getString("phone_number");
                        }
                        System.out.print("\n age of Author/Editor (Enter 0 If you dont want to change)\n");
                        int age = scanner.nextInt();
                        scanner.nextLine();
                        if (age == 0) {
                                age = result.getInt("age");
                        }
                        System.out.print("\n gender of Author/Editor M/F (Press Enter If you dont want to change)\n");
                        String gender = scanner.nextLine();
                        if (gender.isEmpty()) {
                                gender = result.getString("gender");
                        }
                        System.out.print("\n email  of Author/Editor (Press Enter If you dont want to change)\n");
                        String email = scanner.nextLine();
                        if (email.isEmpty()) {
                                email = result.getString("email");
                        }
                        System.out.print("\n Address  of Author/Editor (Press Enter If you dont want to change)\n");
                        String Address = scanner.nextLine();
                        if (Address.isEmpty()) {
                                Address = result.getString("Address");
                        }
                        updateStaff_sql(staff_ID, name, phone_number, age, gender, email, Address);
                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void deleteStaff() {
                try {
                        System.out.print("\n Enter the details of the Author/Editor to be Deleted\n");
                        System.out.print("\n Staff ID of the Author\n");

                        int staff_ID = scanner.nextInt();
                        scanner.nextLine();
                        result = getEditorByID(staff_ID);
                        if (result == null) {
                                System.out.println("Cant find Author/Editor");
                                return;
                        }
                        try {
                                connection.setAutoCommit(false);
                                try {
                                        deleteStaffQuery.setInt(1, staff_ID);
                                        deleteStaffQuery.executeUpdate();

                                        connection.commit();
                                } catch (SQLException e) {
                                        connection.rollback();
                                        System.out.println(e.getMessage());
                                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                                } finally {
                                        connection.setAutoCommit(true);
                                }
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        }
                } catch (Exception e) {
                        System.out.println("Failure. Unable to delete staff");
                        ;
                }
        }

        public static void displayPublicationsMenu() {
                while (true) {
                        clearConsoleScreen();
                        int publicationID;
                        String issue_date, periodicity, title, text, creationDate;
                        System.out.println("\nPublications Menu\n");
                        System.out.println("---------------BOOKS---------------");
                        System.out.println("1.  Add a new book edition");
                        System.out.println("2.  Update Book/Chapter");
                        System.out.println("3.  Find books");
                        System.out.println("4.  Delete a book");
                        System.out.println("5.  Add a chapter to a Book");
                        System.out.println("6.  Delete a chapter from a Book");
                        System.out.println("7.  Show all Books");
                        System.out.println("8.  Show all chapters for a Book");
                        System.out.println("---------------PERIODICALS---------------");
                        System.out.println("9.  Add new Periodical");
                        System.out.println("10.  Find a periodical");
                        System.out.println("11. Add an article to a Periodical");
                        System.out.println("12. Delete an article from a Periodical");
                        System.out.println("13. Delete a periodical");
                        System.out.println("14. Update Periodical/article");
                        System.out.println("15. Show all periodicals");
                        System.out.println("16. Show all articles for a periodical");
                        System.out.println("---------------MENU ACTIONS---------------");
                        System.out.println("17. Delete a Publication");
                        System.out.println("18. Go back to previous Menu");
                        System.out.println("19. Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        getNewBookInputs();
                                        break;
                                case "2":
                                        displayAllBooks();
                                        updateBookMenu();
                                        break;
                                case "3":
                                        findBooks();
                                        break;
                                case "4":
                                        displayAllBooks();
                                        getDeleteBookInputs();
                                        break;
                                case "5":
                                        getNewChapterInputs();
                                        break;
                                case "6":
                                        getDeleteChapterInputs();
                                        break;
                                case "7":
                                        displayAllBooks();
                                        break;
                                case "8":
                                        getBookIDforChapters();
                                        break;
                                case "9":
                                        insertPeriodicalMenu();
                                        break;
                                case "10":
                                        findPeriodicals();
                                        break;
                                case "11":
                                        getNewArticleInputs();
                                        break;
                                case "12":
                                        getDeleteArticleInputs();
                                        break;
                                case "13":
                                        getDeletePeriodicalInputs();
                                        break;
                                case "14":
                                        updatePeriodicalMenu();
                                        break;
                                case "15":
                                        displayAllPeriodicals();
                                        break;
                                case "16":
                                        getPeriodicalIDforArticles();
                                        break;
                                case "17":
                                        getDeletePublicationInputs();
                                case "18":
                                        return;
                                case "19":
                                        System.exit(0);
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }
                }

        }

        public static void updateDistributorInputs() {
                clearConsoleScreen();
                int account_number;
                String value;
                System.out.println("1.  Update distributor account number");
                System.out.println("2.  Update distributor phone");
                System.out.println("3.  update distributor city");
                System.out.println("4.  update distributor street address");
                System.out.println("5.  update distributor type");
                System.out.println("6.  update distributor name");
                System.out.println("7.  update distributor balance");
                System.out.println("8.  update distributor contact person");
                System.out.println("9.  Return to menu");
                System.out.println("10.  Exit");

                String response = scanner.nextLine();
                switch (response) {
                        case "1":
                                System.out.println("Enter account number that needs to be updated.");
                                account_number = scanner.nextInt();
                                System.out.println("Enter new account number");
                                value = scanner.next();
                                updateDistributor(account_number, value, "1");
                                break;
                        case "2":
                                System.out.println("Enter distributor account number.");
                                account_number = scanner.nextInt();
                                System.out.println("Enter distributor phone number");
                                value = scanner.next();
                                updateDistributor(account_number, value, "2");
                                break;
                        case "3":
                                System.out.println("Enter distributor account number.");
                                account_number = scanner.nextInt();
                                System.out.println("Enter distributor city");
                                value = scanner.next();
                                updateDistributor(account_number, value, "3");
                                break;
                        case "4":
                                System.out.println("Enter distributor account number.");
                                account_number = scanner.nextInt();
                                System.out.println("Enter distributor street address");
                                value = scanner.next();
                                updateDistributor(account_number, value, "4");
                                break;
                        case "5":
                                System.out.println("Enter distributor account number.");
                                account_number = scanner.nextInt();
                                System.out.println("Enter distributor type");
                                value = scanner.next();
                                updateDistributor(account_number, value, "5");
                                break;
                        case "6":
                                System.out.println("Enter distributor account number.");
                                account_number = scanner.nextInt();
                                System.out.println("Enter distributor name");
                                value = scanner.next();
                                updateDistributor(account_number, value, "6");
                                break;
                        case "7":
                                System.out.println("Enter distributor account number.");
                                account_number = scanner.nextInt();
                                System.out.println("Enter distributor balance");
                                value = scanner.next();
                                updateDistributor(account_number, value, "7");
                                break;
                        case "8":
                                System.out.println("Enter distributor account number");
                                account_number = scanner.nextInt();
                                System.out.println("Enter distributor contact person");
                                value = scanner.next();
                                updateDistributor(account_number, value, "8");
                                break;

                        case "9":
                                return;
                        case "10":
                                System.exit(0);
                                break;
                        default:
                                System.out.println("Please enter correct choice from above.");
                                break;

                }

        }

        public static void addDistributorInputs() {
                int account_number;
                String phone;
                String city;
                String street_address;
                String type;
                String name;
                double balance;
                String contact_person;
                System.out.println("Enter distributor account number");
                account_number = scanner.nextInt();
                System.out.println("Enter distributor phone");
                phone = scanner.next();
                System.out.println("Enter distributor city");
                city = scanner.next();
                System.out.println("Enter distributor street address");
                street_address = scanner.next();
                System.out.println("Enter distributor type");
                type = scanner.next();
                System.out.println("Enter distributor name");
                name = scanner.next();
                System.out.println("Enter distributor balance");
                balance = scanner.nextDouble();
                System.out.println("Enter distributor contact person");
                contact_person = scanner.next();
                insertDistributor(account_number, phone, city, street_address, type, name, balance, contact_person);
        }

        public static void insertDistributorPayment(int account_number, String payment_date, int amount_paid) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertDistributorPaymentQuery.setInt(1, account_number);
                                insertDistributorPaymentQuery.setString(2, payment_date);
                                insertDistributorPaymentQuery.setInt(3, amount_paid);
                                insertDistributorPaymentQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        public static void deleteDistributorInputs() {
                System.out.println("Enter distributor account number");
                int value = scanner.nextInt();
                deleteDistributor(value);
        }

        public static void placeOrder(int order_number, int pid, int account_number, String order_date,
                        String order_delivery_date, int number_of_copies, double total_cost, double shipping_cost) {
                insertOrder(order_number, pid, account_number, order_date, order_delivery_date, number_of_copies,
                                total_cost, shipping_cost);
        }

        public static void placeOrderInputs() {

                System.out.println("Enter Order ID:");
                int oid = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter publication ID for the order");
                int pid = scanner.nextInt();
                System.out.println("Enter distributor account number");
                int account_number = scanner.nextInt();
                System.out.println("Enter order date in the format YYYY-MM-DD");
                String order_date = scanner.next();
                System.out.println("Enter order delivery date in the format YYYY-MM-DD");
                String order_delivery_date = scanner.next();
                System.out.println("Enter number of copies");
                int number_of_copies = scanner.nextInt();
                System.out.println("Enter total cost");
                double total_cost = scanner.nextDouble();
                System.out.println("Enter shipping cost");
                double shipping_cost = scanner.nextDouble();
                placeOrder(oid, pid, account_number, order_date, order_delivery_date, number_of_copies, total_cost,
                                shipping_cost);

        }

        public static void generateBill() {
                try {
                        result = generateBillDisplay.executeQuery();
                        if (!result.next()) {
                                System.out.println("No Bills exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        public static void deduceBalance(int account_number, String payment_date) {
                try {
                        connection.setAutoCommit(false);
                        try {

                                deduceBalanceQuery.setInt(1, account_number);
                                deduceBalanceQuery.setString(2, payment_date);
                                deduceBalanceQuery.executeUpdate();
                                connection.commit();

                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        public static void receivePayment() {
                System.out.println("Enter account number of the distributor");
                int account_number = scanner.nextInt();
                System.out.println("Enter payment date in the format YYYY-DD-MM");
                String payment_date = scanner.next();
                System.out.println("Enter amount paid");
                int amount_paid = scanner.nextInt();

                insertDistributorPayment(account_number, payment_date, amount_paid);
                deduceBalance(account_number, payment_date);

        }

        public static void displayDistributorMenu() {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nDistributor Management Menu\n");
                        System.out.println("---------------Distributors---------------");
                        System.out.println("1.  Add a new Distributor");
                        System.out.println("2.  Update a Distributor");
                        System.out.println("3.  Delete a Distributor ");
                        System.out.println("4.  Place an order for a distributor");
                        System.out.println("5.  Generate Bill for distributor");
                        System.out.println("6.  Receive Payment of distributor");
                        System.out.println("7.  Show All Distributors");
                        System.out.println("---------------MENU ACTIONS---------------");
                        System.out.println("8. Go back to previous Menu");
                        System.out.println("9. Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        addDistributorInputs();
                                        break;
                                case "2":
                                        updateDistributorInputs();
                                        break;
                                case "3":
                                        deleteDistributorInputs();
                                        break;
                                case "4":
                                        placeOrderInputs();
                                        break;
                                case "5":
                                        generateBill();
                                        break;
                                case "6":
                                        receivePayment();
                                        break;
                                case "7":
                                        displayAllDistributors();
                                        break;
                                case "8":
                                        return;
                                case "9":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }
                }
        }

        public static void displayReports(String input) {
                try {
                        switch (input) {
                                case "1":
                                        result = copiesPerDistributorPerMonthlyReportQuery.executeQuery();
                                        break;
                                case "2":
                                        result = monthlyTotalRevenueReportQuery.executeQuery();
                                        break;
                                case "3":
                                        result = monthlyTotalExpenseReportQuery.executeQuery();
                                        break;
                                case "4":
                                        result = currentTotalDistributorsReportQuery.executeQuery();
                                        break;
                                case "5":
                                        result = totalRevenuePerCityReportQuery.executeQuery();
                                        break;
                                case "6":
                                        result = totalRevenuePerDistibutorReportQuery.executeQuery();
                                        break;
                                case "7":
                                        result = totalRevenuePerLocationReportQuery.executeQuery();
                                        break;
                                case "8":
                                        System.out.print("\nEnter Start Date: ");
                                        String startDate = scanner.nextLine();
                                        System.out.print("\nEnter End Date: ");
                                        String endDate = scanner.nextLine();
                                        totalStaffPaymentsPerPeriodPerWorkTypeReportQuery.setString(1, startDate);
                                        totalStaffPaymentsPerPeriodPerWorkTypeReportQuery.setString(2, endDate);
                                        totalStaffPaymentsPerPeriodPerWorkTypeReportQuery.setString(3, startDate);
                                        totalStaffPaymentsPerPeriodPerWorkTypeReportQuery.setString(4, endDate);
                                        result = totalStaffPaymentsPerPeriodPerWorkTypeReportQuery.executeQuery();
                                        break;
                                case "9":
                                        return;
                                case "10":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }

                        if (result != null) {
                                if (!result.next()) {
                                        System.out.println("No Reports exist");
                                        return;
                                }
                                result.beforeFirst();
                                display_table(result);
                                System.out.println();
                        }
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                        System.out.println("Failure");
                }
        }

        public static void displayTableContent(String input) {
                try {
                        switch (input) {
                                case "publications":
                                        result = showPublications.executeQuery();
                                        break;
                                case "staff":
                                        result = showStaff.executeQuery();
                                        break;
                                case "books":
                                        result = showBooks.executeQuery();
                                        break;
                                case "orders":
                                        result = showOrders.executeQuery();
                                        break;
                                case "edits":
                                        result = showEdits.executeQuery();
                                        break;
                                case "payments":
                                        result = showPayments.executeQuery();
                                        break;
                                case "distributors":
                                        result = showDistributors.executeQuery();
                                        break;
                                case "distributor-payments":
                                        result = showDistributorPayments.executeQuery();
                                        break;
                                case "authors":
                                        result = showAuthors.executeQuery();
                                        break;
                                case "editors":
                                        result = showEditors.executeQuery();
                                        break;
                                case "periodicals":
                                        result = showPeriodicals.executeQuery();
                                        break;
                                case "chapters":
                                        result = showChapters.executeQuery();
                                        break;
                                case "articles":
                                        result = showArticles.executeQuery();
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }

                        if (result != null) {
                                if (!result.next()) {
                                        System.out.println("No Reports exist");
                                        return;
                                }
                                result.beforeFirst();
                                display_table(result);
                                System.out.println();
                        } else {
                                System.out.println("No Reports Exist");
                        }
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                        System.out.println("Failure");
                }
        }

        public static void displayReportsMenu() {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nReports Menu\n");
                        System.out.println("---------------Monthly Reports---------------");
                        System.out.println(
                                        "1.  Number and total price of copies of each publication bought per distributor");
                        System.out.println("2.  Total revenue of the publishing house");
                        System.out.println("3.  Total expenses ");
                        System.out.println("---------------Other Reports---------------");
                        System.out.println("4.  Total current number of distributors");
                        System.out.println("5.  Total revenue (since inception) per city");
                        System.out.println("6.  Total revenue (since inception) per distributor");
                        System.out.println("7.  Total revenue (since inception) per location");
                        System.out.println("8.  Total payments to the editors and authors");
                        System.out.println("---------------MENU ACTIONS---------------");
                        System.out.println("9. Go back to previous Menu");
                        System.out.println("10. Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        displayReports(response);
                        if (response.equals("9")) {
                                return;
                        }
                }
        }

        public static void displayStaffMenu() {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nStaff Management Menu\n");
                        System.out.println("---------------Editors---------------");
                        System.out.println("1.  Add a new editor");
                        System.out.println("2.  Update an editor");
                        System.out.println("3.  Delete an editor ");
                        System.out.println("4.  Assign Editor to Publication");
                        System.out.println("5.  Remove Editor as a publication editor");
                        System.out.println("6.  Find Editor by ID");
                        System.out.println("7.  Show all Editors");
                        System.out.println("8.  Find Publication by Editor");
                        System.out.println("---------------Authors---------------");
                        System.out.println("9.  Add a new Autor");
                        System.out.println("10.  Update an Author");
                        System.out.println("11.  Delete an author");
                        System.out.println("12. Add an author to a Book");
                        System.out.println("13. Add an author to an Article");
                        System.out.println("14. Remove an author to a Book");
                        System.out.println("15. Remove an author to a Article");
                        System.out.println("16. Find Author by ID");
                        System.out.println("17. Show all Authors");
                        System.out.println("18. Find Book by Author");
                        System.out.println("19. Find Article by Author");
                        System.out.println("---------------Staff Payment---------------");
                        System.out.println("20. Enter payment for Staff");
                        System.out.println("21. Update Date of collection of payment for Staff");
                        System.out.println("22. Show payments for Staff ID");
                        System.out.println("---------------MENU ACTIONS---------------");
                        System.out.println("23. Go back to previous Menu");
                        System.out.println("24. Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        addEditor();
                                        break;
                                case "2":
                                        updateStaff();
                                        break;
                                case "3":
                                        deleteStaff();
                                        break;
                                case "4":
                                        addEdits();
                                        break;
                                case "5":
                                        deleteEdits();
                                        break;
                                case "6":
                                        showEditorByID();
                                        break;
                                case "7":
                                        displayAllEditors();
                                        break;
                                case "8":
                                        findPublicationByEditor();
                                        break;
                                case "9":
                                        addAuthor();
                                        break;
                                case "10":
                                        updateStaff();
                                        break;
                                case "11":
                                        deleteStaff();
                                        break;
                                case "12":
                                        addWritesBook();
                                        break;
                                case "13":
                                        addWritesArticles();
                                        break;
                                case "14":
                                        deleteWritesBook();
                                        break;
                                case "15":
                                        deleteWritesArticles();
                                        break;
                                case "16":
                                        showAuthorByID();
                                        break;
                                case "17":
                                        displayAllAuthors();
                                        break;
                                case "18":
                                        findBookByAuthor();
                                        break;
                                case "19":
                                        findArticleByAuthor();
                                        break;
                                case "20":
                                        insertPayment();
                                        break;
                                case "21":
                                        updateCollectionDateOfPayment();
                                        break;
                                case "22":
                                        displayPaymentsForStaff();
                                        break;
                                case "23":
                                        return;
                                case "24":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }
                }
        }

        public static void displayEditorMenu() {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nEditor Menu\n");
                        System.out.println("---------------Editors Menu---------------");
                        System.out.println("1. Show Publications assigned to Editor");
                        System.out.println("2. Update a chapter of a Book");
                        System.out.println("3. Update text of article");
                        System.out.println("4. Show payments to Editor");
                        System.out.println("---------------MENU ACTIONS---------------");
                        System.out.println("5. Go back to previous Menu");
                        System.out.println("6. Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        findPublicationByEditor();
                                        break;
                                case "2":
                                        updateBookMenu();
                                        break;
                                case "3":
                                        updateArticleMenu();
                                        break;
                                case "4":
                                        displayPaymentsForStaff();
                                        break;
                                case "5":
                                        return;
                                case "6":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }
                }
        }

        public static void showOrdersDistributor() {
                int account_number;
                System.out.println("Enter the account number");
                account_number = scanner.nextInt();
                try {
                        showOrdersDistributorQuery.setInt(1, account_number);
                        result = showOrdersDistributorQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No such orders exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void showDistributorBalance() {
                int account_number;
                System.out.println("Enter the account number");
                account_number = scanner.nextInt();
                try {
                        showDistributorBalanceQuery.setInt(1, account_number);
                        result = showDistributorBalanceQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No distributor exists with the given account number");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void showDistributorPayments() {
                int account_number;
                System.out.println("Enter the account number");
                account_number = scanner.nextInt();
                try {
                        showDistributorPaymentsQuery.setInt(1, account_number);
                        result = showDistributorPaymentsQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No such payments exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void showArticlesPeriodical() {
                int pid;
                System.out.println("Enter the publication number of the periodical");
                pid = scanner.nextInt();
                scanner.nextLine();
                try {
                        showArticlesPeriodicalQuery.setInt(1, pid);
                        result = showArticlesPeriodicalQuery.executeQuery();
                        if (!result.next()) {
                                System.out.println("No such articles exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void displayDistributorsMenu() {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nDistributors Menu\n");
                        System.out.println("---------------Distributors Menu---------------");
                        System.out.println("1. Place an order");
                        System.out.println("2. Show All orders for distributor");
                        System.out.println("3. Show balance for distributor account");
                        System.out.println("4. Make payment to distributor account");
                        System.out.println("5. Show payments to distributor account");
                        System.out.println("---------------Publications Menu---------------");
                        System.out.println("6. Find books");
                        System.out.println("7. Find periodicals");
                        System.out.println("8. Show all Books");
                        System.out.println("9. Show all periodicals");
                        System.out.println("10. Show all articles for a periodical");
                        System.out.println("---------------MENU ACTIONS---------------");
                        System.out.println("11. Go back to previous Menu");
                        System.out.println("12. Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        placeOrderInputs();
                                        break;
                                case "2":
                                        showOrdersDistributor();
                                        break;
                                case "3":
                                        showDistributorBalance();
                                        break;
                                case "4":
                                        receivePayment();
                                        break;
                                case "5":
                                        showDistributorPayments();
                                        break;
                                case "6":
                                        findBooks();
                                        break;
                                case "7":
                                        findPeriodicals();
                                        break;
                                case "8":
                                        displayAllBooks();
                                        break;
                                case "9":
                                        displayAllPeriodicals();
                                        break;
                                case "10":
                                        showArticlesPeriodical();
                                        break;
                                case "11":
                                        return;
                                case "12":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }
                }
        }

        public static void displayAuthorMenu() {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nAuthor Menu\n");
                        System.out.println("---------------Authors Menu---------------");
                        System.out.println("1. Add a new book edition");
                        System.out.println("2. Add an article to a Periodical");
                        System.out.println("3. Add a chapter to a Book");
                        System.out.println("4. Show Books by Author");
                        System.out.println("5. Show Articles by Author");
                        System.out.println("6. Show payments to Author");
                        System.out.println("---------------MENU ACTIONS---------------");
                        System.out.println("7. Go back to previous Menu");
                        System.out.println("8. Exit");
                        String title, text, creationDate;
                        int publicationID;

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        getNewBookInputs();
                                        break;
                                case "2":
                                        getNewArticleInputs();
                                        break;
                                case "3":
                                        getNewChapterInputs();
                                        break;
                                case "4":
                                        findBookByAuthor();
                                        break;
                                case "5":
                                        findArticleByAuthor();
                                        break;
                                case "6":
                                        displayPaymentsForStaff();
                                        break;
                                case "7":
                                        return;
                                case "8":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }
                }
        }

        public static void displayAdminMenu() {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nWelcome Admin. Below are the operations you can perform.\n");
                        System.out.println("1.Show Publications Menu");
                        System.out.println("2.Show Staff Menu");
                        System.out.println("3.Show Distributors Menu");
                        System.out.println("4.Show Reports Menu");
                        System.out.println("5.Go back to previous Menu");
                        System.out.println("6.Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        displayPublicationsMenu();
                                        break;
                                case "2":
                                        displayStaffMenu();
                                        break;
                                case "3":
                                        displayDistributorMenu();
                                        break;
                                case "4":
                                        displayReportsMenu();
                                        break;
                                case "5":
                                        return;
                                case "6":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }
                }
        }

        public static void displayDBAdminMenu() throws SQLException {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nWelcome DataBase Admin. Below are the operations you can perform.\n");
                        System.out.println("1.Create Tables");
                        System.out.println("2.Populate Tables with demo data");
                        System.out.println("3.Populate Tables with All data");
                        System.out.println("4.Delete all tables");
                        System.out.println("5.Clear data in all tables");
                        System.out.println("6.Go back to previous Menu");
                        System.out.println("7.Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        initDBTables();
                                        break;
                                case "2":
                                        loadDataForDemo();
                                        break;
                                case "3":
                                        loadData();
                                        break;
                                case "4":
                                        dropDBTables();
                                        break;
                                case "5":
                                        deleteDataFromDBTables();
                                        break;
                                case "6":
                                        return;
                                case "7":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }
                }
        }

        public static int generatePublicationID() throws SQLException {
                int publicationID = 0;

                try {
                        result = getLastPublicationID.executeQuery();
                        result.beforeFirst();
                        while (result.next()) {
                                publicationID = result.getInt("publication_ID");
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

                return publicationID + 1;
        }

        public static void displayUpdateBookMenu() {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nBelow are the update operations you can perform.\n");
                        System.out.println("1.Show Publications Menu");
                        System.out.println("2.Show Staff Menu");
                        System.out.println("3.Show Distributors Menu");
                        System.out.println("4.Show Reports Menu");
                        System.out.println("5.Go back to previous Menu");
                        System.out.println("6.Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        displayPublicationsMenu();
                                        break;
                                case "2":
                                        displayStaffMenu();
                                        break;
                                case "3":
                                        displayDistributorMenu();
                                        break;
                                case "4":
                                        displayReportsMenu();
                                        break;
                                case "5":
                                        return;
                                case "6":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }
                }

        }

        public static void updatePeriodicalMenu() {
                displayAllPeriodicals();
                System.out.println("1.  update Periodical Issue Date (Date format YYYY-DD-MM).");
                System.out.println("2.  update Periodical Periodicity");
                System.out.println("3.  update the creation date of an article (Date format YYYY-DD-MM).");
                System.out.println("4.  update the text of an Article");
                System.out.println("5.  update an article title");
                String option = scanner.next();
                String value;
                int publicationID;
                try {
                        connection.setAutoCommit(false);
                        try {
                                switch (option) {
                                        case "1":
                                                System.out.println("Enter publication ID of the Periodical:");
                                                publicationID = scanner.nextInt();
                                                scanner.nextLine();
                                                updatePeriodicalIssueDateQuery.setInt(2, publicationID);
                                                System.out.println(
                                                                "Enter updated Issue Date (Date format YYYY-DD-MM):");
                                                value = scanner.nextLine();
                                                updatePeriodicalIssueDateQuery.setString(1, value);
                                                updatePeriodicalIssueDateQuery.executeUpdate();
                                                break;
                                        case "2":
                                                System.out.println("Enter publication ID of the Periodical");
                                                publicationID = scanner.nextInt();
                                                scanner.nextLine();
                                                updatePeriodicalPeriodicityQuery.setInt(2, publicationID);
                                                System.out.println("Enter the updated periodicity");
                                                value = scanner.nextLine();
                                                updatePeriodicalPeriodicityQuery.setString(1, value);
                                                updatePeriodicalPeriodicityQuery.executeUpdate();
                                                break;
                                        case "3":
                                                System.out.println("Enter publication ID of the Periodical");
                                                publicationID = scanner.nextInt();
                                                scanner.nextLine();
                                                updateArticleCreationDateQuery.setInt(2, publicationID);
                                                System.out.println("Enter title of the Periodical:");
                                                value = scanner.nextLine();
                                                updateArticleCreationDateQuery.setString(3, value);
                                                System.out.println(
                                                                "Enter the updated creation date of the article (Date format YYYY-DD-MM):");
                                                value = scanner.nextLine();
                                                updateArticleCreationDateQuery.setString(1, value);
                                                updateArticleCreationDateQuery.executeUpdate();
                                                break;
                                        case "4":
                                                System.out.println("Enter publication ID of the Periodical");
                                                publicationID = scanner.nextInt();
                                                scanner.nextLine();
                                                updateArticleTextQuery.setInt(2, publicationID);
                                                System.out.println("Enter title of the Article:");
                                                value = scanner.nextLine();
                                                updateArticleTextQuery.setString(3, value);
                                                System.out.println("Enter the updated text of an Article:");
                                                value = scanner.nextLine();
                                                updateArticleTextQuery.setString(1, value);
                                                updateArticleTextQuery.executeUpdate();
                                                break;
                                        case "5":
                                                System.out.println("Enter publication ID of the Periodical");
                                                publicationID = scanner.nextInt();
                                                scanner.nextLine();
                                                updateArticleTitleQuery.setInt(2, publicationID);
                                                System.out.println("Enter title of the Article:");
                                                value = scanner.next();
                                                updateArticleTitleQuery.setString(3, value);
                                                System.out.println("Enter the updated title of an Article:");
                                                value = scanner.next();
                                                updateArticleTitleQuery.setString(1, value);
                                                updateArticleTitleQuery.executeUpdate();
                                                break;
                                        case "6":
                                                return;
                                        default:
                                                System.out.println("Cannot perform the update operation");
                                                break;
                                }
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        public static void displayMenu() throws SQLException {

                while (true) {
                        System.out.println("\nEnter the user that you would like to use the system as.");
                        System.out.println(
                                        "1.Enter as Admin\n2.Enter as Editor\n3.Enter as Author\n4.Enter as Distributor\n5.Enter as Database Admin\n6.quit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        displayAdminMenu();
                                        break;
                                case "2":
                                        displayEditorMenu();
                                        break;
                                case "3":
                                        displayAuthorMenu();
                                        break;
                                case "4":
                                        displayDistributorsMenu();
                                        break;
                                case "5":
                                        displayDBAdminMenu();
                                        break;
                                case "6":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choices from above.");
                                        break;
                        }
                        clearConsoleScreen();
                }

        }

        private static void dropDBTables() throws SQLException {

                try {
                        connection.setAutoCommit(false);

                        String publications_table = "DROP TABLE `Publications`;";
                        String staff_table = "DROP TABLE `Staff`;";
                        String books_table = "DROP TABLE `Books`;";
                        String edits_table = "DROP TABLE `Edits`;";
                        String payment_table = "DROP TABLE `Payments`;";
                        String distributors_table = "DROP TABLE `Distributors`;";
                        String distributorPayments_table = "DROP TABLE `DistributorPayments`;";
                        String orders_table = "DROP TABLE `Orders`;";
                        String editors_table = "DROP TABLE `Editors`;";
                        String authors_table = "DROP TABLE `Authors`;";
                        String periodicals_table = "DROP TABLE `Periodicals`;";
                        String chapters_table = "DROP TABLE `Chapters`;";
                        String writesbook_table = "DROP TABLE `WritesBook`;";
                        String articles_table = "DROP TABLE `Articles`;";
                        String writesarticles_table = "DROP TABLE `WritesArticles`;";

                        statement.executeUpdate(payment_table);
                        statement.executeUpdate(distributorPayments_table);
                        statement.executeUpdate(orders_table);
                        statement.executeUpdate(chapters_table);
                        statement.executeUpdate(edits_table);
                        statement.executeUpdate(writesbook_table);
                        statement.executeUpdate(writesarticles_table);
                        statement.executeUpdate(articles_table);
                        statement.executeUpdate(distributors_table);
                        statement.executeUpdate(periodicals_table);
                        statement.executeUpdate(books_table);
                        statement.executeUpdate(publications_table);
                        statement.executeUpdate(editors_table);
                        statement.executeUpdate(authors_table);
                        statement.executeUpdate(staff_table);

                } catch (SQLException e) {
                        connection.rollback();
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                } finally {
                        connection.setAutoCommit(true);
                }
        }

        private static void deleteDataFromDBTables() throws SQLException {

                try {
                        connection.setAutoCommit(false);

                        String publications_table = "DELETE FROM `Publications`;";
                        String staff_table = "DELETE FROM `Staff`;";
                        String books_table = "DELETE FROM `Books`;";
                        String edits_table = "DELETE FROM `Edits`;";
                        String payment_table = "DELETE FROM `Payments`;";
                        String distributors_table = "DELETE FROM `Distributors`;";
                        String distributorPayments_table = "DELETE FROM `DistributorPayments`;";
                        String orders_table = "DELETE FROM `Orders`;";
                        String editors_table = "DELETE FROM `Editors`;";
                        String authors_table = "DELETE FROM `Authors`;";
                        String periodicals_table = "DELETE FROM `Periodicals`;";
                        String chapters_table = "DELETE FROM `Chapters`;";
                        String writesbook_table = "DELETE FROM `WritesBook`;";
                        String articles_table = "DELETE FROM `Articles`;";
                        String writesarticles_table = "DELETE FROM `WritesArticles`;";

                        statement.executeUpdate(payment_table);
                        statement.executeUpdate(distributorPayments_table);
                        statement.executeUpdate(orders_table);
                        statement.executeUpdate(chapters_table);
                        statement.executeUpdate(edits_table);
                        statement.executeUpdate(writesbook_table);
                        statement.executeUpdate(writesarticles_table);
                        statement.executeUpdate(articles_table);
                        statement.executeUpdate(distributors_table);
                        statement.executeUpdate(periodicals_table);
                        statement.executeUpdate(books_table);
                        statement.executeUpdate(publications_table);
                        statement.executeUpdate(editors_table);
                        statement.executeUpdate(authors_table);
                        statement.executeUpdate(staff_table);

                } catch (SQLException e) {
                        connection.rollback();
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                } finally {
                        connection.setAutoCommit(true);
                }
        }

        private static void initDBTables() throws SQLException {

                try {
                        connection.setAutoCommit(false);

                        String publications_table = "CREATE TABLE IF NOT EXISTS `Publications` ("
                                        + "`publication_ID` INT,"
                                        + "`title` VARCHAR(50) NOT NULL,"
                                        + "`topic` VARCHAR(30),"
                                        + "`type` VARCHAR(30) NOT NULL,"
                                        + "`price` DECIMAL(8,2) NOT NULL,"
                                        + "PRIMARY KEY(`publication_ID`)"
                                        + ");";

                        String staff_table = "CREATE TABLE IF NOT EXISTS `Staff` ("
                                        + "`staff_ID` INT,"
                                        + "`name` VARCHAR(120) NOT NULL,"
                                        + "`role` VARCHAR(15) NOT NULL,"
                                        + "`phone_number` VARCHAR(20) NOT NULL,"
                                        + "`Age` INTEGER ,"
                                        + "`Gender` VARCHAR(1) ,"
                                        + "`Email` VARCHAR(255) , "
                                        + "`Address` VARCHAR(255) , "
                                        + "PRIMARY KEY(`staff_ID`)"
                                        + ");";

                        String books_table = "CREATE TABLE IF NOT EXISTS `Books` ("
                                        + "`publication_ID` INT,"
                                        + "`ISBN` INT NOT NULL,"
                                        + "`edition` INT,"
                                        + "`publication_date` DATE NOT NULL,"
                                        + "PRIMARY KEY(`publication_ID`),"
                                        + "UNIQUE(`ISBN`),"
                                        + " FOREIGN KEY(`publication_ID`) REFERENCES "
                                        + "`Publications`(`publication_ID`) "
                                        + " ON DELETE CASCADE "
                                        + " ON UPDATE CASCADE"
                                        + ");";

                        String edits_table = "CREATE TABLE IF NOT EXISTS `Edits` ("
                                        + "`staff_ID` INT,"
                                        + "`publication_ID` INT,"
                                        + "PRIMARY KEY (`publication_ID`,`staff_ID`),"
                                        + " FOREIGN KEY (`publication_ID`) REFERENCES  "
                                        + "`Publications`(`publication_ID`)"
                                        + " ON DELETE CASCADE"
                                        + " ON UPDATE CASCADE"
                                        + ");";

                        String payment_table = "CREATE TABLE IF NOT EXISTS `Payments` ("
                                        + "`staff_ID` INT,"
                                        + "`salary_date` DATE NOT NULL,"
                                        + "`payment_amount` DECIMAL(8,2) NOT NULL,"
                                        + "`collection_date` date,"
                                        + "PRIMARY KEY (`staff_ID`,`salary_date`),"
                                        + " FOREIGN KEY(`staff_ID`) REFERENCES   `Staff`(`staff_ID`)"
                                        + " ON UPDATE CASCADE"
                                        + " ON DELETE CASCADE"
                                        + ");";

                        String distributors_table = "CREATE TABLE IF NOT EXISTS `Distributors` ("
                                        + "`account_number` INT,"
                                        + "`phone` VARCHAR(255) NOT NULL,"
                                        + "`city` VARCHAR(255) NOT NULL,"
                                        + "`street_address` VARCHAR(255) NOT NULL,"
                                        + "`type` VARCHAR(255) NOT NULL,"
                                        + "`name` VARCHAR(255) NOT NULL,"
                                        + "`balance` DECIMAL(8,2) NOT NULL,"
                                        + "`contact_person` VARCHAR(255) NOT NULL,"
                                        + "PRIMARY KEY(`account_number`)"
                                        + ");";

                        String distributorPayments_table = "CREATE TABLE IF NOT EXISTS `DistributorPayments` ("
                                        + "`account_number` INT,"
                                        + "`payment_date` DATE NOT NULL,"
                                        + "`amount_paid` DECIMAL(8,2) NOT NULL,"
                                        + "PRIMARY KEY (`account_number`,`payment_date`), "
                                        + " FOREIGN KEY (`account_number`) REFERENCES "
                                        + "Distributors(`account_number`)"
                                        + " ON DELETE CASCADE"
                                        + " ON UPDATE CASCADE"
                                        + ");";

                        String orders_table = "CREATE TABLE IF NOT EXISTS `Orders` ("
                                        + "`order_number` INT ,"
                                        + "`publication_ID` INT ,"
                                        + "`distributor_account_no` INT,"
                                        + "`order_date` DATE NOT NULL,"
                                        + "`order_delivery_date` DATE NOT NULL,"
                                        + "`number_of_copies` INT NOT NULL,"
                                        + "`total_cost` DECIMAL(8,2) NOT NULL,"
                                        + "`shipping_cost` DECIMAL(8,2) NOT NULL,"
                                        + "PRIMARY KEY (`order_number`),"
                                        + " FOREIGN KEY (`publication_ID`) REFERENCES "
                                        + "Publications(`publication_ID`)"
                                        + " ON DELETE CASCADE"
                                        + " ON UPDATE CASCADE,"
                                        + " FOREIGN KEY (`distributor_account_no`) REFERENCES "
                                        + "Distributors(`account_number`)"
                                        + " ON DELETE CASCADE"
                                        + " ON UPDATE CASCADE"
                                        + ");";

                        String editors_table = "CREATE TABLE IF NOT EXISTS `Editors` ("
                                        + "`staff_ID` INT,"
                                        + "`type` varchar(30) NOT NULL,"
                                        + "PRIMARY KEY(`staff_ID`),"
                                        + " FOREIGN KEY (`staff_ID`) REFERENCES  `Staff` (`staff_ID`)"
                                        + " ON UPDATE CASCADE"
                                        + " ON DELETE CASCADE"
                                        + ");";

                        String authors_table = "CREATE TABLE IF NOT EXISTS `Authors` ("
                                        + "`staff_ID` INT,"
                                        + "`type` varchar(30) NOT NULL,"
                                        + "PRIMARY KEY(`staff_ID`),"
                                        + " FOREIGN KEY (`staff_ID`) REFERENCES  `Staff` (`staff_ID`)"
                                        + " ON UPDATE CASCADE"
                                        + " ON DELETE CASCADE"
                                        + ");";

                        String periodicals_table = "CREATE TABLE IF NOT EXISTS `Periodicals` ("
                                        + "`publication_id` INT,"
                                        + "`issue_date` DATE NOT NULL,"
                                        + "`periodicity` VARCHAR(30) NOT NULL,"
                                        + "PRIMARY KEY (`publication_ID`),"
                                        + " FOREIGN KEY (`publication_ID`) REFERENCES "
                                        + "`Publications`(`publication_ID`)"
                                        + " ON DELETE CASCADE"
                                        + " ON UPDATE CASCADE"
                                        + ");";

                        String chapters_table = "CREATE TABLE IF NOT EXISTS `Chapters` ("
                                        + "`publication_ID` INT,"
                                        + "`title` VARCHAR(30) NOT NULL,"
                                        + "`text` TEXT NOT NULL,"
                                        + "PRIMARY KEY (`publication_ID`,`title`),"
                                        + " FOREIGN KEY (`publication_ID`) REFERENCES  `Books`(`publication_ID`)"
                                        + " ON DELETE CASCADE  ON UPDATE CASCADE"
                                        + ");";

                        String writesbook_table = "CREATE TABLE IF NOT EXISTS `WritesBook` ("
                                        + "`staff_ID` INT,"
                                        + "`publication_ID` INT,"
                                        + "PRIMARY KEY(`staff_ID`,`publication_ID`),"
                                        + " FOREIGN KEY(`publication_ID`) REFERENCES  `Books`(`publication_ID`)"
                                        + " ON DELETE CASCADE"
                                        + " ON UPDATE CASCADE,"
                                        + " FOREIGN KEY(`staff_ID`) REFERENCES  `Authors`(`staff_ID`)"
                                        + " ON DELETE CASCADE"
                                        + " ON UPDATE CASCADE"
                                        + ");";

                        String articles_table = "CREATE TABLE IF NOT EXISTS `Articles` ("
                                        + "`publication_ID` INT,"
                                        + "`title` VARCHAR(50) NOT NULL,"
                                        + "`text` TEXT NOT NULL,"
                                        + "`creation_date` DATE NOT NULL,"
                                        + "PRIMARY KEY(`publication_ID`,`title`),"
                                        + " FOREIGN KEY(`publication_ID`) REFERENCES "
                                        + "`Periodicals`(`publication_ID`)"
                                        + " ON DELETE CASCADE"
                                        + " ON UPDATE CASCADE"
                                        + ");";

                        String writesarticles_table = "CREATE TABLE IF NOT EXISTS `WritesArticles` ("
                                        + "`staff_ID` INT,"
                                        + "`publication_ID` INT,"
                                        + "`title` VARCHAR(50),"
                                        + "PRIMARY KEY(`staff_ID`,`publication_ID`, `title`),"
                                        + " FOREIGN KEY(`publication_ID`, `title`) REFERENCES "
                                        + "`Articles`(`publication_ID`, `title`)"
                                        + " ON DELETE CASCADE"
                                        + " ON UPDATE CASCADE,"
                                        + " FOREIGN KEY(`staff_ID`) REFERENCES  `Authors`(`staff_ID`)"
                                        + " ON DELETE CASCADE"
                                        + " ON UPDATE CASCADE"
                                        + ");";

                        statement.executeUpdate(staff_table);
                        statement.executeUpdate(authors_table);
                        statement.executeUpdate(editors_table);
                        statement.executeUpdate(publications_table);
                        statement.executeUpdate(books_table);
                        statement.executeUpdate(periodicals_table);
                        statement.executeUpdate(distributors_table);
                        statement.executeUpdate(articles_table);
                        statement.executeUpdate(writesarticles_table);
                        statement.executeUpdate(writesbook_table);
                        statement.executeUpdate(edits_table);
                        statement.executeUpdate(chapters_table);
                        statement.executeUpdate(orders_table);
                        statement.executeUpdate(distributorPayments_table);
                        statement.executeUpdate(payment_table);

                } catch (SQLException e) {
                        connection.rollback();
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                } finally {
                        connection.setAutoCommit(true);
                }
        }

        private static void loadData() throws SQLException {

                try {
                        connection.setAutoCommit(false);

                        statement.executeUpdate(
                                        "INSERT INTO Staff VALUES (6991,'Subodh','Admin','9391234560', 36, 'M', '3001@gmail.com', '21 ABC St, NC 27');");
                        statement.executeUpdate(
                                        "INSERT INTO Staff VALUES (6992,'Pallavi','Author','9391234561', 36, 'F', '3001@gmail.com', '21 ABC St, NC 27');");
                        statement.executeUpdate(
                                        "INSERT INTO Staff VALUES (6993,'Harini','Editor','9391234562', 36, 'F', '3001@gmail.com', '21 ABC St, NC 27');");
                        statement.executeUpdate(
                                        "INSERT INTO Staff VALUES (6994,'Saurab','Author','9391234563', 36, 'M', '3001@gmail.com', '21 ABC St, NC 27');");
                        statement.executeUpdate(
                                        "INSERT INTO Staff VALUES (6995,'Harish','Editor','9391234564', 36, 'M', '3001@gmail.com', '21 ABC St, NC 27');");
                        statement.executeUpdate(
                                        "INSERT INTO Staff VALUES (6996,'Eshwar','Author','9391234565', 36, 'M', '3001@gmail.com', '21 ABC St, NC 27');");
                        statement.executeUpdate(
                                        "INSERT INTO Staff VALUES (6997,'Sandeep','Editor','9391234566', 36, 'M', '3001@gmail.com', '21 ABC St, NC 27');");
                        statement.executeUpdate(
                                        "INSERT INTO Staff VALUES (6998,'Harika','Author','9391234567', 36, 'F', '3001@gmail.com', '21 ABC St, NC 27');");
                        statement.executeUpdate(
                                        "INSERT INTO Staff VALUES (6999,'Bhavya','Editor','9391234568', 36, 'F', '3001@gmail.com', '21 ABC St, NC 27');");

                        statement.executeUpdate("INSERT INTO Editors VALUES (6993,'Staff');");
                        statement.executeUpdate("INSERT INTO Editors VALUES (6995,'Invited');");
                        statement.executeUpdate("INSERT INTO Editors VALUES (6997,'Staff');");
                        statement.executeUpdate("INSERT INTO Editors VALUES (6999,'Invited');");

                        statement.executeUpdate("INSERT INTO Authors VALUES (6992,'Staff');");
                        statement.executeUpdate("INSERT INTO Authors VALUES (6994,'Invited');");
                        statement.executeUpdate("INSERT INTO Authors VALUES (6996,'Staff');");
                        statement.executeUpdate("INSERT INTO Authors VALUES (6998,'Invited');");

                        statement.executeUpdate(
                                        "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1009,'Brain Science','Science','Book',23);");
                        statement.executeUpdate(
                                        "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1010,'Animal Fashion','Fashion','Book',40);");
                        statement.executeUpdate(
                                        "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1011,'Introduction to Blockchain','Technology','Book',48);");
                        statement.executeUpdate(
                                        "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1004,'Introduction to Food','Health','Book',24);");
                        statement.executeUpdate(
                                        "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1005,'Science Today','Science','Periodical',34);");
                        statement.executeUpdate(
                                        "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1006,'Health Today','Health','Periodical',24);");
                        statement.executeUpdate(
                                        "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1007,'Fashion Today','Fashion','Periodical',44);");
                        statement.executeUpdate(
                                        "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1008,'Technology Today','Technology','Periodical',18);");

                        statement.executeUpdate(
                                        "INSERT INTO Payments VALUES (6992,'2021-03-02', 900.02, '2021-03-10');");
                        statement.executeUpdate(
                                        "INSERT INTO Payments VALUES (6996,'2021-03-02', 900.02, '2021-03-10');");
                        statement.executeUpdate(
                                        "INSERT INTO Payments VALUES (6992,'2021-04-02', 900.02, '2021-04-10');");
                        statement.executeUpdate(
                                        "INSERT INTO Payments VALUES (6996,'2021-04-02', 900.02, '2021-04-10');");
                        statement.executeUpdate(
                                        "INSERT INTO Payments VALUES (6994,'2021-03-02', 600.02, '2021-03-08');");
                        statement.executeUpdate(
                                        "INSERT INTO Payments VALUES (6998,'2021-03-02', 600.02, '2021-03-08');");
                        statement.executeUpdate(
                                        "INSERT INTO Payments VALUES (6995,'2021-04-02', 89.02, '2021-04-10');");
                        statement.executeUpdate(
                                        "INSERT INTO Payments VALUES (6997,'2021-04-02', 89.02, '2021-04-10');");

                        statement.executeUpdate(
                                        "INSERT INTO `Distributors` VALUES (3001,9101111111,'Raleigh','111, Avent Ferry Road','library','Distributor1',0,'Shane');");
                        statement.executeUpdate(
                                        "INSERT INTO `Distributors` VALUES (3002,9102222222,'New York','222, Brooke Street ','library','Distributor2',0,'Natalie');");
                        statement.executeUpdate(
                                        "INSERT INTO `Distributors` VALUES (3003,9103333333,'Chicago','333, High Point Ave','wholesale','Distributor3',0,'Jarette');");
                        statement.executeUpdate(
                                        "INSERT INTO `Distributors` VALUES (3004,9104444444,'Boston','444, Silver Spear Lane','wholesale','Distributor4',0,'Shaina');");
                        statement.executeUpdate(
                                        "INSERT INTO `Distributors` VALUES (3005,9105555555,'Austin','555, Second Street','bookstore','Distributor5',0,'Mallory');");
                        statement.executeUpdate(
                                        "INSERT INTO `Distributors` VALUES (3006,9106666666,'Raleigh','666, Walnutwood','bookstore','Distributor6',0,'Eric');");
                        statement.executeUpdate(
                                        "INSERT INTO `Distributors` VALUES (3007,9107777777,'Chicago','777, North 6th Lane','library','Distributor7',0,'Deepti');");
                        statement.executeUpdate(
                                        "INSERT INTO `Distributors` VALUES (3008,9108888888,'Austin','888, Third Street','bookstore','Distributor8',0,'Shake');");

                        statement.executeUpdate(
                                        "INSERT INTO `Periodicals` (`publication_ID`, `issue_date`,`periodicity`) VALUES (1005,'2022-01-12','Monthly');");
                        statement.executeUpdate(
                                        "INSERT INTO `Periodicals` (`publication_ID`, `issue_date`,`periodicity`) VALUES (1006,'2022-04-02','Yearly');");
                        statement.executeUpdate(
                                        "INSERT INTO `Periodicals` (`publication_ID`, `issue_date`,`periodicity`) VALUES (1007,'2022-03-10','Monthly');");
                        statement.executeUpdate(
                                        "INSERT INTO `Periodicals` (`publication_ID`, `issue_date`,`periodicity`) VALUES (1008,'2022-09-03','Weekly');");

                        statement.executeUpdate(
                                        "INSERT INTO `Books` (`publication_ID`, `ISBN`,`edition`, `publication_date`) VALUES (1009 ,123456 ,1,'2022-02-02');");
                        statement.executeUpdate(
                                        "INSERT INTO `Books` (`publication_ID`, `ISBN`,`edition`, `publication_date`) VALUES (1010 ,123457 ,1,'2022-04-03');");
                        statement.executeUpdate(
                                        "INSERT INTO `Books` (`publication_ID`, `ISBN`,`edition`, `publication_date`) VALUES (1011 ,123458 ,1,'2022-03-02');");
                        statement.executeUpdate(
                                        "INSERT INTO `Books` (`publication_ID`, `ISBN`,`edition`, `publication_date`) VALUES (1004 ,123459 ,1,'2022-04-12');");

                        statement.executeUpdate(
                                        "INSERT INTO `Chapters` VALUES (1009,'Brain Science','Brain Science Text');");
                        statement.executeUpdate(
                                        "INSERT INTO `Chapters` VALUES (1010,'Animal Fashion','Animal Fashion Text');");
                        statement.executeUpdate(
                                        "INSERT INTO `Chapters` VALUES (1011,'Introduction to Blockchain','Introduction to Blockchain Text');");
                        statement.executeUpdate(
                                        "INSERT INTO `Chapters` VALUES (1004,'Introduction to Food','Introduction to Food Text');");

                        statement.executeUpdate(
                                        "INSERT INTO Orders VALUES (13345, 1010, 3001, '2021-03-02', '2021-03-08', 10, 400, 20);");
                        statement.executeUpdate(
                                        "INSERT INTO Orders VALUES (13346, 1011, 3002, '2021-03-02', '2021-03-08', 10, 480, 20);");
                        statement.executeUpdate(
                                        "INSERT INTO Orders VALUES (13347, 1004, 3003, '2021-04-02', '2021-04-08', 10, 240, 20);");
                        statement.executeUpdate(
                                        "INSERT INTO Orders VALUES (13348, 1010, 3001, '2021-05-02', '2021-05-08', 10, 400, 20);");
                        statement.executeUpdate(
                                        "INSERT INTO Orders VALUES (13349, 1011, 3002, '2021-05-02', '2021-05-08', 10, 480, 20);");
                        statement.executeUpdate(
                                        "INSERT INTO Orders VALUES (13350, 1004, 3003, '2021-06-02', '2021-06-08', 10, 240, 20);");
                        statement.executeUpdate(
                                        "INSERT INTO Orders VALUES (13351, 1010, 3001, '2021-04-02', '2021-04-08', 10, 400, 20);");
                        statement.executeUpdate(
                                        "INSERT INTO Orders VALUES (13352, 1011, 3002, '2021-05-02', '2021-05-08', 10, 480, 20);");
                        statement.executeUpdate(
                                        "INSERT INTO Orders VALUES (13353, 1010, 3001, '2021-03-06', '2021-04-08', 5, 200, 20);");
                        statement.executeUpdate(
                                        "INSERT INTO Orders VALUES (13354, 1011, 3001, '2021-03-15', '2021-05-08', 5, 240, 20);");

                        statement.executeUpdate(
                                        "INSERT INTO `Articles` (`publication_ID`,`title`, `text`,`creation_date`) VALUES (1005,'science today title','science today text','2022-05-03');");
                        statement.executeUpdate(
                                        "INSERT INTO `Articles` (`publication_ID`,`title`, `text`,`creation_date`) VALUES (1005,'science today title2','science today text2','2022-04-02');");
                        statement.executeUpdate(
                                        "INSERT INTO `Articles` (`publication_ID`,`title`, `text`,`creation_date`) VALUES (1006,'health today title','health today text','2022-09-02');");
                        statement.executeUpdate(
                                        "INSERT INTO `Articles` (`publication_ID`,`title`,`text`,`creation_date`) VALUES (1006,'health today title2','health today text2','2022-12-02');");
                        statement.executeUpdate(
                                        "INSERT INTO `Articles` (`publication_ID`,`title`,`text`,`creation_date`) VALUES (1007,'fashion today title','fashion today text','2022-11-09');");
                        statement.executeUpdate(
                                        "INSERT INTO `Articles` (`publication_ID`,`title`, `text`,`creation_date`) VALUES (1007,'fashion today title2','fashion today text2','2022-02-07');");
                        statement.executeUpdate(
                                        "INSERT INTO `Articles` (`publication_ID`, `title`,`text`,`creation_date`) VALUES (1008,'technology today title','technology today text','2022-02-08');");
                        statement.executeUpdate(
                                        "INSERT INTO `Articles` (`publication_ID`, `title`,`text`,`creation_date`) VALUES (1008,'technology today title2','technology today text2','2022-03-07');");

                        statement.executeUpdate("Insert into Edits VALUES(6993,1009);");
                        statement.executeUpdate("Insert into Edits VALUES(6995,1009);");
                        statement.executeUpdate("Insert into Edits VALUES(6995,1010);");
                        statement.executeUpdate("Insert into Edits VALUES(6997,1011);");
                        statement.executeUpdate("Insert into Edits VALUES(6999,1009);");
                        statement.executeUpdate("Insert into Edits VALUES(6997,1004);");
                        statement.executeUpdate("Insert into Edits VALUES(6993,1004);");
                        statement.executeUpdate("Insert into Edits VALUES(6995,1007);");
                        statement.executeUpdate("Insert into Edits VALUES(6993,1008);");
                        statement.executeUpdate("Insert into Edits VALUES(6999,1007);");
                        statement.executeUpdate("Insert into Edits VALUES(6997,1006);");
                        statement.executeUpdate("Insert into Edits VALUES(6993,1006);");

                        statement.executeUpdate(
                                        "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3001,'2021-01-01','790');");
                        statement.executeUpdate(
                                        "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3002,'2021-02-03','122');");
                        statement.executeUpdate(
                                        "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3003,'2021-07-09','1899');");
                        statement.executeUpdate(
                                        "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3004,'2021-01-02','790');");
                        statement.executeUpdate(
                                        "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3005,'2021-09-01','987');");
                        statement.executeUpdate(
                                        "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3006,'2021-08-01','1009');");
                        statement.executeUpdate(
                                        "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3007,'2021-02-02','798.6');");
                        statement.executeUpdate(
                                        "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3008,'2021-06-08','999');");

                        statement.executeUpdate(
                                        "INSERT INTO `WritesArticles` (`staff_ID`, `publication_ID`,`title`) VALUES (6994, 1008, 'technology today title');");
                        statement.executeUpdate(
                                        "INSERT INTO `WritesArticles` (`staff_ID`, `publication_ID`,`title`) VALUES (6996, 1006, 'health today title');");
                        statement.executeUpdate(
                                        "INSERT INTO `WritesArticles` (`staff_ID`, `publication_ID`,`title`) VALUES (6996, 1006, 'health today title2');");
                        statement.executeUpdate(
                                        "INSERT INTO `WritesArticles` (`staff_ID`, `publication_ID`,`title`) VALUES (6996, 1007, 'fashion today title');");
                        statement.executeUpdate(
                                        "INSERT INTO `WritesArticles` (`staff_ID`, `publication_ID`,`title`) VALUES (6996, 1007, 'fashion today title2');");
                        statement.executeUpdate(
                                        "INSERT INTO `WritesArticles` (`staff_ID`, `publication_ID`,`title`) VALUES (6998, 1005, 'science today title');");
                        statement.executeUpdate(
                                        "INSERT INTO `WritesArticles` (`staff_ID`, `publication_ID`,`title`) VALUES (6998, 1005, 'science today title2');");

                        statement.executeUpdate(
                                        "INSERT INTO `WritesBook` (`staff_ID`, `publication_ID`) VALUES (6992, 1009);");
                        statement.executeUpdate(
                                        "INSERT INTO `WritesBook` (`staff_ID`, `publication_ID`) VALUES (6992, 1010);");
                        statement.executeUpdate(
                                        "INSERT INTO `WritesBook` (`staff_ID`, `publication_ID`) VALUES (6994, 1011);");
                        statement.executeUpdate(
                                        "INSERT INTO `WritesBook` (`staff_ID`, `publication_ID`) VALUES (6996, 1004);");

                } catch (SQLException e) {
                        connection.rollback();
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                } finally {
                        connection.setAutoCommit(true);
                }
        }

        private static void loadDataForDemo() throws SQLException {

                try {
                        connection.setAutoCommit(false);
                        // Publications
                        statement.executeUpdate("INSERT INTO Publications "
                                        + "(publication_ID, title, topic, type , price) VALUES"
                                        + "(1001, 'introduction to database', 'technology', 'Book', 20)");

                        statement.executeUpdate("INSERT INTO Publications "
                                        + "(publication_ID, title, topic, type , price) VALUES"
                                        + "(1002, 'Healthy Diet', 'health', 'Magazine', 20)");

                        statement.executeUpdate("INSERT INTO Publications "
                                        + "(publication_ID, title, topic, type , price) VALUES"
                                        + "(1003, 'Animal Science', 'science', 'Journal', 10)");
                        // Distributors
                        statement.executeUpdate("INSERT INTO Distributors "
                                        + "(account_number, phone , city, street_address, type, name, balance, contact_person) VALUES"
                                        + "(2001, '9191234567', 'charlotte', '2200, A Street, NC', 'bookstore', 'BookSell', 215, 'Jason')");

                        statement.executeUpdate("INSERT INTO Distributors "
                                        + "(account_number, phone , city, street_address, type, name, balance, contact_person) VALUES"
                                        + "(2002, '9291234568', 'Raleigh', '2200, B Street, NC', 'wholesaler', 'BookDist', 0, 'Alex')");
                        // Staff
                        statement.executeUpdate("INSERT INTO Staff "
                                        + "(staff_ID, name, role, phone_number, Age, Gender, Email, Address) VALUES"
                                        + "(3001, 'John', 'Editor', '9391234567', 36, 'M', '3001@gmail.com', '21 ABC St, NC 27')");

                        statement.executeUpdate("INSERT INTO Staff "
                                        + "(staff_ID, name, role, phone_number, Age, Gender, Email, Address) VALUES"
                                        + "(3002, 'Ethen', 'Editor', '9491234567', 30, 'M', '3002@gmail.com', '21 ABC St, NC 27606')");

                        statement.executeUpdate("INSERT INTO Staff "
                                        + "(staff_ID, name, role, phone_number, Age, Gender, Email, Address) VALUES"
                                        + "(3003, 'Cathy', 'Author', '9591234567', 28, 'F', '3003@gmail.com', '3300 AAA St, NC 27606')");

                        // Orders
                        statement.executeUpdate("INSERT INTO Orders "
                                        + "(order_number, publication_ID, distributor_account_no , order_date, order_delivery_date, number_of_copies, total_cost, shipping_cost) VALUES"
                                        + "(4001, 1001, 2001, '2020-01-02', '2020-01-15', 30, 630, 30 )");

                        statement.executeUpdate("INSERT INTO Orders "
                                        + "(order_number, publication_ID, distributor_account_no , order_date, order_delivery_date, number_of_copies, total_cost, shipping_cost) VALUES"
                                        + "(4002, 1001, 2001, '2020-02-05', '2020-02-15', 10, 215, 15 )");

                        statement.executeUpdate("INSERT INTO Orders "
                                        + "(order_number, publication_ID, distributor_account_no , order_date, order_delivery_date, number_of_copies, total_cost, shipping_cost) VALUES"
                                        + "(4003, 1003, 2002, '2020-02-10', '2020-02-25', 10, 115, 15 )");

                        // DistributorPayments
                        statement.executeUpdate("INSERT INTO DistributorPayments "
                                        + "(account_number, payment_date, amount_paid) VALUES "
                                        + "(2001,'2020-01-02',630);");

                        statement.executeUpdate("INSERT INTO DistributorPayments "
                                        + "(account_number, payment_date, amount_paid) VALUES "
                                        + "(2002,'2020-02-10',115);");

                        // Books
                        statement.executeUpdate("INSERT INTO Books "
                                        + "(publication_ID, ISBN, edition, publication_date) VALUES "
                                        + "(1001, 12345, 2, '2018-10-10');");

                        // Periodicals
                        statement.executeUpdate("INSERT INTO Periodicals "
                                        + "(publication_id, issue_date, periodicity) VALUES "
                                        + "(1002, '2020-02-24', 'Weekly');");

                        statement.executeUpdate("INSERT INTO Periodicals "
                                        + "(publication_id, issue_date, periodicity) VALUES "
                                        + "(1003, '2020-03-01', 'Monthly');");

                        // Editors
                        statement.executeUpdate("INSERT INTO Editors (staff_ID, type) VALUES (3001,'Staff');");

                        statement.executeUpdate("INSERT INTO Editors (staff_ID, type) VALUES (3002,'Staff');");

                        // Authors
                        statement.executeUpdate("INSERT INTO Authors (staff_ID, type) VALUES (3003, 'Invited');");

                        // Edits
                        statement.executeUpdate("INSERT INTO Edits (staff_ID, publication_ID) VALUES (3001, 1001);");

                        statement.executeUpdate("INSERT INTO Edits (staff_ID, publication_ID) VALUES (3002, 1002);");

                        // Payments
                        statement.executeUpdate(
                                        "INSERT INTO Payments (staff_ID, salary_date, payment_amount, collection_date) VALUES "
                                                        + "(3001,'2020-04-01',1000,'2020-04-02');");

                        statement.executeUpdate(
                                        "INSERT INTO Payments (staff_ID, salary_date, payment_amount, collection_date) VALUES "
                                                        + "(3002,'2020-04-01',1000,'2020-04-01');");

                        statement.executeUpdate(
                                        "INSERT INTO Payments (staff_ID, salary_date, payment_amount, collection_date) VALUES "
                                                        + "(3003,'2020-04-01',1200,'2020-04-02');");

                        // Articles
                        statement.executeUpdate(
                                        "INSERT INTO Articles (publication_ID, title, text, creation_date) VALUES "
                                                        + "(1002,'WolfaDiet How to','ABC','2019-03-02')");

                        statement.executeUpdate(
                                        "INSERT INTO Articles (publication_ID, title, text, creation_date) VALUES "
                                                        + "(1003,'Do you know Wolfanimals?','AAA','2020-02-02')");

                } catch (SQLException e) {
                        connection.rollback();
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                } finally {
                        connection.setAutoCommit(true);
                }
        }

        // initialization steps to setup the database and connections
        private static void initialize() {
                try {
                        connectToDatabase();

                        Runtime.getRuntime().addShutdownHook(new Thread() {
                                public void run() {
                                        try {
                                                Thread.sleep(200);
                                                System.out.println("\nShutting down ...");
                                                close();
                                        } catch (InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                                System.out.println(e.getMessage());
                                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                                        }
                                }
                        });

                        generateDDLAndDMLStatements();
                        System.out.println("Connection to WolfPubDB is successfull.");
                        scanner = new Scanner(System.in);
                } catch (ClassNotFoundException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
        }

        // connects to database
        private static void connectToDatabase() throws ClassNotFoundException, SQLException {
                Class.forName("org.mariadb.jdbc.Driver");
                // String user = "kvankad";
                // String password = "Builder!12";
                String user = "sthota";
                String password = "200420891";

                connection = DriverManager.getConnection(jdbcURL, user, password);
                generateDDLAndDMLStatements();
                statement = connection.createStatement();

        }

        // close connection from database
        private static void close() {
                System.out.println("Closing all connections....");
                if (connection != null) {
                        try {
                                connection.close();
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        }
                }
                if (statement != null) {
                        try {
                                statement.close();
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        }
                }
                if (result != null) {
                        try {
                                result.close();
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                System.out.println("Operation Failed. Try Again with Valid Inputs");
                        }
                }
        }

        public static void main(String[] args) {

                System.out.println("*********************");
                System.out.println("Welcome to WolfPubDB!");
                System.out.println("*********************");
                initialize();
                try {
                        displayMenu();
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }
                close();
        }

        // display table helper
        public static void display_table(ResultSet resultSet) {
                try {
                        ResultSetMetaData rsmd = resultSet.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        String headers[] = new String[columnsNumber];

                        for (int i = 0; i < columnsNumber; i++) {
                                headers[i] = rsmd.getColumnName(i + 1);
                        }
                        setHeaders(headers);
                        while (result.next()) {
                                String data[] = new String[columnsNumber];
                                for (int i = 0; i < columnsNumber; i++) {
                                        String temp = result.getString(i + 1);
                                        if (resultSet.wasNull()) {
                                                data[i] = "NULL";
                                        } else {
                                                data[i] = temp;
                                        }

                                }
                                addRow(data);
                        }
                        print();
                        resetRows();
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Operation Failed. Try Again with Valid Inputs");
                }

        }

        public static void setRightAlign(boolean rightAlign1) {
                rightAlign = rightAlign1;
        }

        public static void setHeaders(String... headers1) {
                headers = headers1;
        }

        public static void addRow(String... cells) {
                rows.add(cells);
        }

        public static void resetRows() {
                rows.clear();
        }

        public static void print() {
                int[] maxWidths = headers != null ? Arrays.stream(headers).mapToInt(String::length).toArray() : null;

                for (String[] cells : rows) {
                        if (maxWidths == null) {
                                maxWidths = new int[cells.length];
                        }
                        if (cells.length != maxWidths.length) {
                                throw new IllegalArgumentException(
                                                "Number of row-cells and headers should be consistent");
                        }
                        for (int i = 0; i < cells.length; i++) {
                                maxWidths[i] = Math.max(maxWidths[i], cells[i].length());
                        }
                }

                if (headers != null) {
                        printLine(maxWidths);
                        printRow(headers, maxWidths);
                        printLine(maxWidths);
                }
                for (String[] cells : rows) {
                        printRow(cells, maxWidths);
                }
                if (headers != null) {
                        printLine(maxWidths);
                }
        }

        private static void printLine(int[] columnWidths) {
                for (int i = 0; i < columnWidths.length; i++) {
                        String line = String.join("", Collections.nCopies(columnWidths[i] +
                                        verticalSep.length() + 1, HORIZONTAL_SEP));
                        System.out.print(joinSep + line + (i == columnWidths.length - 1 ? joinSep : ""));
                }
                System.out.println();
        }

        private static void printRow(String[] cells, int[] maxWidths) {
                for (int i = 0; i < cells.length; i++) {
                        String s = cells[i];
                        String verStrTemp = i == cells.length - 1 ? verticalSep : "";
                        if (rightAlign) {
                                System.out.printf("%s %" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp);
                        } else {
                                System.out.printf("%s %-" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp);
                        }
                }
                System.out.println();
        }

}
