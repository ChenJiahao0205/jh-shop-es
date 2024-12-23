package pres.jhshop.fapi.es.service.fallback;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import pres.jhshop.fapi.es.service.EsProductsApiService;

/**
 * @author ChenJiahao(五条)
 * @date 2024/12/23 23:16:53
 */
@Component
public class EsProductsApiServiceFallbackFactory implements FallbackFactory<EsProductsApiService> {

    @Override
    public EsProductsApiService create(Throwable cause) {
        return new EsProductsApiServiceFallback(cause);
    }
}
