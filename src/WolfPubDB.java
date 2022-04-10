import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WolfPubDB {
    static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/kvankad";

    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet result = null;

    public static void main(String[] args) {

        System.out.println("***************************************************************");
        System.out.println("Welcome to WolfaPubDB! Yes you read it right, we are WOLFAPACK!");
        System.out.println("***************************************************************");
        initialize();
        close();
    }

    private static void initDBTables() {

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
                    + "FOREIGN KEY(`publication_ID`) REFERENCES"
                    + "`Publications`(`publication_ID`)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE"
                    + ");";

            String edits_table = "CREATE TABLE IF NOT EXISTS `Edits` ("
                    + "`staff_ID` INT,"
                    + "`publication_ID` INT,"
                    + "PRIMARY KEY (`publication_ID`,`staff_ID`),"
                    + "FOREIGN KEY (`publication_ID`) REFERENCES"
                    + "`Publications`(`publication_ID`)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE"
                    + ");";

            String payment_table = "CREATE TABLE IF NOT EXISTS `Payment` ("
                    + "`staff_ID` INT,"
                    + "`salary_date` DATE NOT NULL,"
                    + "`payment_amount` DECIMAL(8,2) NOT NULL,"
                    + "`collection_date` date,"
                    + "PRIMARY KEY (`staff_ID`,`salary_date`),"
                    + "FOREIGN KEY(`staff_ID`) REFERENCES `Staff`(`staff_ID`)"
                    + "ON UPDATE CASCADE"
                    + "ON DELETE CASCADE"
                    + ");";

            String distributors_table = "CREATE TABLE IF NOT EXISTS `Distributors` ("
                    + "`account_number` INT,"
                    + "`phone` VARCHAR(255) NOT NULL,"
                    + "`city` VARCHAR(255) NOT NULL,"
                    + "`street_address` VARCHAR(255) NOT NULL,"
                    + "`type` VARCHAR(255) NOT NULL,"
                    + "`name` VARCHAR(255) NOT NULL,"
                    + "`balance` INT NOT NULL,"
                    + "`contact_person` VARCHAR(255) NOT NULL,"
                    + "PRIMARY KEY(`account_number`)"
                    + ");";

            String distributorPayments_table = "CREATE TABLE IF NOT EXISTS `DistributorPayments` ("
                    + "`account_number` INT,"
                    + "`payment_date` DATE NOT NULL,"
                    + "`amount_paid` DECIMAL(8,2) NOT NULL,"
                    + "PRIMARY KEY (`account_number`,`payment_date`)"
                    + "FOREIGN KEY (account_number) REFERENCES"
                    + "Distributors(account_number)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE"
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
                    + "FOREIGN KEY (publication_ID) REFERENCES"
                    + "Publications(publication_ID)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE,"
                    + "FOREIGN KEY (distributor_account_no) REFERENCES"
                    + "Distributors(account_number)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE"
                    + ");";

            String editors_table = "CREATE TABLE IF NOT EXISTS `Editors` ("
                    + "`staff_ID` INT,"
                    + "`type` varchar(30) NOT NULL,"
                    + "PRIMARY KEY(`staff_ID`),"
                    + "FOREIGN KEY (`staff_ID`) REFERENCES `Staff` (`staff_ID`)"
                    + "ON UPDATE CASCADE"
                    + "ON DELETE CASCADE"
                    + ");";

            String authors_table = "CREATE TABLE IF NOT EXISTS `Authors` ("
                    + "`staff_ID` INT,"
                    + "`type` varchar(30) NOT NULL,"
                    + "PRIMARY KEY(`staff_ID`),"
                    + "FOREIGN KEY (`staff_ID`) REFERENCES `Staff` (`staff_ID`)"
                    + "ON UPDATE CASCADE"
                    + "ON DELETE CASCADE"
                    + ");";

            String periodicals_table = "CREATE TABLE IF NOT EXISTS `Periodicals` ("
                    + "`publication_id` INT,"
                    + "`issue_date` DATE NOT NULL,"
                    + "`periodicity` VARCHAR(30) NOT NULL,"
                    + "PRIMARY KEY (`publication_ID`),"
                    + "FOREIGN KEY (`publication_ID`) REFERENCES"
                    + "`Publications`(`publication_ID`)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE"
                    + ");";

            String chapters_table = "CREATE TABLE IF NOT EXISTS `Chapters` ("
                    + "`publication_ID` INT,"
                    + "`title` VARCHAR(30) NOT NULL,"
                    + "`text` TEXT NOT NULL,"
                    + "PRIMARY KEY (`publication_ID`,`title`),"
                    + "FOREIGN KEY (`publication_ID`) REFERENCES `Books`(`publication_ID`)"
                    + "ON DELETE CASCADE ON UPDATE CASCADE"
                    + ");";

            String writesbook_table = "CREATE TABLE IF NOT EXISTS `WritesBook` ("
                    + "`staff_ID` INT,"
                    + "`publication_ID` INT,"
                    + "PRIMARY KEY(`staff_ID`,`publication_ID`),"
                    + "FOREIGN KEY(`publication_ID`) REFERENCES `Books`(`publication_ID`)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE,"
                    + "FOREIGN KEY(`staff_ID`) REFERENCES `Authors`(`staff_ID`)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE"
                    + ");";

            String articles_table = "CREATE TABLE IF NOT EXISTS `Articles` ("
                    + "`publication_ID` INT,"
                    + "`title` VARCHAR(50) NOT NULL,"
                    + "`text` TEXT NOT NULL,"
                    + "`creation_date` DATE NOT NULL,"
                    + "PRIMARY KEY(`publication_ID`,`title`),"
                    + "FOREIGN KEY(`publication_ID`) REFERENCES"
                    + "`Periodicals`(`publication_ID`)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE"
                    + ");";

            String writesarticles_table = "CREATE TABLE IF NOT EXISTS `WritesArticles` ("
                    + "`staff_ID` INT,"
                    + "`publication_ID` INT,"
                    + "`title` VARCHAR(50),"
                    + "PRIMARY KEY(`staff_ID`,`publication_ID`, `title`),"
                    + "FOREIGN KEY(`publication_ID`, `title`) REFERENCES"
                    + "`Articles`(`publication_ID`, `title`)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE,"
                    + "FOREIGN KEY(`staff_ID`) REFERENCES `Authors`(`staff_ID`)"
                    + "ON DELETE CASCADE"
                    + "ON UPDATE CASCADE"
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
            e.printStackTrace();
        }
    }
    

    private static void initialize() {
        try {
            connectToDatabase();
            System.out.println("Connection to WolfPubDB is successfull.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");

        String user = "kvankad";
        String password = "Builder!12";

        connection = DriverManager.getConnection(jdbcURL, user, password);
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
}