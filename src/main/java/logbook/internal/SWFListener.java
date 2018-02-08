package logbook.internal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.tags.DefineSpriteTag;
import com.jpexs.decompiler.flash.tags.base.CharacterTag;
import com.jpexs.decompiler.flash.tags.base.ImageTag;
import com.jpexs.helpers.Cache;

import logbook.bean.AppConfig;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
import logbook.proxy.ContentListenerSpi;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * SWFファイルを処理します
 *
 */
public class SWFListener implements ContentListenerSpi {

    public SWFListener() {
        Cache.setStorageType(Cache.STORAGE_MEMORY);
    }

    @Override
    public boolean test(RequestMetaData request) {
        return request.getRequestURI().startsWith("/kcs/resources/swf/");
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
            LoggerHolder.get().warn("SWF処理中に例外が発生しました", e);
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

            Map<Integer, ShipMst> shipMstMap = ShipMstCollection.get()
                    .getShipMap();

            ShipMst shipMst = shipMstMap.entrySet().stream()
                    .filter(e -> shipgraph.equals(e.getValue().getGraph()))
                    .map(Map.Entry::getValue)
                    .findAny()
                    .orElse(null);

            if (shipMst != null) {
                // 艦娘画像キャッシュ設定
                ShipImageCacheStrategy cacheStrategy = AppConfig.get().getShipImageCacheStrategy();
                if (cacheStrategy == ShipImageCacheStrategy.USED && shipMst.getAfterlv() == null) {
                    // 敵艦の場合は1.jpgのみしか無いのでLIMITに変える
                    cacheStrategy = ShipImageCacheStrategy.LIMIT;
                }

                synchronized (shipMst) {
                    // ./resources/ships/[name]
                    Path dir = ShipMst.getResourcePathDir(shipMst);
                    this.storeShipImages(dir, in, cacheStrategy);
                }
            }
        }
    }

    /**
     * /kcs/resources/swf/icons.swf を処理します
     *
     * @param request リクエストに含まれている情報
     * @param response レスポンスに含まれている情報
     * @throws IOException 入出力例外またはSWFファイルの解析に失敗した場合
     * @throws InterruptedException SWFファイルの解析に失敗した場合
     */
    void icons(RequestMetaData request, ResponseMetaData response) throws IOException, InterruptedException {
        if (response.getResponseBody().isPresent()) {
            InputStream in = response.getResponseBody().get();
            Path dir = Items.getItemResourcePathDir();
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
            Path dir = Paths.get(AppConfig.get().getResourcesDir(), "common");
            this.storeCommonImages(dir, in);
        }
    }

    /**
     * 画像ファイルを保存します
     * @param dir 保存先のディレクトリ
     * @param in SWFファイル
     * @param strategy 艦娘画像キャッシュ設定
     * @throws IOException 入出力例外またはSWFファイルの解析に失敗した場合
     * @throws InterruptedException SWFファイルの解析に失敗した場合
     */
    void storeShipImages(Path dir, InputStream in, ShipImageCacheStrategy strategy)
            throws IOException, InterruptedException {
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        SWF swf = new SWF(in, false);
        for (Entry<Integer, CharacterTag> e : swf.getCharacters().entrySet()) {
            if (e.getValue() instanceof ImageTag) {
                ImageTag img = (ImageTag) e.getValue();
                String ext = this.imageExt(img);
                String name = img.getCharacterId() + "." + ext;

                InputStream imageData = img.getImageData();
                if (strategy == null || strategy.getFileNames() == null || strategy.getFileNames().contains(name)) {
                    // 画像ファイルを再圧縮するオプション
                    if (AppConfig.get().isShipImageCompress()) {
                        InputStream compressedImageData = this.compressImage(imageData);
                        if (compressedImageData != null) {
                            imageData = compressedImageData;
                            name = img.getCharacterId() + ".jpg";
                        } else {
                            imageData.reset();
                        }
                    }
                    Path file = dir.resolve(name);
                    Files.copy(imageData, file, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        synchronized (Cache.class) {
            Cache.clearAll();
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
        for (Entry<Integer, CharacterTag> e : swf.getCharacters().entrySet()) {
            if (e.getValue() instanceof ImageTag) {
                ImageTag img = (ImageTag) e.getValue();
                String ext = this.imageExt(img);

                Path file = dir.resolve(img.getCharacterId() + "." + ext);

                Files.copy(img.getImageData(), file, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        Cache.clearAll();
    }

    /**
     * 画像ファイルを保存します
     * @param dir 保存先のディレクトリ
     * @param in SWFファイル
     * @throws IOException 入出力例外またはSWFファイルの解析に失敗した場合
     * @throws InterruptedException SWFファイルの解析に失敗した場合
     */
    void storeCommonImages(Path dir, InputStream in) throws IOException, InterruptedException {
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        SWF swf = new SWF(in, false);

        // SpriteTagとImageTagを抽出
        List<CharacterTag> tags = swf.getCharacters()
                .values()
                .stream()
                .filter(tag -> tag instanceof DefineSpriteTag || tag instanceof ImageTag)
                .sorted(Comparator.comparing(CharacterTag::getCharacterId))
                .collect(Collectors.toList());
        // ImageTag,ImageTag...,SpriteTag のような順番の繰り返しで並んでいる(そうなっていないのもある)ので見やすいように逆順にする
        // DefineBitsJPEG3 (408)  <--  res.common.MCBannerSmokeImgの子ImageTag 1
        // DefineBitsJPEG3 (410)  <--  res.common.MCBannerSmokeImgの子ImageTag 2
        // DefineBitsJPEG3 (412)  <--  res.common.MCBannerSmokeImgの子ImageTag 3
        // DefineSprite (414: res.common.MCBannerSmokeImg)
        Collections.reverse(tags);

        // SpriteTagをキーにしたMapを作る
        Map<String, List<ImageTag>> tagMap = new HashMap<>();
        String className = null;
        for (CharacterTag tag : tags) {
            if (tag instanceof DefineSpriteTag) {
                className = ((DefineSpriteTag) tag).getClassName();
            }
            if (tag instanceof ImageTag && className != null) {
                // 逆順になっているので常に先頭にadd
                tagMap.computeIfAbsent(className, k -> new ArrayList<>())
                        .add(0, (ImageTag) tag);
            }
        }
        // 保存
        for (String key : tagMap.keySet()) {
            List<ImageTag> sub = tagMap.get(key);
            for (int i = 0; i < sub.size(); i++) {
                ImageTag tag = sub.get(i);
                Path file = dir.resolve(key + "_" + i + "." + this.imageExt(tag));
                Files.copy(tag.getImageData(), file, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        Cache.clearAll();
    }

    /**
     * ImageTagから拡張子を取得します
     *
     * @param tag ImageTag
     * @return tagのファイルフォーマットに対応する拡張子
     */
    String imageExt(ImageTag tag) {
        String ext;
        switch (tag.getImageFormat()) {
        case JPEG:
            ext = "jpg";
            break;
        default:
            ext = tag.getImageFormat().toString().toLowerCase();
            break;
        }
        return ext;
    }

    /**
     * 画像をjpeg形式で再圧縮します。
     *
     * @param in InputStream
     * @return InputStream
     */
    InputStream compressImage(InputStream in) {
        try {
            BufferedImage image = ImageIO.read(in);

            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics gc = canvas.createGraphics();
            gc.setColor(Color.WHITE);
            gc.fillRect(0, 0, width, height);
            gc.drawImage(image, 0, 0, null);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(out)) {
                ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                try {

                    ImageWriteParam iwp = writer.getDefaultWriteParam();
                    if (iwp.canWriteCompressed()) {
                        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        iwp.setCompressionQuality(0.9f);
                    }
                    writer.setOutput(ios);
                    writer.write(null, new IIOImage(canvas, null, null), iwp);
                } finally {
                    writer.dispose();
                }
            }
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }
}
