package logbook.internal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.BiConsumer;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import logbook.bean.AppConfig;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
import logbook.bean.Spritesmith;
import logbook.proxy.ContentListenerSpi;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * 画像ファイルを処理します
 *
 */
public class ImageListener implements ContentListenerSpi {

    @Override
    public boolean test(RequestMetaData request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/kcs2/resources/") || uri.startsWith("/kcs2/img/");
    }

    @Override
    public void accept(RequestMetaData request, ResponseMetaData response) {
        try {
            String uri = request.getRequestURI();
            // 艦娘画像
            if (uri.startsWith("/kcs2/resources/ship/")) {
                this.ships(request, response);
            }
            // 汎用画像
            if (uri.startsWith("/kcs2/img/common/")) {
                this.common(request, response);
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("画像ファイル処理中に例外が発生しました", e);
        }
    }

    private void ships(RequestMetaData request, ResponseMetaData response) throws IOException {
        String uri = URI.create(request.getRequestURI()).getPath();
        String name = null;

        if (uri.contains("/banner/"))
            name = "1.png";
        if (uri.contains("/banner_dmg/"))
            name = "3.png";
        if (uri.contains("/card/"))
            name = "5.png";
        if (uri.contains("/card_dmg/"))
            name = "7.png";
        if (uri.contains("/full/"))
            name = "17.png";
        if (uri.contains("/full_dmg/"))
            name = "19.png";

        if (name != null) {
            ShipImageCacheStrategy strategy = AppConfig.get().getShipImageCacheStrategy();
            if (strategy == null || strategy.getFileNames() == null || strategy.getFileNames().contains(name)) {
                this.storeShipImage(name, request, response);
            }
        }
    }

    private void storeShipImage(String name, RequestMetaData request, ResponseMetaData response) throws IOException {
        String uri = URI.create(request.getRequestURI()).getPath();
        int nameIndex = uri.lastIndexOf('/');
        int extIndex = uri.lastIndexOf('_');
        String shipid = uri.substring(nameIndex + 1, extIndex);

        ShipMst shipMst = ShipMstCollection.get()
                .getShipMap()
                .get(Integer.parseInt(shipid));
        if (response.getResponseBody().isPresent()) {
            // 画像ファイルを再圧縮するオプション
            InputStream is;
            if (AppConfig.get().isShipImageCompress()) {
                is = this.compressImage(response.getResponseBody().get());
                name = name.replace(".png", ".jpg");
            } else {
                is = response.getResponseBody().get();
            }
            Path path = ShipMst.getResourcePathDir(shipMst)
                    .resolve(name);
            this.write(is, path);
        }
    }

    private void common(RequestMetaData request, ResponseMetaData response) throws IOException {
        String uri = request.getRequestURI();
        Path dir = Paths.get(AppConfig.get().getResourcesDir(), "common");
        Path path = dir.resolve(Paths.get(URI.create(uri).getPath()).getFileName());
        if (response.getResponseBody().isPresent()) {
            this.write(response.getResponseBody().get(), path);

            String filename = String.valueOf(path.getFileName());
            // pngファイル
            Path pngPath = null;
            // jsonファイル
            Path jsonPath = null;

            // jsonファイルの場合
            if (filename.endsWith(".json")) {
                pngPath = path.resolveSibling(filename.replace(".json", ".png"));
                jsonPath = path;
            }
            // pngファイルの場合
            if (filename.endsWith(".png")) {
                pngPath = path;
                jsonPath = path.resolveSibling(filename.replace(".png", ".json"));
            }
            // 分解した画像の格納先
            Path spriteDir = pngPath.resolveSibling(filename.substring(0, filename.lastIndexOf('.')));

            this.sprite(spriteDir, pngPath, jsonPath);
        }
    }

    private void sprite(Path storeDir, Path imageSrc, Path jsonSrc) throws IOException {
        if (!Files.exists(imageSrc) || !Files.exists(jsonSrc)) {
            return;
        }
        if (!Files.exists(storeDir)) {
            Files.createDirectories(storeDir);
        }
        Spritesmith sprite;
        try (BufferedInputStream is = new BufferedInputStream(Files.newInputStream(jsonSrc))) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            sprite = mapper.readValue(is, Spritesmith.class);
        }
        try (BufferedInputStream is = new BufferedInputStream(Files.newInputStream(imageSrc))) {
            BufferedImage image = ImageIO.read(is);

            BiConsumer<String, Spritesmith.Frame> action = (k, v) -> {
                Spritesmith.Rect rect = v.getFrame();
                BufferedImage subimage = image.getSubimage(rect.getX(), rect.getY(), rect.getW(), rect.getH());
                try {
                    try (OutputStream out = new BufferedOutputStream(
                            Files.newOutputStream(storeDir.resolve(k + ".png")))) {
                        ImageIO.write(subimage, "png", out);
                    }
                } catch (Exception e) {
                    LoggerHolder.get().warn("画像ファイル処理中に例外が発生しました[src=" + imageSrc + "]", e);
                }
            };
            sprite.getFrames().forEach(action);
        }
    }

    private void write(InputStream from, Path to) throws IOException {
        Path parent = to.getParent();
        if (parent != null) {
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
        }
        Path temp = Files.createTempFile("ImageListener-", "");
        Files.copy(from, temp, StandardCopyOption.REPLACE_EXISTING);
        Files.move(temp, to, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 画像をjpeg形式で再圧縮します。
     *
     * @param in InputStream
     * @return InputStream
     */
    private InputStream compressImage(InputStream in) {
        try {
            BufferedImage image = ImageIO.read(in);

            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
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
                        iwp.setCompressionQuality(0.8f);
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
