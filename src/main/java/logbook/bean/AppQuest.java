package logbook.bean;

import java.io.InputStream;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import logbook.bean.QuestList.Quest;
import logbook.internal.LoggerHolder;
import logbook.internal.Logs;
import logbook.plugin.PluginServices;
import lombok.Data;

/**
 * 任務
 */
@Data
public class AppQuest implements Serializable {

    private static final long serialVersionUID = 4212109733911812553L;

    /** デイリー */
    private static final int DAILY = 1;
    /** ウィークリー */
    private static final int WEEKLY = 2;
    /** マンスリー */
    private static final int MONTHLY = 3;
    /** 単発 */
    private static final int ONECE = 4;
    /** クォータリー */
    private static final int QUARTRELY = 5;

    /** No */
    private Integer no;

    /** 任務 */
    private Quest quest;

    /** 期限 */
    private String expire;

    /**
     * 任務を構築します
     *
     * @param quest 任務
     * @return {@link AppQuest}
     */
    public static AppQuest toAppQuest(Quest quest) {
        AppQuest bean = new AppQuest();
        bean.setNo(quest.getNo());
        bean.setQuest(quest);

        ZonedDateTime base = ZonedDateTime.now(ZoneId.of("GMT+04:00"))
                .truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime expire = null;

        int type = quest.getType();

        InputStream is = PluginServices.getResourceAsStream("logbook/quest/" + quest.getNo() + ".json");
        if (is != null) {
            String resetType = null;
            try {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.enable(Feature.ALLOW_COMMENTS);
                    AppQuestCondition condition = mapper.readValue(is, AppQuestCondition.class);
                    resetType = condition.getResetType();
                } finally {
                    is.close();
                }
            } catch (Exception e) {
                LoggerHolder.get().info("任務設定ファイルが読み込めませんでした。", e);
            }
            if (resetType != null) {
                switch (resetType) {
                case "デイリー":
                    type = DAILY;
                    break;
                case "ウィークリー":
                    type = WEEKLY;
                    break;
                case "マンスリー":
                    type = MONTHLY;
                    break;
                case "単発":
                    type = ONECE;
                    break;
                case "クオータリー":
                case "クォータリー":
                    type = QUARTRELY;
                    break;
                }
            }
        }

        if (type == DAILY) {
            // 1=デイリー
            // 1日加算
            expire = base.plusDays(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (type == WEEKLY) {
            // 2=ウィークリー
            // 7日加算して曜日(1(月曜日)から7(日曜日))-1を減算する
            expire = base.plusWeeks(1)
                    .minusDays(base.getDayOfWeek().getValue() - 1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (type == MONTHLY) {
            // 3=マンスリー
            // 翌月1日にする
            expire = base
                    .withDayOfMonth(1)
                    .plusMonths(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (type == ONECE) {
            // 4=単発
            // 期限なし、とりあえず9999年12月31日
            expire = base
                    // XXXX年1月1日
                    .withDayOfYear(1)
                    // 10000年1月1日
                    .withYear(10000)
                    // 9999年12月31日
                    .minusDays(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (type == QUARTRELY) {
            // 5=他
            // 月(1-12)を-1して4で割った後切り捨てて4をかけて4を足す。
            // これにより4月,8月,12月のいずれかになる。(クオータリー最終月)
            int month = (base.getMonthValue() - 1) / 4 * 4 + 4;
            // クオータリー最終月の翌月1日
            expire = base.withDayOfMonth(1)
                    // クオータリー最終月にする
                    .withMonth(month)
                    // その翌月
                    .plusMonths(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        }
        if (expire != null) {
            bean.setExpire(Logs.DATE_FORMAT.format(expire));
        }

        return bean;
    }
}
