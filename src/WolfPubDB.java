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

        private static PreparedStatement insertOrderQuery;
        private static PreparedStatement updateOrderDateQuery;
        private static PreparedStatement updateOrderDeliveryDateQuery;
        private static PreparedStatement updateOrderNumberOfCopiesQuery;
        private static PreparedStatement updateOrderTotalCostQuery;
        private static PreparedStatement updateOrderShippingCostQuery;
        private static PreparedStatement deleteOrderQuery;

        private static PreparedStatement insertPaymentQuery;
        private static PreparedStatement updatePaymentAmountQuery;
        private static PreparedStatement updatePaymentCollectionDateQuery;
        private static PreparedStatement deletePaymentQuery;

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

        public static void generateDDLAndDMLStatements(Connection connection) {
                String query = "";
                try {
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

                        query = "INSERT INTO `Edits` (`staff_ID`, `publication_ID`) VALUES (?, ?);";
                        editorAssignmentQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Edits`" + " WHERE `staff_ID` = ? AND `publication_ID`=?;";
                        editorUnAssignmentQuery = connection.prepareStatement(query);

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

                        query = "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`, `amount_paid`) VALUES (?, ?, ?);";
                        insertDistributorPaymentQuery = connection.prepareStatement(query);
                        query = "UPDATE `DistributorPayments`"
                                        + " SET `amount_paid` = ? WHERE `account_number`= ? AND `payment_date`=?;";
                        updateDistributorPaymentAmountPaidQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `DistributorPayments`"
                                        + " WHERE `account_number`= ? AND `payment_date`=?;";
                        deleteDistributorPaymentQuery = connection.prepareStatement(query);

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

                        query = "INSERT INTO `Distributors` (`account_number`, `phone`, `city`, `street_address`, `type`, `name`, `balance`, `contact_person`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
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

                        query = "INSERT INTO `Payment` (`staff_ID`, `salary_date`, `payment_amount`, `collection_date`) VALUES (?, ?, ?, ?);";
                        insertPaymentQuery = connection.prepareStatement(query);
                        query = "UPDATE `Payment`"
                                        + " SET `payment_amount` = ? WHERE staff_ID = ? AND salary_date = ?;";
                        query = "UPDATE `Payment`" + " SET `payment_amount` = ? WHERE order_number = ?;";
                        updatePaymentAmountQuery = connection.prepareStatement(query);
                        query = "UPDATE `Payment`"
                                        + " SET `collection_date` = ? WHERE staff_ID = ? AND salary_date = ?;";
                        updatePaymentCollectionDateQuery = connection.prepareStatement(query);
                        query = "DELETE FROM `Payment`" + " WHERE `staff_ID` = ? AND salary_date = ?;";

                        deletePaymentQuery = connection.prepareStatement(query);

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

                        query = "SELECT city AS distributor_city, sum(amount_paid) as revenue" +
                                        "FROM" +
                                        "Distributors D NATURAL JOIN DistributorPayments DP" +
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

                        query = "SELECT * FROM Editors;";
                        showEditors = connection.prepareStatement(query);

                        query = "SELECT * FROM Authors;";
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

                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void deleteDistributor(int account_number) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteDistributorQuery.setInt(1, account_number);
                                deleteDistributorQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

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
                                                updateDistributorPhoneQuery.setString(1, newValue);
                                                updateDistributorPhoneQuery.setInt(2, account_number);
                                                updateDistributorPhoneQuery.executeUpdate();
                                                break;

                                        case "3":
                                                updateDistributorCityQuery.setString(1, newValue);
                                                updateDistributorCityQuery.setInt(2, account_number);
                                                updateDistributorCityQuery.executeUpdate();
                                                break;

                                        case "5":
                                                updateDistributorStreetAddressQuery.setString(1, newValue);
                                                updateDistributorStreetAddressQuery.setInt(2, account_number);
                                                updateDistributorStreetAddressQuery.executeUpdate();
                                                break;
                                        case "6":
                                                updateDistributorTypeQuery.setString(1, newValue);
                                                updateDistributorTypeQuery.setInt(2, account_number);
                                                updateDistributorTypeQuery.executeUpdate();
                                                break;
                                        case "8":
                                                updateDistributorNameQuery.setString(1, newValue);
                                                updateDistributorNameQuery.setInt(2, account_number);
                                                updateDistributorNameQuery.executeUpdate();
                                                break;
                                        case "9":
                                                updateDistributorBalanceQuery.setInt(1, Integer.parseInt(newValue));
                                                updateDistributorBalanceQuery.setInt(2, account_number);
                                                updateDistributorBalanceQuery.executeUpdate();
                                                break;
                                        case "10":
                                                updateDistributorContactPersonQuery.setString(1, newValue);
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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void insertPublication(int publicationID, String title, String topic, String type, Double price) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertPublicationQuery.setInt(1, publicationID);
                                insertPublicationQuery.setString(2, title);
                                insertPublicationQuery.setString(3, topic);
                                insertPublicationQuery.setString(4, type);
                                insertPublicationQuery.setDouble(5, price);
                                insertPublicationQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void insertBook(int publicationID, int isbn, int edition, String publicationDate) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertBookQuery.setInt(1, publicationID);
                                insertBookQuery.setInt(2, isbn);
                                insertBookQuery.setInt(3, edition);
                                insertBookQuery.setString(4, publicationDate);
                                insertBookQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void deleteBook(int publicationID) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteBookQuery.setInt(1, publicationID);
                                deleteBookQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void updateBook(int publicationID, String option, String newValue) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                switch (option) {
                                        case "1":
                                                updateBookISBNQuery.setString(1, newValue);
                                                updatePublicationTitleQuery.setInt(2, publicationID);
                                                updatePublicationTitleQuery.executeUpdate();
                                                break;

                                        case "2":
                                                updateBookEditionQuery.setString(1, newValue);
                                                updateBookEditionQuery.setInt(2, publicationID);
                                                updateBookEditionQuery.executeUpdate();
                                                break;

                                        case "3":
                                                updateBookPublicationDateQuery.setString(1, newValue);
                                                updateBookPublicationDateQuery.setInt(2, publicationID);
                                                updateBookPublicationDateQuery.executeUpdate();
                                                break;

                                        default:
                                                System.out.println("Cannot perform the update operation");
                                                break;

                                }

                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

        public static void insertPeriodical(int publicationID, String issueDate, String periodicity) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertPeriodicalQuery.setInt(1, publicationID);
                                insertPeriodicalQuery.setString(2, issueDate);
                                insertPeriodicalQuery.setString(3, periodicity);
                                insertPeriodicalQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void deletePeriodical(int publicationID) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deletePeriodicalQuery.setInt(1, publicationID);
                                deletePeriodicalQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }
        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

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
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void deleteOrder(int order_number) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                deleteOrderQuery.setInt(1, order_number);
                                deleteOrderQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void insertPayment(int staff_ID, String salary_date, double payment_amount,
                        String collection_date) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                insertPaymentQuery.setInt(1, staff_ID);
                                insertOrderQuery.setString(2, salary_date);
                                insertOrderQuery.setDouble(3, payment_amount);
                                insertOrderQuery.setString(4, collection_date);
                                insertOrderQuery.executeUpdate();
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
                }

        }

        public static void updatePayment(String option, String newValue, int staff_ID, String salary_date) {
                try {
                        connection.setAutoCommit(false);
                        try {
                                switch (option) {

                                        case "1":
                                                updatePaymentAmountQuery.setDouble(1, Double.parseDouble(newValue));
                                                updatePaymentAmountQuery.setInt(2, staff_ID);
                                                updatePaymentAmountQuery.setString(2, salary_date);
                                                updatePaymentAmountQuery.executeUpdate();
                                                break;
                                        case "2":
                                                updatePaymentCollectionDateQuery.setString(1, newValue);
                                                updatePaymentCollectionDateQuery.setInt(2, staff_ID);
                                                updatePaymentCollectionDateQuery.setString(2, salary_date);
                                                updatePaymentCollectionDateQuery.executeUpdate();
                                                break;
                                        default:
                                                System.out.println("Cannot perform the update operation");
                                                break;
                                }
                                connection.commit();
                        } catch (SQLException e) {
                                connection.rollback();
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (Exception e) {
                        System.out.println("Failure");
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
                        e.printStackTrace();
                        // Handle any exceptions.
                }
                // System.out.print("Everything on the console will cleared");
                // System.out.print("\033[H\033[2J");
                // System.out.flush();
        }

        public static void getNewBookInputs() {
                try {
                        int publicationID = generatePublicationID();
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
                        insertPublication(publicationID, title, topic, type, price);
                        insertBook(publicationID, isbn, edition, publicationDate);
                } catch (Exception e) {
                        System.out.println("Failure");
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

                        insertPublication(publicationID, title, topic, type, price);
                        insertPeriodical(publicationID, issueDate, periodicity);

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

                } catch (Throwable err) {
                        err.printStackTrace();
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

                } catch (Throwable err) {
                        err.printStackTrace();
                }
        }

        public static void displayPublicationsMenu() {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nPublications Menu\n");
                        System.out.println("---------------BOOKS---------------");
                        System.out.println("1.  Add a new book edition");
                        System.out.println("2.  Update book edition");
                        System.out.println("3.  Delete book edition ");
                        System.out.println("4.  Add a chapter to a Book");
                        System.out.println("5.  Update a chapter of a Book");
                        System.out.println("6.  Find books");
                        System.out.println("7.  Show all Books");
                        System.out.println("8.  Show all chapters for a Book");
                        System.out.println("---------------PERIODICALS---------------");
                        System.out.println("9.  Add new Periodical");
                        System.out.println("10. Update Periodical");
                        System.out.println("11. Delete Periodical");
                        System.out.println("12. Add an article to a Periodical");
                        System.out.println("13. Update an article of a Periodical");
                        System.out.println("15. Update text of article");
                        System.out.println("16. Find articles");
                        System.out.println("17. Show all periodicals");
                        System.out.println("18. Show all articles for a periodical");
                        System.out.println("---------------MENU ACTIONS---------------");
                        System.out.println("19. Go back to previous Menu");
                        System.out.println("20. Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        getNewBookInputs();
                                        break;
                                case "2":
                                        displayUpdateBookMenu();
                                        break;
                                case "3":
                                        getDeleteBookInputs();
                                        break;
                                case "4":
                                        getNewChapterInputs();
                                        break;
                                case "5":
                                        break;
                                case "6":
                                        break;
                                case "7":
                                        displayAllBooks();
                                        break;
                                case "8":
                                        getBookIDforChapters();
                                        break;
                                case "9":
                                        getNewPeriodicalInputs();
                                        break;
                                case "10":
                                        break;
                                case "11":
                                        getDeletePeriodicalInputs();
                                        break;
                                case "12":
                                        getNewArticleInputs();
                                        break;
                                case "13":
                                        updatePeriodicalMenu();
                                        break;
                                case "14":
                                        break;
                                case "15":
                                        break;
                                case "16":
                                        break;
                                case "17":
                                        displayAllPeriodicals();
                                        break;
                                case "18":
                                        getPeriodicalIDforArticles();
                                        break;
                                case "19":
                                        return;
                                case "20":
                                        System.exit(0);
                                        break;
                                default:
                                        System.out.println("Please enter correct choice from above.");
                                        break;
                        }
                }

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
                        System.out.println("6.  Recieve Payment of distributor");
                        System.out.println("---------------MENU ACTIONS---------------");
                        System.out.println("7. Go back to previous Menu");
                        System.out.println("8. Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        break;
                                case "2":
                                        break;
                                case "3":
                                        break;
                                case "4":
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

        public static void displayReportsMenu() {
                while (true) {
                        clearConsoleScreen();
                        System.out.println("\nDistributor Management Menu\n");
                        System.out.println("---------------Monthly Reports---------------");
                        System.out.println(
                                        "1.  Number and total price of copies of each publication bought per distributor");
                        System.out.println("2.  Total revenue of the publishing house");
                        System.out.println("3.  Total expenses ");
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
                        switch (response) {
                                case "1":
                                        break;
                                case "2":
                                        break;
                                case "3":
                                        break;
                                case "4":
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
        }

        public static void displayAllEditors() {
                try {
                        result = statement.executeQuery("Select * from Editors;");
                        if (!result.next()) {
                                System.out.println("No Editors exist");
                                return;
                        }
                        result.beforeFirst();
                        display_table(result);
                        System.out.println();
                } catch (Exception e) {
                        System.out.println("Failure");
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
                        System.out.println("6.  Find Editor");
                        System.out.println("7.  Show all Editors");
                        System.out.println("---------------Authors---------------");
                        System.out.println("8.  Add a new Autor");
                        System.out.println("9.  Update an Author");
                        System.out.println("10.  Delete an author");
                        System.out.println("11. Add an author to a Book");
                        System.out.println("12. Add an author to an Article");
                        System.out.println("13. Remove an author to a Book");
                        System.out.println("14. Remove an author to a Article");
                        System.out.println("15. Find Authors");
                        System.out.println("16.  Show all Authors");
                        System.out.println("---------------Staff Payment---------------");
                        System.out.println("17. Enter payment for Staff");
                        System.out.println("18. Update Date of collection of payment for Staff");
                        System.out.println("---------------MENU ACTIONS---------------");
                        System.out.println("19. Go back to previous Menu");
                        System.out.println("20. Exit");

                        System.out.print("\nEnter Choice: ");
                        String response = scanner.nextLine();
                        switch (response) {
                                case "1":
                                        break;
                                case "2":
                                        break;
                                case "3":
                                        break;
                                case "4":
                                        break;
                                case "7":
                                        displayAllEditors();
                                        break;
                                case "16":
                                        break;
                                case "19":
                                        return;
                                case "20":
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
                        e.printStackTrace();
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
                                e.printStackTrace();
                        } finally {
                                connection.setAutoCommit(true);
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
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
                                        // displayEditorMenu();
                                        break;
                                case "3":
                                        // displayAuthorMenu();
                                        break;
                                case "4":
                                        // displayDistributorMenu();
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
                        e.printStackTrace();
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
                        e.printStackTrace();
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
                        e.printStackTrace();
                } finally {
                        connection.setAutoCommit(true);
                }
        }

        private static void loadData() throws SQLException {

                try {
                        connection.setAutoCommit(false);

                        statement.executeUpdate("INSERT INTO Staff VALUES (6991,'Subodh','Admin','9391234560');");
                        statement.executeUpdate("INSERT INTO Staff VALUES (6992,'Pallavi','Author','9391234561');");
                        statement.executeUpdate("INSERT INTO Staff VALUES (6993,'Harini','Editor','9391234562');");
                        statement.executeUpdate("INSERT INTO Staff VALUES (6994,'Saurab','Author','9391234563');");
                        statement.executeUpdate("INSERT INTO Staff VALUES (6995,'Harish','Editor','9391234564');");
                        statement.executeUpdate("INSERT INTO Staff VALUES (6996,'Eshwar','Author','9391234565');");
                        statement.executeUpdate("INSERT INTO Staff VALUES (6997,'Sandeep','Editor','9391234566');");
                        statement.executeUpdate("INSERT INTO Staff VALUES (6998,'Harika','Author','9391234567');");
                        statement.executeUpdate("INSERT INTO Staff VALUES (6999,'Bhavya','Editor','9391234568');");

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
                        e.printStackTrace();
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
                                        + "(staff_ID, name, role, phone_number) VALUES"
                                        + "(3001, 'John', 'Editor', '9391234567')");

                        statement.executeUpdate("INSERT INTO Staff "
                                        + "(staff_ID, name, role, phone_number) VALUES"
                                        + "(3002, 'Ethen', 'Editor', '9491234567')");

                        statement.executeUpdate("INSERT INTO Staff "
                                        + "(staff_ID, name, role, phone_number) VALUES"
                                        + "(3003, 'Cathy', 'Author', '9591234567')");

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
                        e.printStackTrace();
                } finally {
                        connection.setAutoCommit(true);
                }
        }

        private static void initialize() {
                try {
                        connectToDatabase();
                        System.out.println("Connection to WolfPubDB is successfull.");
                        scanner = new Scanner(System.in);
                } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }

        private static void connectToDatabase() throws ClassNotFoundException, SQLException {
                Class.forName("org.mariadb.jdbc.Driver");

                // String user = "kvankad";
                // String password = "Builder!12";
                String user = "sthota";
                String password = "200420891";

                connection = DriverManager.getConnection(jdbcURL, user, password);
                generateDDLAndDMLStatements(connection);
                statement = connection.createStatement();

        }

        private static void close() {
                if (connection != null) {
                        try {
                                connection.close();
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                }
                if (statement != null) {
                        try {
                                statement.close();
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                }
                if (result != null) {
                        try {
                                result.close();
                        } catch (SQLException e) {
                                e.printStackTrace();
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
                        e.printStackTrace();
                }
                close();
        }

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
                        e.printStackTrace();
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
