package logbook.api;

import javax.json.JsonObject;

import logbook.bean.Mapinfo;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/mapinfo
 *
 */
@API("/kcsapi/api_get_member/mapinfo")
public class ApiGetMemberMapinfo implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject object = json.getJsonObject("api_data");
        if (object != null) {
            Mapinfo mapinfo = Mapinfo.get();

            // 海域
            if (object.getJsonArray("api_map_info") != null) {
                mapinfo.setMapInfo(JsonHelper.toList(object.getJsonArray("api_map_info"), Mapinfo.MapInfo::toMapInfo));
            }
            // 基地航空隊
            if (object.getJsonArray("api_air_base") != null) {
                mapinfo.setAirBase(JsonHelper.toList(object.getJsonArray("api_air_base"), Mapinfo.AirBase::toAirBase));
            } else {
                mapinfo.getAirBase().clear();
            }
        }
    }

}
