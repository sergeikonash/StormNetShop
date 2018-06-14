import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class AccountDAO {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDAO.class);

    public static synchronized List<Account> findAccounts() {
        try (FileInputStream fis = new FileInputStream("account.json")) {
            return mapper.readValue(fis, new TypeReference<List<Account>>() {
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
            return Collections.emptyList();
    }

    //метод проверяет на наличие в листе передаваемого имени, возвращает акаунт при совпадении
    public static synchronized Account findAccountByName(String name){
        List<Account> accounts = findAccounts();
        for (Account account: accounts){
            if (name.equals(account.getName())){
                return account;
            }
        }
        return null;
    }

    //тот же метод, что и предыдущий, написаный через лямбды
    public static synchronized Optional<Account> findAccountByNameLambda(String name){
        return findAccounts().stream()
                .filter(account->name.equals(account.getName()))
                .findFirst();
    }

}
