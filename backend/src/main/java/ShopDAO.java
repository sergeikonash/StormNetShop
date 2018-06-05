import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ShopDAO {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static synchronized List<Good> findAllGoods() {
        try {
            FileInputStream fis = new FileInputStream("goods.json");
            List<Good> goods = mapper.readValue(fis, new TypeReference<List<Good>>() {
            });
            return goods;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }

    public static synchronized Good findByName(String name) {
        List<Good> goods = findAllGoods();
        for (Good good : goods) {
            if (good.name.equalsIgnoreCase(name)) {
                return good;
            }
        }
        return null;
    }
}
