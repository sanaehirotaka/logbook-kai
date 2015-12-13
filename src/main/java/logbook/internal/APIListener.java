package logbook.internal;

import java.io.InputStream;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.GZIPInputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.logging.log4j.LogManager;

import logbook.Messages;
import logbook.api.API;
import logbook.api.APIListenerSpi;
import logbook.proxy.ContentListenerSpi;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * APIを受け取りJSONをAPIListenerSpiを実装したサービスクラスに送ります
 *
 */
public final class APIListener implements ContentListenerSpi {

    private final List<APIListenerSpi> services;

    public APIListener() {
        ServiceLoader<APIListenerSpi> loader = ServiceLoader.load(
                APIListenerSpi.class,
                PluginContainer.getInstance().getClassLoader());

        this.services = StreamSupport.stream(loader.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public boolean test(RequestMetaData requestMetaData) {
        return requestMetaData.getRequestURI().startsWith("/kcsapi/"); //$NON-NLS-1$
    }

    @Override
    public void accept(RequestMetaData requestMetaData, ResponseMetaData responseMetaData) {
        try {
            // レスポンスのJSONを復号します
            InputStream stream = responseMetaData.getResponseBody().get();
            // Check header
            int header = (stream.read() | (stream.read() << 8));
            stream.reset();
            if (header == GZIPInputStream.GZIP_MAGIC) {
                stream = new GZIPInputStream(stream);
            }
            // レスポンスボディのJSONはsvdata=から始まるので除去します
            int read;
            while (((read = stream.read()) != -1) && (read != '=')) {
            }

            try (JsonReader jsonreader = Json.createReader(stream)) {
                JsonObject json = jsonreader.readObject();

                String requri = requestMetaData.getRequestURI();

                for (APIListenerSpi service : this.services) {
                    Runnable task = () -> {
                        // アノテーションからURIを取り出し、リクエストのURIと一致するか調べます
                        API target = service.getClass().getAnnotation(API.class);
                        boolean test = false;
                        if (target != null) {
                            for (String uri : target.values()) {
                                if (requri.equals(uri)) {
                                    test = true;
                                    break;
                                }
                            }
                        } else {
                            test = true;
                        }
                        try {
                            if (test) {
                                service.accept(json, requestMetaData, responseMetaData);
                            }
                        } catch (Exception e) {
                            LogManager.getLogger(APIListener.class).warn(Messages.getString("APIListener.1"), e); //$NON-NLS-1$
                            LogManager.getLogger(APIListener.class).warn(json);
                        }
                    };
                    ThreadManager.getExecutorService().submit(task);
                }
            }
        } catch (Exception e) {
            LogManager.getLogger(APIListener.class).warn(Messages.getString("APIListener.2"), e); //$NON-NLS-1$
        }
    }

}
