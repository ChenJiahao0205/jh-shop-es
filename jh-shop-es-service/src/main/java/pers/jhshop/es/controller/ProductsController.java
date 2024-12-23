package pers.jhshop.es.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pers.jhshop.common.entity.ResultBo;
import pers.jhshop.es.consts.JhShopEsApiConstants;
import pers.jhshop.es.model.es.EsProductsEntity;
import pers.jhshop.es.model.req.EsProductsCreateOrUpdateReq;
import pers.jhshop.es.model.req.EsProductsQueryReq;
import pers.jhshop.es.model.vo.EsProductsQueryVo;
import pers.jhshop.es.service.ProductsService;

import java.util.List;

/**
 * 商品信息控制层
 * @author ChenJiahao(五条)
 * @date 2024/12/21 16:35:00
 */
@Slf4j
@RestController
@RequestMapping(JhShopEsApiConstants.API_USER + "products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService productsService;

    @PostMapping("createOrUpdate")
    public ResultBo createOrUpdate(@RequestBody EsProductsCreateOrUpdateReq createReq) {
        productsService.createOrUpdateBiz(createReq);
        return ResultBo.success();
    }

    @PostMapping("batchInsertOrUpdate")
    public ResultBo batchInsertOrUpdate(@RequestBody List<EsProductsCreateOrUpdateReq> dataList) {
        productsService.batchInsertOrUpdate(dataList);
        return ResultBo.success();
    }

    @GetMapping("getById")
    public ResultBo<EsProductsQueryVo> getById(Long id) {
        return ResultBo.success(productsService.getByIdBiz(id));
    }

    @PostMapping("page")
    public ResultBo<Page<EsProductsEntity>> page(@RequestBody EsProductsQueryReq queryReq) {
        return ResultBo.success(productsService.pageBiz(queryReq));
    }

}
