package pers.jhshop.es.feign;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import pers.jhshop.es.model.es.EsProductsEntity;
import pers.jhshop.es.model.req.EsProductsCreateOrUpdateReq;
import pers.jhshop.es.model.req.EsProductsQueryReq;
import pers.jhshop.es.service.ProductsService;
import pres.jhshop.fapi.es.dto.EsProductsFeignQueryDTO;
import pres.jhshop.fapi.es.dto.req.EsProductsBatchCreateOrUpdateReq;
import pres.jhshop.fapi.es.dto.req.EsProductsFeignQueryReq;
import pres.jhshop.fapi.es.service.EsProductsApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChenJiahao(五条)
 * @date 2024/12/23 23:08:14
 */
@RestController
@AllArgsConstructor
public class EsProductsApiServiceFeign implements EsProductsApiService {

    private final ProductsService productsService;

    @Override
    public void batchCreateOrUpdateReq(List<EsProductsBatchCreateOrUpdateReq> batchCreateOrUpdateReq) {
        if (CollectionUtils.isEmpty(batchCreateOrUpdateReq)){
            return;
        }

        List<EsProductsCreateOrUpdateReq> dataList = batchCreateOrUpdateReq.stream()
                .map(feignEntity -> {
                    EsProductsCreateOrUpdateReq req = new EsProductsCreateOrUpdateReq();
                    BeanUtil.copyProperties(feignEntity, req);
                    return req;
                })
                .collect(Collectors.toList());

        productsService.batchInsertOrUpdate(dataList);
    }

    @Override
    public List<EsProductsFeignQueryDTO> list(EsProductsFeignQueryReq queryReq) {
        EsProductsQueryReq req = new EsProductsQueryReq();
        BeanUtil.copyProperties(queryReq, req);

        List<EsProductsEntity> list = productsService.list(req);
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }

        return list.stream()
                .map(l ->{
                    EsProductsFeignQueryDTO esProductsFeignQueryDTO = new EsProductsFeignQueryDTO();
                    BeanUtil.copyProperties(l, esProductsFeignQueryDTO);
                    return esProductsFeignQueryDTO;
                }).collect(Collectors.toList());
    }
}
