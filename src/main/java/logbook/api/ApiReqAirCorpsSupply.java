package logbook.api;

import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

import logbook.bean.Mapinfo;
import logbook.bean.Mapinfo.AirBase;
import logbook.bean.Mapinfo.PlaneInfo;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_air_corps/supply
 *
 */
@API("/kcsapi/api_req_air_corps/supply")
public class ApiReqAirCorpsSupply implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject object = json.getJsonObject("api_data");
        if (object != null) {
            int areaId = Integer.parseInt(req.getParameterMap().get("api_area_id").get(0));
            int baseId = Integer.parseInt(req.getParameterMap().get("api_base_id").get(0));

            AirBase airBase = Mapinfo.get()
                    .getAirBase()
                    .stream()
                    .filter(b -> b.getAreaId() == areaId)
                    .filter(b -> b.getRid() == baseId)
                    .findFirst()
                    .orElse(null);
            if (airBase != null) {
                List<PlaneInfo> infos = airBase.getPlaneInfo();
                Map<Integer, PlaneInfo> supplys = JsonHelper.toMap(object.getJsonArray("api_plane_info"),
                        PlaneInfo::getSquadronId, PlaneInfo::toPlaneInfo);

                for (int i = 0; i < infos.size(); i++) {
                    PlaneInfo newPlane = supplys.get(infos.get(i).getSquadronId());
                    if (newPlane != null) {
                        infos.set(i, newPlane);
                    }
                }
            }
        }
    }
}
