package pers.jhshop.es.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.es.model.req.EsProductsCreateOrUpdateReq;
import pers.jhshop.es.model.req.EsProductsQueryReq;
import pers.jhshop.es.model.vo.EsProductsQueryVo;

/**
 * 商品服务类
 * @author ChenJiahao(五条)
 * @date 2024/12/21 16:39:10
 */
public interface ProductsService {

    void createOrUpdateBiz(EsProductsCreateOrUpdateReq createReq);

    EsProductsQueryVo getByIdBiz(Long id);

    Page<EsProductsQueryVo> pageBiz(EsProductsQueryReq queryReq);
}
