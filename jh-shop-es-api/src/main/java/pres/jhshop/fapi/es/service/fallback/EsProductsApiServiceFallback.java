package pres.jhshop.fapi.es.service.fallback;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pres.jhshop.fapi.es.dto.EsProductsFeignQueryDTO;
import pres.jhshop.fapi.es.dto.req.EsProductsBatchCreateOrUpdateReq;
import pres.jhshop.fapi.es.dto.req.EsProductsFeignQueryReq;
import pres.jhshop.fapi.es.service.EsProductsApiService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenJiahao(五条)
 * @date 2024/12/23 23:16:42
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class EsProductsApiServiceFallback implements EsProductsApiService {

    private Throwable throwable;

    @Override
    public void batchCreateOrUpdateReq(List<EsProductsBatchCreateOrUpdateReq> batchCreateOrUpdateReq) {
        log.error("EsProductsApiServiceFallback--batchCreateOrUpdateReq--fallback, error msg[{}]", throwable.getMessage());
    }

    @Override
    public List<EsProductsFeignQueryDTO> list(EsProductsFeignQueryReq queryReq) {
        log.error("EsProductsApiServiceFallback--list--fallback, error msg[{}]", throwable.getMessage());
        return new ArrayList<>();
    }
}
