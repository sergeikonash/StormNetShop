import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private static Route getAllGoodsRoute() {
        Route getRoute = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String allGoods = mapper.writeValueAsString(ShopDAO.findAll());
                return allGoods;
            }
        };
        return getRoute;
    }

    private static Route getFindGoodByNameRoute() {
        Route getRoute = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String goodName = request.queryParams("goodName");
                if (StringUtils.isBlank(goodName)) {
                    return "Please specify correct good name";
                }
                Good good = ShopDAO.findByName(goodName);
                if (good == null) {
                    return "Good with name " + goodName + " not found";
                }
                String json = mapper.writeValueAsString(good);
                return json;
            }
        };
        return getRoute;
    }

 /*   private static Route getFindGoodByPriceRoute() {
        Route getRoute = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String goodPrice = request.queryParams("goodPrice");
                if (StringUtils.isBlank(goodPrice)) {
                    return "Please specify correct good price";
                }
                int pr = convertStringToInt(goodPrice);
                List<Good> good = ShopDAO.findByPrice(pr);
                if (good == null) {
                    return "Good with name " + goodPrice + " not found";
                }
                String json = mapper.writeValueAsString(good);
                return json;
            }
        };
        return getRoute;
    }
*/
    public static void main(String[] args) {
/*        Route getRoute = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return "This is GET reguest";
            }
        };
        Route postRoute = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return "This is POST reguest";
            }
        };
        get("/simpleGet", getRoute);
        post("simplePost", postRoute);
        */
        get("/getAllGoods", getAllGoodsRoute());
        get("/getAllGoods", getAllGoodsRoute());
        get("/findGoodByName", getFindGoodByNameRoute());
      //  get("/getAllPrices", getFindGoodByPriceRoute());

        post("/authorization", (request, response) -> {
            try {
                String body = request.body();
                Account loginPassword = mapper.readValue(body, Account.class);
                String token = AccountService.checkAccountAndGetToken(loginPassword);
                ;
                response.header("Header", token);
                return token;
            } catch (JsonParseException | JsonMappingException e) {
                LOGGER.error(e.getMessage(), e);
            }
            response.status(400);
            return "error";
        });

        post("/addGood", getAddGoodRoute());
    }

    public static int convertStringToInt(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    private static Route getAddGoodRoute() {
        Route postRoute = (request, response) -> {
            String goodName = request.queryParams("name");
            String count = request.queryParams("count");
            String price = request.queryParams("price");
            String isValid = validateParametersForGood(goodName, count, price);
            if (StringUtils.isNotBlank(isValid)) {
                return isValid;
            }
            Integer priceInt = convertStringToInt(price);
            if (priceInt == null) {
                return "Please specify price as a number";
            }
            Integer contInt = convertStringToInt(count);
            if (contInt == null) {
                return "Please specify count as a number";
            }
            Good good = new Good(goodName, contInt, priceInt);
            String accessToken = request.headers("Header");
            System.out.println(accessToken);
            boolean result = ShopService.addGood(good, accessToken);
            if (result) {
                return "Good was successfully added";
            } else {
                return "Something went wrong during adding process";
            }
        };
        return postRoute;
    }

    public static String validateParametersForGood(String goodName, String count, String price) {
        if (StringUtils.isBlank(goodName)) {
            return "Please specify correct good name";
        }
        if (StringUtils.isBlank(goodName)) {
            return "Please specify correct good count";
        }
        if (StringUtils.isBlank(goodName)) {
            return "Please specify correct good price";
        }
        return StringUtils.EMPTY;
    }

}
