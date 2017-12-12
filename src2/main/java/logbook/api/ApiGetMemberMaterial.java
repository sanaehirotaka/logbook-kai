package logbook.api;

import java.time.Duration;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.AppConfig;
import logbook.bean.Material;
import logbook.internal.JsonHelper;
import logbook.internal.LogWriter;
import logbook.internal.Logs;
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
            Duration duration = Duration.ofMillis(System.currentTimeMillis() - AppCondition.get()
                    .getWroteMaterialLogLast());
            if (duration.compareTo(Duration.ofSeconds(AppConfig.get().getMaterialLogInterval())) >= 0) {
                Map<Integer, Material> material = JsonHelper.toMap(array, Material::getId, Material::toMaterial);
                new LogWriter()
                        .header(Logs.MATERIAL_HEADER)
                        .file(Logs.MATERIAL)
                        .alterFile(Logs.MATERIAL_ALT)
                        .write(material, Logs::formatMaterial);
                AppCondition.get()
                        .setWroteMaterialLogLast(System.currentTimeMillis());
            }
        }
    }
}
