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

    private static PreparedStatement editorAssignmentQuery;
    private static PreparedStatement insertPublicationQuery;
    private static PreparedStatement updatePublicationTitleQuery;
    private static PreparedStatement updatePublicationTopicQuery;
    private static PreparedStatement updatePublicationTypeQuery;
    private static PreparedStatement updatePublicationPriceQuery;
    
    private static PreparedStatement createDistributorQuery;
    private static PreparedStatement updateDistributorAccountNumber;
    private static PreparedStatement updateDistributorPhoneQuery;
    private static PreparedStatement updateDistributorCityQuery;
    private static PreparedStatement updateDistributorStreetAddress;
    private static PreparedStatement updateDistributorType;
    private static PreparedStatement updateDistributorName;
    private static PreparedStatement updateDistributorBalance;
    private static PreparedStatement updateDistributorContactPerson;


    public static void generateDDLAndDMLStatements(Connection connection) {
        String query;
        try{
                query = "INSERT INTO `Publications` (`publication_ID`, `title`, `topic`, `type`, `price`) VALUES (?, ?, ?, ?, ?);";
                insertPublicationQuery= connection.prepareStatement(query);
                query = "UPDATE `Publications`" + " SET `price` = ? WHERE PID= ?;";
                updatePublicationPriceQuery = connection.prepareStatement(query);
                query = "UPDATE `Publications`" + " SET `TOPIC` = ? WHERE PID= ?;";
                updatePublicationTopicQuery= connection.prepareStatement(query);
                query = "UPDATE `Publications`" + " SET `title` = ? WHERE PID= ?;";
                updatePublicationTitleQuery= connection.prepareStatement(query);
                query = "UPDATE `Publications`" + " SET `type` = ? WHERE PID= ?;";
                updatePublicationTypeQuery= connection.prepareStatement(query);
                query = "INSERT INTO `Edits` (`staff_ID`, `publication_ID`) VALUES (?, ?);";
                editorAssignmentQuery= connection.prepareStatement(query);
        }
        catch (SQLException e){
                e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        System.out.println("***************************************************************");
        System.out.println("Welcome to WolfaPubDB! Yes you read it right, we are WOLFAPACK!");
        System.out.println("***************************************************************");
        initialize();
        close();
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
                    "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1001,'Brain Science','Science','Book',23);");
            statement.executeUpdate(
                    "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1002,'Animal Fashion','Fashion','Book',40);");
            statement.executeUpdate(
                    "INSERT INTO `Publications` (`publication_ID`, `title`,`topic`,`type`, `price`) VALUES (1003,'Introduction to Blockchain','Technology','Book',48);");
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

            statement.executeUpdate("INSERT INTO Payments VALUES (6992,'2021-03-02', 900.02, '2021-03-10');");
            statement.executeUpdate("INSERT INTO Payments VALUES (6996,'2021-03-02', 900.02, '2021-03-10');");
            statement.executeUpdate("INSERT INTO Payments VALUES (6992,'2021-04-02', 900.02, '2021-04-10');");
            statement.executeUpdate("INSERT INTO Payments VALUES (6996,'2021-04-02', 900.02, '2021-04-10');");
            statement.executeUpdate("INSERT INTO Payments VALUES (6994,'2021-03-02', 600.02, '2021-03-08');");
            statement.executeUpdate("INSERT INTO Payments VALUES (6998,'2021-03-02', 600.02, '2021-03-08');");
            statement.executeUpdate("INSERT INTO Payments VALUES (6995,'2021-04-02', 89.02, '2021-04-10');");
            statement.executeUpdate("INSERT INTO Payments VALUES (6997,'2021-04-02', 89.02, '2021-04-10');");

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

            statement.executeUpdate("INSERT INTO `Chapters` VALUES (1001,'Brain Science','Brain Science Text');");
            statement.executeUpdate("INSERT INTO `Chapters` VALUES (1002,'Animal Fashion','Animal Fashion Text');");
            statement.executeUpdate(
                    "INSERT INTO `Chapters` VALUES (1003,'Introduction to Blockchain','Introduction to Blockchain Text');");
            statement.executeUpdate(
                    "INSERT INTO `Chapters` VALUES (1004,'Introduction to Food','Introduction to Food Text');");

            statement.executeUpdate(
                    "INSERT INTO Orders VALUES (13345, 1002, 3001, '2021-03-02', '2021-03-08', 10, 400, 20);");
            statement.executeUpdate(
                    "INSERT INTO Orders VALUES (13346, 1003, 3002, '2021-03-02', '2021-03-08', 10, 480, 20);");
            statement.executeUpdate(
                    "INSERT INTO Orders VALUES (13347, 1004, 3003, '2021-04-02', '2021-04-08', 10, 240, 20);");
            statement.executeUpdate(
                    "INSERT INTO Orders VALUES (13348, 1002, 3001, '2021-05-02', '2021-05-08', 10, 400, 20);");
            statement.executeUpdate(
                    "INSERT INTO Orders VALUES (13349, 1003, 3002, '2021-05-02', '2021-05-08', 10, 480, 20);");
            statement.executeUpdate(
                    "INSERT INTO Orders VALUES (13350, 1004, 3003, '2021-06-02', '2021-06-08', 10, 240, 20);");
            statement.executeUpdate(
                    "INSERT INTO Orders VALUES (13351, 1002, 3001, '2021-04-02', '2021-04-08', 10, 400, 20);");
            statement.executeUpdate(
                    "INSERT INTO Orders VALUES (13352, 1003, 3002, '2021-05-02', '2021-05-08', 10, 480, 20);");
            statement.executeUpdate(
                    "INSERT INTO Orders VALUES (13353, 1002, 3001, '2021-03-06', '2021-04-08', 5, 200, 20);");
            statement.executeUpdate(
                    "INSERT INTO Orders VALUES (13354, 1003, 3001, '2021-03-15', '2021-05-08', 5, 240, 20);");

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

            statement.executeUpdate("Insert into Edits VALUES(6993,1001);");
            statement.executeUpdate("Insert into Edits VALUES(6995,1001);");
            statement.executeUpdate("Insert into Edits VALUES(6995,1002);");
            statement.executeUpdate("Insert into Edits VALUES(6997,1003);");
            statement.executeUpdate("Insert into Edits VALUES(6999,1001);");
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
                    "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3006,'2021-08-01','1001');");
            statement.executeUpdate(
                    "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3007,'2021-02-02','798.6');");
            statement.executeUpdate(
                    "INSERT INTO `DistributorPayments` (`account_number`, `payment_date`,`amount_paid`) VALUES (3008,'2021-06-08','999');");

            statement.executeUpdate(
                    "INSERT INTO `Books` (`publication_ID`, `ISBN`,`edition`, `publication_date`) VALUES (1001 ,123456 ,1,'2022-02-02');");
            statement.executeUpdate(
                    "INSERT INTO `Books` (`publication_ID`, `ISBN`,`edition`, `publication_date`) VALUES (1002 ,123457 ,1,'2022-04-03');");
            statement.executeUpdate(
                    "INSERT INTO `Books` (`publication_ID`, `ISBN`,`edition`, `publication_date`) VALUES (1003 ,123458 ,1,'2022-03-02');");
            statement.executeUpdate(
                    "INSERT INTO `Books` (`publication_ID`, `ISBN`,`edition`, `publication_date`) VALUES (1004 ,123459 ,1,'2022-04-12');");

            statement.executeUpdate(
                    "INSERT INTO `WritesArticles;` (`staff_ID`, `publication_ID`,`title`) VALUES (6994, 1008, 'technology today title');");
            statement.executeUpdate(
                    "INSERT INTO `WritesArticles;` (`staff_ID`, `publication_ID`,`title`) VALUES (6996, 1006, 'health today title');");
            statement.executeUpdate(
                    "INSERT INTO `WritesArticles;` (`staff_ID`, `publication_ID`,`title`) VALUES (6996, 1006, 'health today title2');");
            statement.executeUpdate(
                    "INSERT INTO `WritesArticles;` (`staff_ID`, `publication_ID`,`title`) VALUES (6996, 1007, 'fashion today title');");
            statement.executeUpdate(
                    "INSERT INTO `WritesArticles;` (`staff_ID`, `publication_ID`,`title`) VALUES (6996, 1007, 'fashion today title2');");
            statement.executeUpdate(
                    "INSERT INTO `WritesArticles;` (`staff_ID`, `publication_ID`,`title`) VALUES (6998, 1005, 'Science Direct Today');");
            statement.executeUpdate(
                    "INSERT INTO `WritesArticles;` (`staff_ID`, `publication_ID`,`title`) VALUES (6998, 1005, 'science today title2');");

            statement.executeUpdate(
                    "INSERT INTO `WritesBook;` (`staff_ID`, `publication_ID`) VALUES (6992, 1001);");
            statement.executeUpdate(
                    "INSERT INTO `WritesBook;` (`staff_ID`, `publication_ID`) VALUES (6992, 1002);");
            statement.executeUpdate(
                    "INSERT INTO `WritesBook;` (`staff_ID`, `publication_ID`) VALUES (6994, 1003);");
            statement.executeUpdate(
                    "INSERT INTO `WritesBook;` (`staff_ID`, `publication_ID`) VALUES (6996, 1004);");

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
                    + "(2001, '9191234567', 'charlotte', '2200, A Street, NC', 'bookstore', 'BookSell', 215, Jason)");

            statement.executeUpdate("INSERT INTO Distributors "
                    + "(account_number, phone , city, street_address, type, name, balance, contact_person) VALUES"
                    + "(2002, '9291234568', 'Raleigh', '2200, B Street, NC', 'wholesaler', 'BookDist', 0, Alex)");
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
            statement.executeUpdate("INSERT INTO Articles (publication_ID, title, text, creation_date) VALUES "
                    + "(1002,'WolfaDiet How to','ABC','2019-03-02')");

            statement.executeUpdate("INSERT INTO Articles (publication_ID, title, text, creation_date) VALUES "
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