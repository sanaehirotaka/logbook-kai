package logbook.api;

import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.Material;
import logbook.bean.MaterialCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/material
 *
 */
@API("/kcsapi/api_get_member/material")
public class ApiGetMemberMaterial implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonArray array = json.getJsonArray("api_data");
        if (array != null) {
            Map<Integer, Material> map = MaterialCollection.get()
                    .getMaterialMap();
            map.putAll(JsonHelper.toMap(array, Material::getId, Material::toMaterial));
        }
    }

}
