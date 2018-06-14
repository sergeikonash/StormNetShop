import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public class ShopService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopService.class);

    public static boolean addGood(Good good, String accessToken) {
        boolean isUserLoggedIn = Optional.ofNullable(AccountService.getAccessTokens()).map(Collection::stream).map(stringStream -> stringStream.anyMatch(accessToken::equals)).orElse(false);
        if (!isUserLoggedIn) {
            return false;
        }
        Good goodInFile = ShopDAO.findByName(good.name);
        try {
            if (goodInFile == null) {
                return ShopDAO.save(good);
            } else {
                ShopDAO.deleteByName(good.name);
                Good newGood = new Good(goodInFile.name, goodInFile.count + good.count, good.price);
                return ShopDAO.save(newGood);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }
}