package logbook.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
            if (uri.startsWith("/kcs/resources/swf/icons.swf")) {
                this.icons(request, response);
            }
            if (uri.startsWith("/kcs/resources/swf/commonAssets.swf")) {
                this.common(request, response);
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn("SWF処理中に例外が発生しました", e);
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
                this.storeImages(dir, in);
            }
        }
    }

    /**
     * /kcs/resources/swf/icons.swf を処理します
     * <pre>
     * 黒丸背景あり 32px×32px
     * 1.png   =空(黒丸のみ)
     * 3.png   =小口径主砲
     * 5.png   =中口径主砲
     * 7.png   =大口径主砲
     * 9.png   =副砲
     * 11.png  =魚雷
     * 13.png  =艦上戦闘機
     * 15.png  =艦上爆撃機
     * 17.png  =艦上攻撃機
     * 19.png  =艦上偵察機
     * 21.png  =水上機
     * 23.png  =電探
     * 25.png  =対空強化弾
     * 27.png  =対艦強化弾
     * 29.png  =応急修理要員
     * 31.png  =対空機銃
     * 33.png  =高角砲
     * 35.png  =爆雷
     * 37.png  =ソナー
     * 39.png  =機関部強化
     * 41.png  =上陸用舟艇
     * 43.png  =オートジャイロ
     * 45.png  =対潜哨戒機
     * 47.png  =追加装甲
     * 49.png  =探照灯
     * 51.png  =簡易輸送部材
     * 53.png  =艦艇修理施設
     * 55.png  =照明弾
     * 57.png  =司令部施設
     * 59.png  =航空要員
     * 61.png  =高射装置
     * 63.png  =対地装備
     * 65.png  =水上艦要員
     * 67.png  =大型飛行艇
     * 69.png  =戦闘糧食
     * 71.png  =補給物資
     *
     * 背景なし 32px×32px
     * 74.png  =小口径主砲
     * 76.png  =中口径主砲
     * 78.png  =大口径主砲
     * 80.png  =副砲
     * 82.png  =魚雷
     * 84.png  =艦上戦闘機
     * 86.png  =艦上爆撃機
     * 88.png  =艦上攻撃機
     * 90.png  =艦上偵察機
     * 92.png  =水上機
     * 94.png  =電探
     * 96.png  =対空強化弾
     * 98.png  =対艦強化弾
     * 100.png =応急修理要員
     * 102.png =対空機銃
     * 104.png =高角砲
     * 106.png =爆雷
     * 108.png =ソナー
     * 110.png =機関部強化
     * 112.png =上陸用舟艇
     * 114.png =オートジャイロ
     * 116.png =対潜哨戒機
     * 118.png =追加装甲
     * 120.png =探照灯
     * 122.png =簡易輸送部材
     * 124.png =艦艇修理施設
     * 126.png =照明弾
     * 128.png =司令部施設
     * 130.png =航空要員
     * 132.png =高射装置
     * 134.png =対地装備
     * 136.png =水上艦要員
     * 138.png =大型飛行艇
     * 140.png =戦闘糧食
     * 142.png =補給物資
     * </pre>
     * @param request リクエストに含まれている情報
     * @param response レスポンスに含まれている情報
     * @throws IOException 入出力例外またはSWFファイルの解析に失敗した場合
     * @throws InterruptedException SWFファイルの解析に失敗した場合
     */
    void icons(RequestMetaData request, ResponseMetaData response) throws IOException, InterruptedException {
        if (response.getResponseBody().isPresent()) {
            InputStream in = response.getResponseBody().get();
            Path dir = Ships.getItemResourcePathDir();
            this.storeItemImages(dir, in);
        }
    }

    /**
     * /kcs/resources/swf/commonAssets.swf を処理します
     *
     * @param request リクエストに含まれている情報
     * @param response レスポンスに含まれている情報
     * @throws IOException 入出力例外またはSWFファイルの解析に失敗した場合
     * @throws InterruptedException SWFファイルの解析に失敗した場合
     */
    void common(RequestMetaData request, ResponseMetaData response) throws IOException, InterruptedException {
        if (response.getResponseBody().isPresent()) {
            InputStream in = response.getResponseBody().get();
            Path dir = ShipImage.getResourcePathDir();
            this.storeImages(dir, in);
        }
    }

    /**
     * 画像ファイルを保存します
     * @param dir 保存先のディレクトリ
     * @param in SWFファイル
     * @throws IOException 入出力例外またはSWFファイルの解析に失敗した場合
     * @throws InterruptedException SWFファイルの解析に失敗した場合
     */
    void storeImages(Path dir, InputStream in) throws IOException, InterruptedException {
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

    /**
     * 画像ファイルを保存します
     * @param dir 保存先のディレクトリ
     * @param in SWFファイル
     * @throws IOException 入出力例外またはSWFファイルの解析に失敗した場合
     * @throws InterruptedException SWFファイルの解析に失敗した場合
     */
    void storeItemImages(Path dir, InputStream in) throws IOException, InterruptedException {
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

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(SWFListener.class);
    }
}
