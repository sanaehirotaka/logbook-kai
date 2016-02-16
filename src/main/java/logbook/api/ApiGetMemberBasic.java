package logbook.api;

import javax.json.JsonObject;

import logbook.bean.Basic;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/basic
 *
 */
@API("/kcsapi/api_get_member/basic")
public class ApiGetMemberBasic implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            Basic.updateBasic(Basic.get(), data);
        }
    }

}
