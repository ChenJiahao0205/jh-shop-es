package pers.jhshop.es.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.jhshop.common.utils.JhShopDateUtil;
import pers.jhshop.es.model.es.EsProductsEntity;
import pers.jhshop.es.model.req.EsProductsCreateOrUpdateReq;
import pers.jhshop.es.model.req.EsProductsQueryReq;
import pers.jhshop.es.model.vo.EsProductsQueryVo;
import pers.jhshop.es.service.ProductsService;
import pers.jhshop.es.service.es.EsProductsServiceRestTemplateService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 * @author ChenJiahao(五条)
 * @date 2024/12/21 16:39:52
 */
@Service
@Slf4j
@AllArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    private EsProductsServiceRestTemplateService esProductsServiceRestTemplateService;

    @Override
    public void createOrUpdateBiz(EsProductsCreateOrUpdateReq createReq) {
        // req转换为Entity
        EsProductsEntity esProductsEntity = new EsProductsEntity();
        BeanUtil.copyProperties(createReq, esProductsEntity, "createdAt", "updatedAt", "createTime", "updateTime");
        // 特殊字段处理
        esProductsEntity.setCreatedAt(JhShopDateUtil.getStrTime(createReq.getCreatedAt()));
        esProductsEntity.setUpdatedAt(JhShopDateUtil.getStrTime(createReq.getUpdatedAt()));
        esProductsEntity.setCreateTime(JhShopDateUtil.getStrTime(createReq.getCreateTime()));
        esProductsEntity.setUpdateTime(JhShopDateUtil.getStrTime(createReq.getUpdateTime()));

        esProductsServiceRestTemplateService.insertOrUpdate(esProductsEntity);
    }

    @Override
    public EsProductsQueryVo getByIdBiz(Long id) {
        EsProductsEntity byId = esProductsServiceRestTemplateService.getById(id);
        return convertEsProductsEntityToVo(byId);
    }

    @Override
    public Page<EsProductsQueryVo> pageBiz(EsProductsQueryReq queryReq) {
        Page<EsProductsEntity> page = esProductsServiceRestTemplateService.page(queryReq);
        List<EsProductsQueryVo> productsQueryVoList = page.getRecords().stream()
                .map(r -> {
                    EsProductsQueryVo vo = convertEsProductsEntityToVo(r);
                    return vo;
                }).collect(Collectors.toList());

        // TODO 后续修改，这里不应该多转换一次(看看能否直接修改page的泛型，如果修改不了，就延续现在的代码逻辑)
        Page<EsProductsQueryVo> pageVo = new Page<>();
        BeanUtil.copyProperties(page, pageVo, "records");
        pageVo.setRecords(productsQueryVoList);
        return pageVo;
    }

    /**
     * 转换entity为vo
     */
    private EsProductsQueryVo convertEsProductsEntityToVo(EsProductsEntity byId) {
        EsProductsQueryVo vo = new EsProductsQueryVo();
        BeanUtil.copyProperties(byId, vo, "createdAt", "updatedAt", "createTime", "updateTime");
        // 特殊字段处理
        vo.setCreatedAt(DateUtil.date(JhShopDateUtil.getDateByStrTime(byId.getCreatedAt())));
        vo.setUpdatedAt(DateUtil.date(JhShopDateUtil.getDateByStrTime(byId.getUpdatedAt())));
        vo.setCreateTime(DateUtil.date(JhShopDateUtil.getDateByStrTime(byId.getCreateTime())));
        vo.setUpdateTime(DateUtil.date(JhShopDateUtil.getDateByStrTime(byId.getUpdateTime())));
        return vo;
    }
}
