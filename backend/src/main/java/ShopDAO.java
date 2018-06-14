import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class ShopDAO {
    private static final ObjectMapper mapper = new ObjectMapper();//преобразует файлы.
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopDAO.class);

    public static synchronized List<Good> findAll() {
        try {
            FileInputStream fis = new FileInputStream("goods.json");// открытие потока, для чтения файла goods.json

            List<Good> goods = mapper.readValue(fis, new TypeReference<List<Good>>() {
            });
            return goods;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public static synchronized Good findByName(String name) {
        List<Good> goods = findAll();
        for (Good good : goods) {
            if (good.name.equalsIgnoreCase(name)) {
                return good;
            }
        }
        return null;
    }

    public static synchronized boolean save(Good good) {
        List<Good> goods = findAll();
        goods.add(good);
        try {
            String listOfGoods = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(goods);
            FileOutputStream fos = new FileOutputStream("goods.json");//открытие потока, для чтения файла.
            fos.write(listOfGoods.getBytes());
            fos.flush();
            fos.close();//закрытие потока
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
        return true;

    }

    public static synchronized void deleteByName(String name) throws IOException {
        List<Good> goods = findAll();
        Iterator<Good> it = goods.iterator();
        while (it.hasNext()) {
            Good el = it.next();
            if (el.name.equals(name)) {
                it.remove();
            }
        }
        String listOfGoods = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(goods);
        FileOutputStream fos = new FileOutputStream("goods.json");//открытие потока, для чтения файла.
        fos.write(listOfGoods.getBytes());
        fos.flush();
        fos.close();
    }
}