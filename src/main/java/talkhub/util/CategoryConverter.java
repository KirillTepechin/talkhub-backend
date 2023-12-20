package talkhub.util;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.stereotype.Component;
import talkhub.model.enums.Category;
import org.springframework.core.convert.converter.Converter;
@Component
public class CategoryConverter extends StdConverter<String, Category> implements Converter<String, Category>  {
    @Override
    public Category convert(String source) {
        if ("Игры".equalsIgnoreCase(source)) {
            return Category.GAMING;
        } else if ("Музыка".equalsIgnoreCase(source)) {
            return Category.MUSIC;
        } else if ("Спорт".equalsIgnoreCase(source)) {
            return Category.SPORTS;
        } else if ("Книги".equalsIgnoreCase(source)) {
            return Category.BOOKS_LITERATURE;
        } else if ("Фильмы и сериалы".equalsIgnoreCase(source)) {
            return Category.MOVIES_TV;
        } else if ("Политика".equalsIgnoreCase(source)) {
            return Category.POLITICS;
        } else if ("Еда и напитки".equalsIgnoreCase(source)) {
            return Category.FOOD_DRINK;
        } else if ("Путешествия".equalsIgnoreCase(source)) {
            return Category.TRAVEL;
        } else if ("Финансы".equalsIgnoreCase(source)) {
            return Category.FINANCE;
        } else {
            return Category.IT;
        }
    }
}
