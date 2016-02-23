package logbook.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.tags.base.CharacterTag;
import com.jpexs.decompiler.flash.tags.base.ImageTag;

import logbook.bean.ShipDescription;
import logbook.bean.ShipDescriptionCollection;
import logbook.proxy.ContentListenerSpi;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * SWFファイルを処理します
 *
 */
public class SWFListener implements ContentListenerSpi {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

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
                    ShipDescriptionCollection.get()
                            .getShipMap()
                            .forEach((k, v) -> this.shipgraphCache.put(v.getGraph(), k));
                }
                shipid = this.shipgraphCache.get(shipgraph);
            }
            if (shipid != null) {
                // ./resources/ships/[name]
                Path dir = ShipDescription.getResourcePathDir(ShipDescriptionCollection.get()
                        .getShipMap()
                        .get(shipid));
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
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
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
                    try (InputStream data = img.getImageData()) {
                        int n = 0;
                        while (-1 != (n = data.read(buffer))) {
                            out.write(buffer, 0, n);
                        }
                    }
                }
            }
        }
    }
}
