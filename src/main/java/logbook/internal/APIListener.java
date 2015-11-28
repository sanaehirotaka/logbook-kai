package logbook.internal;

import logbook.proxy.ContentListenerSpi;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

public class APIListener implements ContentListenerSpi {

    @Override
    public boolean test(RequestMetaData requestMetaData) {
        return requestMetaData.getRequestURI().startsWith("/kcsapi/");
    }

    @Override
    public void accept(RequestMetaData requestMetaData, ResponseMetaData responseMetaData) {
        try {
        } catch (Exception e) {
        }
    }

}
