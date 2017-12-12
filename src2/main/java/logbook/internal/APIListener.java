package logbook.internal;

import java.io.InputStream;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.GZIPInputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logbook.Messages;
import logbook.api.API;
import logbook.api.APIListenerSpi;
import logbook.plugin.PluginContainer;
import logbook.proxy.ContentListenerSpi;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * APIを受け取りJSONをAPIListenerSpiを実装したサービスクラスに送ります
 *
 */
public final class APIListener implements ContentListenerSpi {

    private final Map<String, List<Pair>> services;

    private final List<Pair> all = new ArrayList<>();

    public APIListener() {
        ServiceLoader<APIListenerSpi> loader = ServiceLoader.load(
                APIListenerSpi.class,
                PluginContainer.getInstance().getClassLoader());

        Function<APIListenerSpi, Stream<Pair>> mapper = impl -> {
            API target = impl.getClass().getAnnotation(API.class);
            if (target != null) {
                return Arrays.stream(target.value())
                        .map(k -> new Pair(k, impl));
            } else {
                this.all.add(new Pair(null, impl));
            }
            return Stream.empty();
        };
        this.services = StreamSupport.stream(loader.spliterator(), false)
                .flatMap(mapper)
                .collect(Collectors.groupingBy(Pair::getKey));
    }

    @Override
    public boolean test(RequestMetaData requestMetaData) {
        String uri = requestMetaData.getRequestURI();
        return uri.startsWith("/kcsapi/") && (!this.all.isEmpty() || this.services.containsKey(uri)); //$NON-NLS-1$
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

                this.send(requestMetaData, responseMetaData, json);
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn(Messages.getString("APIListener.2"), e); //$NON-NLS-1$
        }
    }

    void send(RequestMetaData req, ResponseMetaData res, JsonObject json) {
        String uri = req.getRequestURI();
        List<Pair> pairs = this.services.getOrDefault(uri, Collections.emptyList());

        for (Pair pair : pairs) {
            Runnable task = () -> this.createTask(pair, json, req, res);
            ThreadManager.getExecutorService().submit(task);
        }

        for (Pair pair : this.all) {
            Runnable task = () -> this.createTask(pair, json, req, res);
            ThreadManager.getExecutorService().submit(task);
        }
    }

    private void createTask(Pair pair, JsonObject json, RequestMetaData req, ResponseMetaData res) {
        try {
            if (LoggerHolder.LOG.isDebugEnabled()) {
                String className = pair.getValue().getClass().getName();
                LoggerHolder.LOG.debug(Messages.getString("APIListener.0"), //$NON-NLS-1$
                        className, req.getRequestURI());
            }
            pair.getValue().accept(json, req, res);
        } catch (Exception e) {
            LoggerHolder.LOG.warn(Messages.getString("APIListener.1"), e); //$NON-NLS-1$
            LoggerHolder.LOG.warn(json);
        }
    }

    private static final class Pair extends SimpleImmutableEntry<String, APIListenerSpi>
            implements Entry<String, APIListenerSpi> {

        private static final long serialVersionUID = 1L;

        public Pair(String key, APIListenerSpi value) {
            super(key, value);
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(APIListener.class);
    }
}
