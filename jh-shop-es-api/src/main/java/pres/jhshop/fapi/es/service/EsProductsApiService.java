package pres.jhshop.fapi.es.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pers.jhshop.common.consts.ServiceConst;
import pres.jhshop.fapi.es.dto.EsProductsFeignQueryDTO;
import pres.jhshop.fapi.es.dto.req.EsProductsBatchCreateOrUpdateReq;
import pres.jhshop.fapi.es.dto.req.EsProductsFeignQueryReq;
import pres.jhshop.fapi.es.service.fallback.EsProductsApiServiceFallbackFactory;

import java.util.List;

/**
 * @author ChenJiahao(五条)
 * @date 2024/12/23 22:58:58
 */
@FeignClient(value = ServiceConst.SERVICE_NAME_ES, contextId = "EsProductsApiService", fallbackFactory = EsProductsApiServiceFallbackFactory.class)
public interface EsProductsApiService {

    @PostMapping("batchCreateOrUpdateReq")
    void batchCreateOrUpdateReq(@RequestBody List<EsProductsBatchCreateOrUpdateReq> batchCreateOrUpdateReq);

    @PostMapping("list")
    List<EsProductsFeignQueryDTO> list(@RequestBody EsProductsFeignQueryReq queryReq);
}
