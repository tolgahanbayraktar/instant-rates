package dynoapps.exchange_rates.converters;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dynoapps.exchange_rates.model.rates.BaseRate;
import dynoapps.exchange_rates.model.rates.EnparaRate;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by erdemmac on 24/11/2016.
 */

public class EnparaConverter implements Converter<ResponseBody, List<BaseRate>> {

    /**
     * Factory for creating converter. We only care about decoding responses.
     **/
    public static final class Factory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                                Annotation[] annotations,
                                                                Retrofit retrofit) {
            return INSTANCE;
        }

    }

    private static final String HOST = "http://www.qnbfinansbank.enpara.com";

    private EnparaConverter() {
    }

    static final EnparaConverter INSTANCE = new EnparaConverter();


    @Override
    public List<BaseRate> convert(ResponseBody value) throws IOException {

        ArrayList<BaseRate> rates = new ArrayList<>();
        String responseBody = value != null ? value.string() : null;

        final Elements shotElements = Jsoup.parse(responseBody, HOST).select("#pnlContent span dl");
        for (Element element : shotElements) {
            BaseRate rate = parseRate(element);
            if (rate != null) {
                rates.add(rate);
            }
        }
        return rates;
    }

    private EnparaRate parseRate(Element element) {
        final Elements divElements = element.select("div");
        EnparaRate rate = new EnparaRate();
        if (divElements.size() > 2) {
            rate.type = divElements.get(0).text();
            rate.value_buy = divElements.get(1).text();
            rate.value_sell = divElements.get(2).text();
        }
        rate.toRateType();
        rate.setRealValues();

        return rate;
    }


}