package logbook.internal.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest;

import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * {@link TextField}にサジェスト機能を追加する
 *
 */
public class SuggestSupport implements Callback<ISuggestionRequest, Collection<String>> {

    private List<String> values;

    private BiPredicate<String, String> predicate;

    /**
     * サジェスト機能を作成します。<br>
     * これは次の記述と同等です
     * <blockquote>
     *     <code>new SuggestSupport(String::startsWith, values)</code>
     * </blockquote>
     *
     * @param values サジェストで表示する候補
     */
    public SuggestSupport(String... values) {
        this(String::startsWith, values);
    }

    /**
     * サジェスト機能を作成します。<br>
     *
     * @param predicate 候補のフィルタ条件
     * @param values サジェストで表示する候補
     */
    public SuggestSupport(BiPredicate<String, String> predicate, String... values) {
        this(predicate, Arrays.asList(values));
    }

    /**
     * サジェスト機能を作成します。<br>
     * これは次の記述と同等です
     * <blockquote>
     *     <code>new SuggestSupport(String::startsWith, values)</code>
     * </blockquote>
     *
     * @param values サジェストで表示する候補
     */
    public SuggestSupport(List<String> values) {
        this(String::startsWith, new ArrayList<>(values));
    }

    /**
     * サジェスト機能を作成します。<br>
     *
     * @param predicate 候補のフィルタ条件
     * @param values サジェストで表示する候補
     */
    public SuggestSupport(BiPredicate<String, String> predicate, List<String> values) {
        this.predicate = predicate;
        this.values = values;
    }

    @Override
    public Collection<String> call(ISuggestionRequest param) {
        String text = param.getUserText();
        if (text.isEmpty()) {
            return this.values;
        } else {
            return this.values.stream()
                    .filter(e -> predicate.test(e, text))
                    .collect(Collectors.toList());
        }
    }
}
