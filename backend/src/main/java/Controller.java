import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.get;

public class Controller {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static Route getAllGoodsRoute() {
        Route getRoute = new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String allGoods = mapper.writeValueAsString(ShopDAO.findAllGoods());
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
    }
}
