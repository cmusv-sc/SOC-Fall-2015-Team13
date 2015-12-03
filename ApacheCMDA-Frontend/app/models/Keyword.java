package models;

import com.fasterxml.jackson.databind.JsonNode;
import util.APICall;
import util.Constants;



/**
 * Created by bluebyte60 on 12/2/15.
 */

public class Keyword {

    private static long id;
    private static String keyword;
    private static final String PUT_KEYWORD = Constants.NEW_BACKEND + "search/keyword/put/";
    private static final String GET_RECOMMENDATION = Constants.NEW_BACKEND + "search/keyword/get";

    public Keyword() {
    }

    public static void put(String word) {
        APICall.callAPI(PUT_KEYWORD + word);
    }

    public static String recommendation() {
        JsonNode jsonNode = APICall.callAPI(GET_RECOMMENDATION);
        return jsonNode.toString();
    }
}
