package logbook.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.tags.base.CharacterTag;
import com.jpexs.decompiler.flash.tags.base.ImageTag;

import logbook.bean.AppConfig;
import logbook.bean.ShipCollection;
import logbook.proxy.ContentListenerSpi;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * SWFファイルを処理します
 *
 */
public class SWFListener implements ContentListenerSpi {

    /** shipgraph -> shipid */
    private Map<String, Integer> shipgraphCache = new HashMap<>();

    @Override
    public boolean test(RequestMetaData request) {
        return request.getRequestURI().startsWith("/kcs/resources/");
    }

    @Override
    public void accept(RequestMetaData request, ResponseMetaData response) {
        try {
            String uri = request.getRequestURI();
            if (uri.startsWith("/kcs/resources/swf/ships/")) {
                this.ships(request, response);
            }
        } catch (Exception e) {
        }
    }

    /**
     * /kcs/resources/swf/ships/ を処理します
     *
     * @param request リクエストに含まれている情報
     * @param response レスポンスに含まれている情報
     * @throws IOException 入出力例外またはSWFファイルの解析に失敗した場合
     * @throws InterruptedException SWFファイルの解析に失敗した場合
     */
    void ships(RequestMetaData request, ResponseMetaData response) throws IOException, InterruptedException {
        if (response.getResponseBody().isPresent()) {
            InputStream in = response.getResponseBody().get();

            // /kcs/resources/swf/ships/[shipgraph].swf
            String uri = request.getRequestURI();
            int nameIndex = uri.lastIndexOf('/');
            int extIndex = uri.lastIndexOf('.');
            String shipgraph = uri.substring(nameIndex + 1, extIndex);

            Integer shipid = this.shipgraphCache.get(shipgraph);
            if (shipid == null) {
                synchronized (this.shipgraphCache) {
                    ShipCollection.get()
                            .getShipMap()
                            .forEach((k, v) -> this.shipgraphCache.put(v.getGraph(), k));
                }
                shipid = this.shipgraphCache.get(shipgraph);
            }
            if (shipid != null) {
                // ./resources/ships/[shipid]
                Path dir = Paths.get(AppConfig.get().getResourcesDir(), "ships", Integer.toString(shipid));
                this.storeShipImages(dir, in);
            }
        }
    }

    /**
     * 画像ファイルを保存します
     * @param dir 保存先のディレクトリ
     * @param in SWFファイル
     * @throws IOException 入出力例外またはSWFファイルの解析に失敗した場合
     * @throws InterruptedException SWFファイルの解析に失敗した場合
     */
    void storeShipImages(Path dir, InputStream in) throws IOException, InterruptedException {
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        SWF swf = new SWF(in, false);
        for (Entry<Integer, CharacterTag> e : swf.getCharacters().entrySet()) {
            if (e.getValue() instanceof ImageTag) {
                ImageTag img = (ImageTag) e.getValue();
                String ext;
                switch (img.getImageFormat()) {
                case JPEG:
                    ext = "jpg";
                    break;
                default:
                    ext = img.getImageFormat().toString().toLowerCase();
                    break;
                }

                Path file = dir.resolve(img.getCharacterId() + "." + ext);

                try (OutputStream out = Files.newOutputStream(file)) {
                    IOUtils.copy(img.getImageData(), out);
                }
            }
        }
    }
}
