package logbook.api;

import javax.json.JsonObject;

import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

public interface APIListenerSpi {

    void accept(JsonObject json, RequestMetaData requestMetaData, ResponseMetaData responseMetaData);

}
