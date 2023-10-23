import org.example.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Scanner;

public class LogTests {
    private final UserInterface ui = new UserInterface();
    private final User currentUser = new User("TestUser", "admin", ui);
    private final CheckingAccount acc = new CheckingAccount("Checking", 123);
    private final Log log = new Log();

    @Test
    public void when_deposit_log_correct_info() {
        currentUser.currentAccount = acc;
        currentUser.userAccounts.add(acc);
        acc.setBalance(new BigDecimal("150"));
        acc.deposit(new BigDecimal("50"));
        log.logEntry("test.txt", currentUser, acc, "Deposit $50");
        String expected = "TestUser: Checking Account #123 Deposit $50 Current Balance $200";
        String actual = "";
        File sourceFile = new File("test.txt");
        try {
            Scanner fileReader = new Scanner(sourceFile);
            while (fileReader.hasNextLine()) {
                actual = fileReader.nextLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals("Log Entry should record correct info to log",expected,actual);
    }
}
