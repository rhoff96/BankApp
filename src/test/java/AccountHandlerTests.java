import org.example.AccountHandler;
import org.example.dao.AccountDao;
import org.example.dao.BaseDaoTests;
import org.example.dao.JdbcAccountDao;
import org.example.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountHandlerTests extends BaseDaoTests {

    private AccountDao testAd;

    @Before
    public void setup(){
        testAd = new JdbcAccountDao(dataSource);

    }    @Test
    public void when_valid_input_then_create_account_creates_account() {
        AccountHandler handler = new AccountHandler(1, "Checking",testAd);
        Account actual = handler.createAccount();
        Account expected = new Account(6,1,"Checking");
        expected.setAccountBalance(BigDecimal.ZERO);
        Assert.assertEquals("Create account should return correct account",expected,actual);
    }
}
