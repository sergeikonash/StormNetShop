import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountService {
    private static final String SALT = "Stormnet";
    private static final List<String> ACCESS_TOKENS = new ArrayList<>();

    public static String checkAccountAndGetToken(Account account) {
        String token;
        Account accountFromFile = AccountDAO.findAccountByName(account.getName());
        if (accountFromFile == null) {
            throw new IllegalArgumentException("The are no account in file");
        }
        if (!Objects.equals(account.getName(), accountFromFile.getName()) || !Objects.equals(account.getPassword(), accountFromFile.getPassword())) {
            throw new IllegalArgumentException("Incorrect login or password");
        }
        String passWithSalt = SALT + account.getPassword();
        token = DigestUtils.sha1Hex(passWithSalt);
        ACCESS_TOKENS.add(token);
        return token;
    }

    public static List getAccessTokens() {
        System.out.println(ACCESS_TOKENS);
        return ACCESS_TOKENS;
    }
}
