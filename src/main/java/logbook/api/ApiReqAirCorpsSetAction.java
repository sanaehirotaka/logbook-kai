package logbook.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import logbook.bean.Mapinfo;
import logbook.bean.Mapinfo.AirBase;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_air_corps/set_action
 *
 */
@API("/kcsapi/api_req_air_corps/set_action")
public class ApiReqAirCorpsSetAction implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        int areaId = Integer.parseInt(req.getParameterMap().get("api_area_id").get(0));
        List<Integer> baseIds = Arrays.stream(req.getParameterMap().get("api_base_id").get(0).split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        List<Integer> kinds = Arrays.stream(req.getParameterMap().get("api_action_kind").get(0).split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        for (int i = 0; i < baseIds.size(); i++) {
            int index = i;
            AirBase airBase = Mapinfo.get()
                    .getAirBase()
                    .stream()
                    .filter(b -> b.getAreaId() == areaId)
                    .filter(b -> b.getRid().equals(baseIds.get(index)))
                    .findFirst()
                    .orElse(null);
            if (airBase != null) {
                airBase.setActionKind(kinds.get(index));
            }
        }
    }
}
