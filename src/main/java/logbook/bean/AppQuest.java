package logbook.bean;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import logbook.bean.QuestList.Quest;
import logbook.internal.Logs;
import lombok.Data;

/**
 * 任務
 */
@Data
public class AppQuest implements Serializable {

    private static final long serialVersionUID = 4212109733911812553L;

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
        if (quest.getType() == 1) {
            // 1=デイリー
            // 1日加算
            expire = base.plusDays(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (quest.getType() == 2) {
            // 2=ウィークリー
            // 7日加算して曜日(1(月曜日)から7(日曜日))-1を減算する
            expire = base.plusWeeks(1)
                    .minusDays(base.getDayOfWeek().getValue() - 1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (quest.getType() == 3) {
            // 3=マンスリー
            // 1ヶ月加算して1日にする
            expire = base.plusMonths(1)
                    .withDayOfMonth(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (quest.getType() == 4) {
            // 4=単発
        } else if (quest.getType() == 5) {
            // 5=他
            expire = base.plusDays(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        }
        if (expire != null) {
            bean.setExpire(Logs.DATE_FORMAT.format(expire));
        }

        return bean;
    }
}
